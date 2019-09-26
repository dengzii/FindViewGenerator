package com.dengzii.plugin.findview.gen;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntheticElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.kotlin.idea.projectView.KtClassOrObjectTreeNode;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtFile;

public class XPsiClass {

    private static final Language JAVA = Language.findLanguageByID("java");
    private static final Language KOTLIN = Language.findLanguageByID("kotlin");

    private KtClass ktClass;
    private PsiClass psiClass;

    XPsiClass(PsiFile psiFile, Editor editor) {
        if (psiFile.getLanguage().is(JAVA)) {
            psiClass = getPsiClass(editor, psiFile);
        }
        if (psiFile.getLanguage().is(KOTLIN)) {
            ktClass = getKtClass((KtFile) psiFile);
        }
    }

    public boolean isValid() {
        return ktClass != null || psiClass != null;
    }

    public void addField(String type, String name, String value) {
        if (!isValid()){
            return;
        }

    }

    public void addMethod(String name) {
        if (!isValid()){
            return;
        }

    }

    private KtClass getKtClass(KtFile file) {

        PsiElement[] psiElements = file.getChildren();
        for (PsiElement element:psiElements){
            if (element instanceof KtClass){
                return ((KtClass) element);
            }
        }

        return null;
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
