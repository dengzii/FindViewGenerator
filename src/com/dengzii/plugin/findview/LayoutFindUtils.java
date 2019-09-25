package com.dengzii.plugin.findview;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;

import java.util.*;

public class LayoutFindUtils {

    private static final String LAYOUT_REF_PREFIX = "R.layout.";
    private static final String LAYOUT_FILE_SUFFIX = ".xml";

    public static Map<String, List<AndroidView>> findJava(PsiFile psiFile, Project project) {

        List<String> layoutRefExpr = PsiFileUtils.findIntactReferenceExpressionStartWith(psiFile, LAYOUT_REF_PREFIX);
        List<PsiFile> layoutPsi = new ArrayList<>();

        for (String layoutName : layoutRefExpr) {
            String fileName = layoutName.substring(LAYOUT_REF_PREFIX.length()) + LAYOUT_FILE_SUFFIX;
            PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, GlobalSearchScope.allScope(project));
            layoutPsi.addAll(Arrays.asList(psiFiles));
        }

        Map<String, List<AndroidView>> layoutViews = new HashMap<>();
        for (PsiFile p : layoutPsi) {
            if (p instanceof XmlFile) {
                layoutViews.put(p.getName(), PsiFileUtils.getAndroidViewInfoFrom(((XmlFile) p)));
            }
        }
        return layoutViews;
    }
}
