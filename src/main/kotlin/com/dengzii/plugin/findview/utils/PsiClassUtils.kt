package com.dengzii.plugin.findview.utils

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod

/**
 * <pre>
 * author : dengzi
 * e-mail : dengzii@foxmail.com
 * github : [...](https://github.com/dengzii)
 * time   : 2019/9/30
 * desc   :
</pre> *
 */
object PsiClassUtils {
    fun getMethod(psiClass: PsiClass, methodName: String): PsiMethod? {
        val methods = getMethods(psiClass)
        for (method in methods) {
            if (method.name == methodName) {
                return method
            }
        }
        return null
    }

    private fun getMethods(psiClass: PsiClass): Array<PsiMethod> {
        return psiClass.methods
    }
}
