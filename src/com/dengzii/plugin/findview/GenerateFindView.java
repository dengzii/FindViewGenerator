package com.dengzii.plugin.findview;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateFindView extends AnAction {

    private static final String LAYOUT_REF_PREFIX = "R.layout.";
    private static final String LAYOUT_FILE_SUFFIX = ".xml";

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if (project == null || psiFile == null) {
            return;
        }

        List<String> layoutRefExpr = PsiFileUtils.findIntactReferenceExpressionStartWith(psiFile, LAYOUT_REF_PREFIX);
        List<PsiFile> layoutPsi = new ArrayList<>();

        for (String layoutName : layoutRefExpr) {
            String fileName = layoutName.substring(LAYOUT_REF_PREFIX.length()) + LAYOUT_FILE_SUFFIX;
            PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, GlobalSearchScope.allScope(project));
            layoutPsi.addAll(Arrays.asList(psiFiles));
        }

        print("LAYOUT_FILE", Arrays.toString(layoutRefExpr.toArray()));
        print("LAYOUT_PSI", Arrays.toString(layoutPsi.toArray()));

        print(project, psiFile);
    }

        private List<String> findLayoutRes() {
        return null;
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

        backgroundProcess();
        /* Access fle-based index, this will run after all processes have bean complete */
        DumbService.getInstance(e.getProject()).smartInvokeLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void backgroundProcess() {
        ProgressManager.progress("hello");

    }

    private static void print(Object... objects) {
        for (Object o : objects) {
            System.out.print(o.toString() + " ");
        }
        System.out.println("");
    }
}
