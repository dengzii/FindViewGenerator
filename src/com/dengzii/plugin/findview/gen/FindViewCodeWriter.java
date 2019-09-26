package com.dengzii.plugin.findview.gen;

import com.dengzii.plugin.findview.ViewInfo;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ThrowableRunnable;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtFile;

import java.util.List;

public class FindViewCodeWriter implements ThrowableRunnable<RuntimeException> {


    private PsiFile psiFile;
    private List<ViewInfo> viewInfos;
    private Editor editor;

    public FindViewCodeWriter(PsiFile psiFile, Editor editor, List<ViewInfo> viewInfos) {
        this.psiFile = psiFile;
        this.viewInfos = viewInfos;
    }

    @Override
    public void run() throws RuntimeException {

        JavaCase javaCase = new JavaCase();
        javaCase.setNext(new KotlinCase());
        javaCase.dispose(psiFile, viewInfos);
    }


    private PsiClass getPsiClass(Editor editor, PsiFile file) {

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);

        if (element == null)
            element = file.findElementAt(offset - 1);
        if (element == null) {
            return null;
        } else {
            PsiClass target = (PsiClass) PsiTreeUtil.getParentOfType(element, PsiClass.class);
            if (target == null) {
                element = file.findElementAt(offset - 1);
                if (element == null)
                    return null;
                target = (PsiClass) PsiTreeUtil.getParentOfType(element, PsiClass.class);
            }
            return target instanceof SyntheticElement ? null : target;
        }
    }
}
