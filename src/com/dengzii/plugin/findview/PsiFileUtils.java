package com.dengzii.plugin.findview;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PsiFileUtils {

    private static final String ANDROID_ID_ATTR_NAME = "android:id";
    private static final String ANDROID_ID_PREFIX = "@+id/";

    public static Map<String, String> getIdAndTagNameFrom(XmlFile xmlFile) {
        Map<String, String> result = new HashMap<>();
        visitAndFindChild(xmlFile, result);
        return result;
    }

    public static List<String> findIntactReferenceExpressionStartWith(PsiFile psiFile, String text) {

        List<String> list = new ArrayList<>();
        visitAndFindChild(psiFile, list, text);
        return list;
    }

    private static void visitAndFindChild(PsiElement psiElement, final Map<String, String> result) {
        psiElement.acceptChildren(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);
                if (element instanceof XmlTag) {
                    XmlTag tag = ((XmlTag) element);
                    String id = tag.getAttributeValue(ANDROID_ID_ATTR_NAME);
                    String type = tag.getName();
                    if (id != null) {
                        result.put(id.replace(ANDROID_ID_PREFIX, ""), type);
                    }
                }
                visitAndFindChild(element, result);
            }
        });
    }

    private static void visitAndFindChild(PsiElement psiElement, final List<String> result, final String text) {

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
