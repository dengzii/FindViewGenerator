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

import javax.swing.*;
import java.util.List;

public class MainAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

        if (project == null || psiFile == null || !isJavaLang(psiFile)) {
            return;
        }

        ViewIdMappingDialog viewIdMappingDialog = new ViewIdMappingDialog(project, LayoutFindUtils.findJava(psiFile, project));
        if (viewIdMappingDialog.showAndGet()) {
            List<AndroidView> androidViews = viewIdMappingDialog.getResult();

        }
    }

    private boolean isJavaLang(PsiFile psiFile) {
        return psiFile.getLanguage().is(Language.findLanguageByID("JAVA"))
                || psiFile.getLanguage().is(Language.findLanguageByID("KOTLIN"));
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
