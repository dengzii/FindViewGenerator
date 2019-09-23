package com.dengzii.plugin.findview;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceExpression;

import java.util.ArrayList;
import java.util.List;

public class PsiFileUtils {

    public static List<String> findIntactReferenceExpressionStartWith(PsiFile psiFile, String text) {

        List<String> list = new ArrayList<>();
        visitAndFindChild(psiFile, list, text);
        return list;
    }

    private static void visitAndFindChild(PsiElement psiElement, final List<String> result,final String text) {

        psiElement.acceptChildren(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);
                if (isIntactReferenceExpression(element)) {
                    String expression = element.getText();
                    if (expression.startsWith(text)) {
                        result.add(expression);
                    }
                }
                visitAndFindChild(element, result, text);
            }
        });
    }

    private static boolean isIntactReferenceExpression(PsiElement element) {
        return isPsiReferenceExpression(element) && !isPsiReferenceExpression(element.getParent());
    }

    private static boolean isPsiReferenceExpression(PsiElement element) {
        return element instanceof PsiReferenceExpression;
    }
}
