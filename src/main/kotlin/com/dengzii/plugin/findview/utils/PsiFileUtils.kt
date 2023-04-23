package com.dengzii.plugin.findview.utils;

import com.dengzii.plugin.findview.ViewInfo;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

import java.util.*;

public class PsiFileUtils {

    private static final String LAYOUT_REF_PREFIX = "R.layout.";
    private static final String LAYOUT_FILE_SUFFIX = ".xml";

    private static final String ANDROID_ID_ATTR_NAME = "android:id";
    private static final String ANDROID_ID_PREFIX = "@+id/";

    public static Map<String, List<ViewInfo>> getViewInfoFromPsiFile(PsiFile psiFile, Project project) {

        List<String> layoutRefExpr = PsiFileUtils.findLayoutStatement(psiFile);
        List<PsiFile> layoutPsi = new ArrayList<>();

        for (String layoutName : layoutRefExpr) {
            String fileName = layoutName.substring(LAYOUT_REF_PREFIX.length()) + LAYOUT_FILE_SUFFIX;
            PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, GlobalSearchScope.allScope(project));
            layoutPsi.addAll(Arrays.asList(psiFiles));
        }

        Map<String, List<ViewInfo>> layoutViews = new HashMap<>();
        for (PsiFile p : layoutPsi) {
            if (p instanceof XmlFile) {
                layoutViews.put(p.getName(), getAndroidViewInfoFrom(((XmlFile) p)));
            }
        }
        return layoutViews;
    }

    public static List<ViewInfo> getAndroidViewInfoFrom(XmlFile xmlFile) {
        List<ViewInfo> result = new ArrayList<>();
        visitLayoutXmlFile(xmlFile, xmlFile, result);
        return result;
    }

    public static List<String> findLayoutStatement(PsiFile psiFile) {

        List<String> list = new ArrayList<>();
        visitStatement(psiFile, list);
        return list;
    }

    private static void visitLayoutXmlFile(PsiFile psiFile, PsiElement psiElement, final List<ViewInfo> result) {
        psiElement.acceptChildren(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);
                if (element instanceof XmlTag) {
                    XmlTag tag = ((XmlTag) element);
                    String id = tag.getAttributeValue(ANDROID_ID_ATTR_NAME);
                    String type = tag.getName();
                    if (id != null) {
                        ViewInfo viewInfo = new ViewInfo(
                                type.contains(".") ? type.substring(type.lastIndexOf(".") + 1) : type,
                                id.replace(ANDROID_ID_PREFIX, ""),
                                psiFile.getName(),
                                type
                        );
                        viewInfo.genMappingField();
                        result.add(viewInfo);
                    }
                }
                visitLayoutXmlFile(psiFile, element, result);
            }
        });
    }

    private static void visitStatement(PsiElement psiElement, final List<String> result) {

        psiElement.acceptChildren(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);
                if (isReferenceExpression(element)) {
                    String expression = element.getText();
                    if (expression.startsWith(LAYOUT_REF_PREFIX)) {
                        result.add(expression);
                    }
                }
                visitStatement(element, result);
            }
        });
    }

    private static boolean isReferenceExpression(PsiElement element) {
        return isKotlinExpression(element) || isJavaExpression(element);
    }

    private static boolean isKotlinExpression(PsiElement element) {
        return element.toString().contains("DOT_QUALIFIED_EXPRESSION");
    }

    private static boolean isJavaExpression(PsiElement element) {
        return element instanceof PsiReferenceExpression;
    }
}
