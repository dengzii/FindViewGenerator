package com.dengzii.plugin.findview;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;

import javax.swing.*;
import java.util.*;

public class GenerateFindView extends AnAction {

    private static final String LAYOUT_REF_PREFIX = "R.layout.";
    private static final String LAYOUT_FILE_SUFFIX = ".xml";

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if (project == null || psiFile == null || !isJavaLang(psiFile)) {
            return;
        }

        List<String> layoutRefExpr = PsiFileUtils.findIntactReferenceExpressionStartWith(psiFile, LAYOUT_REF_PREFIX);
        List<PsiFile> layoutPsi = new ArrayList<>();

        for (String layoutName : layoutRefExpr) {
            String fileName = layoutName.substring(LAYOUT_REF_PREFIX.length()) + LAYOUT_FILE_SUFFIX;
            PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, GlobalSearchScope.allScope(project));
            layoutPsi.addAll(Arrays.asList(psiFiles));
        }

        Map<String, Map<String, String>> layoutFileIndex = new HashMap<>();
        for (PsiFile p : layoutPsi) {
            if (p instanceof XmlFile) {
                layoutFileIndex.put(p.getName(), PsiFileUtils.getIdAndTagNameFrom(((XmlFile) p)));
            }
        }

        print("LAYOUT_FILE", Arrays.toString(layoutRefExpr.toArray()));

        Dialog dialog = new Dialog(project, wrap(layoutFileIndex));
        dialog.show();
        print(project, psiFile);
    }

    private Map<String, List<AndroidView>> wrap(Map<String, Map<String, String>> layoutFileIndex){

        Map<String, List<AndroidView>> result = new HashMap<>();
        for (String f : layoutFileIndex.keySet()) {
            result.put(f, new ArrayList<>());
            for (String id : layoutFileIndex.get(f).keySet()) {
                String fullViewName = layoutFileIndex.get(f).get(id);
                String className = fullViewName.contains(".")
                        ? fullViewName.substring(fullViewName.lastIndexOf(".") + 1)
                        : fullViewName;
                result.get(f).add(new AndroidView(className, id, f, fullViewName));
            }
        }
        return result;
    }

    private boolean isJavaLang(PsiFile psiFile) {
        return psiFile.getLanguage().is(Language.findLanguageByID("JAVA"));
    }

    private void d(AnActionEvent e) {
        // invokeLater
        // The way to pass control from background thread to the event dispatch thread
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("GenerateFindView.run");
            }
        }, ModalityState.any());

        // NOT THAT WAY, This way is use for default swing kit
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            }
        });

        /* Access fle-based index, this will run after all processes have bean complete */
        DumbService.getInstance(e.getProject()).smartInvokeLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private static void print(Object... objects) {
        for (Object o : objects) {
            System.out.print(o.toString() + " ");
        }
        System.out.println("");
    }
}
