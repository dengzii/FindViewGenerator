package com.dengzii.plugin.findview.gen

import com.dengzii.plugin.findview.Config
import com.dengzii.plugin.findview.ViewInfo
import com.dengzii.plugin.findview.utils.PsiClassUtils
import com.intellij.psi.*
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
class JavaCase : BaseCase() {
    //    private static final InsertPlace mFieldInsertPlace = InsertPlace.FIRST; // TODO: 2019/9/30 add insert place support
    //    private static final InsertPlace mFindViewInsertPlace = InsertPlace.FIRST; // TODO: 2019/9/30
    public override fun dispose(genConfig: GenConfig) {
        if (!genConfig.psiFile.language.`is`(Config.JAVA)) {
            next(genConfig)
            return
        }
        val factory = JavaPsiFacade.getElementFactory(genConfig.psiFile.project)
        val psiClass = getPsiClass(genConfig.psiFile) ?: return
        val initViewMethod = genInitViewMethod(factory, psiClass)
        for (viewInfo in genConfig.viewInfo) {
            if (!viewInfo.enable) continue
            psiClass.add(genViewDeclareField(factory, viewInfo))
            if (!Objects.isNull(initViewMethod.body)) {
                initViewMethod.body!!.add(genFindViewStatement(factory, viewInfo))
            }
        }
    }

    private fun getPsiClass(file: PsiFile): PsiClass? {
        val psiElements = file.children
        for (element in psiElements) {
            if (element is PsiClass) {
                return element
            }
        }
        return null
    }

    private fun genViewDeclareField(factory: PsiElementFactory, viewInfo: ViewInfo): PsiField {
        val statement = String.format(STATEMENT_FIELD, viewInfo.type, viewInfo.field)
        return factory.createFieldFromText(statement, null)
    }

    private fun genInitViewMethod(factory: PsiElementFactory, psiClass: PsiClass): PsiMethod {
        var method1 = PsiClassUtils.getMethod(psiClass, Config.METHOD_INIT_VIEW)
        if (Objects.isNull(method1)) {
            method1 = factory.createMethod(Config.METHOD_INIT_VIEW, PsiType.VOID)
            psiClass.add(method1)
        }
        method1?.modifierList?.setModifierProperty(PsiModifier.PRIVATE, true)
        return method1!!
    }

    private fun genFindViewStatement(factory: PsiElementFactory, viewInfo: ViewInfo): PsiStatement {
        val findStatement = String.format(STATEMENT_FIND_VIEW, viewInfo.type.trim { it <= ' ' }, viewInfo.id)
        return factory.createStatementFromText(findStatement, null)
    }

    companion object {
        private const val STATEMENT_FIELD = "private %s %s;"
        private const val STATEMENT_FIND_VIEW = "%s = findViewById(R.id.%s);\n"
    }
}
