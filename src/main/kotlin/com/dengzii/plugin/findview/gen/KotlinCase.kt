package com.dengzii.plugin.findview.gen

import com.dengzii.plugin.findview.Config
import com.dengzii.plugin.findview.ViewInfo
import com.dengzii.plugin.findview.utils.KtPsiUtils
import com.intellij.codeInsight.CodeInsightUtilCore
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.idea.kdoc.insert
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtPsiFactory
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
    private val mFieldPlace = InsertPlace.FIRST
    private val mMethodPlace = InsertPlace.FIRST
    public override fun dispose(psiElement: PsiFile, viewInfos: List<ViewInfo>) {
        if (!psiElement.language.`is`(Config.KOTLIN)) {
            next(psiElement, viewInfos)
            return
        }
        val ktClass = getKtClass(psiElement) ?: return
        val ktPsiFactory = KtPsiFactory(psiElement.project)
        checkAndCreateClassBody(ktClass, ktPsiFactory)

        val ktFile = ktClass.parent as? KtFile?
        if (ktFile != null) {
            insertImports(ktFile, viewInfos, ktPsiFactory)
        }

        if (!INIT_VIEW_BY_LAZY) {
            insertInitViewKtFun(ktClass, ktPsiFactory)
        }
        for (viewInfo in viewInfos) {
            insertViewField(viewInfo, ktPsiFactory, ktClass)
        }
    }

    private fun insertImports(ktFile: KtFile, viewInfos: List<ViewInfo>, ktPsiFactory: KtPsiFactory) {
        val types = viewInfos.map { it.type }.toMutableSet()

        // import android.view.View
        val import = ImportPath.fromString("import android.view.View")
        val importView = ktPsiFactory.createImportDirective(import)
        ktFile.importList?.accept(object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)
                if (element.text == importView.text) {
                    return
                }
            }
        })
    }

    private fun insertViewField(viewInfo: ViewInfo, ktPsiFactory: KtPsiFactory, ktClass: KtClass?) {
        val body = ktClass!!.body
        val lBrace = body!!.lBrace
        val lazyViewProperty = String.format(STATEMENT_LAZY_INIT_VIEW,
                MODIFIER_INIT_VIEW_PROPERTY,
                viewInfo.field,
                viewInfo.type,
                viewInfo.id)
        val ktProperty = ktPsiFactory.createProperty(lazyViewProperty)
        body.addAfter(ktProperty, lBrace)
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
        private const val STATEMENT_LAZY_INIT_VIEW = " %s val %s by lazy  { findViewById<%s>(R.id.%s) }"
        private const val FUN_INIT_VIEW = "private fun %s() {\n\n}"
        private const val MODIFIER_INIT_VIEW_PROPERTY = "private"
        private const val INIT_VIEW_BY_LAZY = true
    }
}
