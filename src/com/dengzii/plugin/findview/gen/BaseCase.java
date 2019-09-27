package com.dengzii.plugin.findview.gen;

import com.dengzii.plugin.findview.ViewInfo;
import com.intellij.psi.PsiFile;

import java.util.List;

/**
 * <pre>
 * author : dengzi
 * e-mail : denua@foxmail.com
 * github : https://github.com/MrDenua
 * time   : 2019/9/27
 * desc   :
 * </pre>
 */
public abstract class BaseCase {

    private BaseCase next;

    abstract void dispose(PsiFile psiElement, List<ViewInfo> viewInfos);

    final void setNext(BaseCase next) {
        this.next = next;
    }

    protected BaseCase next() {
        return next;
    }

    protected void next(PsiFile psiElement, List<ViewInfo> viewInfos) {
        if (next != null) {
            next.dispose(psiElement, viewInfos);
        }
    }
}
