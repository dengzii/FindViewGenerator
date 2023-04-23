package com.dengzii.plugin.findview.gen

import com.dengzii.plugin.findview.ViewInfo
import com.intellij.psi.PsiFile

/**
 * <pre>
 * author : dengzi
 * e-mail : dengzii@foxmail.com
 * github : [...](https://github.com/dengzii)
 * time   : 2019/9/27
 * desc   :
</pre> *
 */
abstract class BaseCase {
    private var next: BaseCase? = null
    abstract fun dispose(psiElement: PsiFile, viewInfos: List<ViewInfo>)
    fun setNext(next: BaseCase?) {
        this.next = next
    }

    protected operator fun next(): BaseCase? {
        return next
    }

    protected fun next(psiElement: PsiFile, viewInfos: List<ViewInfo>) {
        if (next != null) {
            next!!.dispose(psiElement, viewInfos)
        }
    }
}
