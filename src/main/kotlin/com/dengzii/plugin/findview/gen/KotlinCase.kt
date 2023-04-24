package com.dengzii.plugin.findview.gen

import com.dengzii.plugin.findview.Config
import com.dengzii.plugin.findview.ViewInfo
import com.dengzii.plugin.findview.utils.KtPsiUtils
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.endOffset
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.ImportPath
import java.util.*

/**
 * <pre>
 * author : dengzi
 * e-mail : dengzii@foxmail.com
 * github : [...](https://github.com/dengzii)
 * time   : 2019/9/27
 * desc   :
</pre> *
 */
class KotlinCase : BaseCase() {

    public override fun dispose(genConfig: GenConfig) {
        if (!genConfig.psiFile.language.`is`(Config.KOTLIN)) {
            next(genConfig)
            return
        }
        val ktClass = getKtClass(genConfig.psiFile) ?: return
        ktClass.body ?: return

        val ktPsiFactory = KtPsiFactory(genConfig.psiFile.project)
        checkAndCreateClassBody(ktClass, ktPsiFactory)

        if (!INIT_VIEW_BY_LAZY) {
            insertInitViewKtFun(ktClass, ktPsiFactory)
        }

        insertViewFieldStr(genConfig, ktPsiFactory, ktClass)

        if (genConfig.autoImport) {
            insertImports(ktClass, genConfig.viewInfo, ktPsiFactory)
        }
    }

    private fun insertImports(ktClass: KtClass, viewInfos: List<ViewInfo>, ktPsiFactory: KtPsiFactory) {
        val ktFile = ktClass.parent as? KtFile ?: return

        val types = viewInfos.map { it.fullType }.toMutableSet().map {
            Config.AndroidViewClasses[it] ?: it
        }.toMutableList()

        ktFile.importList?.imports?.mapNotNull { it.importPath?.pathStr }?.forEach { import ->
            import.endsWith(".*").let { isAllImport ->
                if (isAllImport) {
                    types.removeAll { t -> t.startsWith(import.substringBeforeLast(".")) }
                } else {
                    types.remove(import)
                }
            }
        }

//        val import = types.mapNotNull { if (it.contains(".")) FqName.topLevel(Name.identifier(it)) else null }
//        ktFile.addImports(import)

        types.forEach {
            if (it.contains(".")) {
                ktFile.importList?.addAfter(
                    ktPsiFactory.createImportDirective(ImportPath.fromString(it)),
                    ktFile.importList?.lastChild
                )
            } else {
                println("type not import $it")
            }
        }

    }

    private fun insertViewFieldStr(genConfig: GenConfig, ktPsiFactory: KtPsiFactory, ktClass: KtClass) {
        var insertAt = when (genConfig.insertAt) {
            InsertPlace.FIRST -> ktClass.body!!.lBrace!!.endOffset
            InsertPlace.LAST -> ktClass.body!!.lBrace!!.endOffset
            InsertPlace.Cursor -> genConfig.editor.caretModel.offset
        }

        val properties = ktClass.getProperties().map { it.name }

        val cursorElement = genConfig.psiFile.findElementAt(genConfig.editor.caretModel.offset)
        val needPrivateModifier = cursorElement?.parent is KtClassBody

        for (viewInfo in genConfig.viewInfo.reversed()) {
            if (properties.contains(viewInfo.field)) {
                println("Field ${viewInfo.field} already exists.")
                continue
            }
            if (!needPrivateModifier && genConfig.insertAt == InsertPlace.Cursor) {
                val expr = String.format(
                    STATEMENT_FIND_VIEW,
                    viewInfo.field,
                    viewInfo.type,
                    viewInfo.id
                )
                genConfig.editor.document.insertString(insertAt, expr)
                insertAt += expr.length
            } else {
                val lazyViewProperty = String.format(
                    STATEMENT_LAZY_INIT_VIEW,
                    MODIFIER_INIT_VIEW_PROPERTY,
                    viewInfo.field,
                    viewInfo.type,
                    viewInfo.id
                )
                val property = ktPsiFactory.createProperty(lazyViewProperty)
                ktClass.body?.addAfter(property, ktClass.body!!.lBrace)
                insertAt += lazyViewProperty.length
            }
        }
    }

    private fun insertViewField(
        viewInfo: ViewInfo,
        ktPsiFactory: KtPsiFactory,
        ktClass: KtClass,
        after: PsiElement? = null
    ): PsiElement? {
        if (ktClass.getProperties().map { it.name }.contains(viewInfo.field)) {
            println("field ${viewInfo.field} already exists")
            return after
        }

        val body = ktClass.body
        val lazyViewProperty = String.format(
            STATEMENT_LAZY_INIT_VIEW,
            MODIFIER_INIT_VIEW_PROPERTY,
            viewInfo.field,
            viewInfo.type,
            viewInfo.id
        )
        val ktProperty = ktPsiFactory.createProperty(lazyViewProperty)
        body?.addAfter(ktProperty, after)
        return null
    }

    private fun insertInitViewKtFun(ktClass: KtClass, factory: KtPsiFactory) {
        val firstFun: PsiElement? = KtPsiUtils.getFirstFun(ktClass)
        val ktClassBody = ktClass.body
        val rBrace = ktClassBody!!.rBrace
        val initViewFun: KtFunction = factory.createFunction(String.format(FUN_INIT_VIEW, Config.METHOD_INIT_VIEW))
        ktClassBody.addBefore(initViewFun, if (Objects.isNull(firstFun)) rBrace else firstFun)
    }

    private fun checkAndCreateClassBody(ktClass: KtClass?, ktPsiFactory: KtPsiFactory) {
        if (Objects.isNull(ktClass!!.body)) {
            ktClass.add(ktPsiFactory.createEmptyClassBody())
        }
    }

    private fun getKtClass(file: PsiFile): KtClass? {
        val psiElements = file.children
        for (element in psiElements) {
            if (element is KtClass) {
                return element
            }
        }
        return null
    }

    companion object {
        private const val STATEMENT_LAZY_INIT_VIEW = "%s val %s by lazy  { findViewById<%s>(R.id.%s) }\n"
        private const val STATEMENT_FIND_VIEW = "val %s = findViewById<%s>(R.id.%s)\n"
        private const val FUN_INIT_VIEW = "private fun %s() {\n\n}"
        private const val MODIFIER_INIT_VIEW_PROPERTY = "private"
        private const val INIT_VIEW_BY_LAZY = true
    }
}
