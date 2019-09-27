package com.dengzii.plugin.findview.gen;

import com.dengzii.plugin.findview.ViewInfo;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.util.ThrowableRunnable;

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
public class FindViewCodeWriter implements ThrowableRunnable<RuntimeException> {


    private PsiFile psiFile;
    private List<ViewInfo> viewInfos;

    public FindViewCodeWriter(PsiFile psiFile, List<ViewInfo> viewInfos) {
        this.psiFile = psiFile;
        this.viewInfos = viewInfos;
    }

    @Override
    public void run() throws RuntimeException {

        JavaCase javaCase = new JavaCase();
        javaCase.setNext(new KotlinCase());
        javaCase.dispose(psiFile, viewInfos);
    }
}
