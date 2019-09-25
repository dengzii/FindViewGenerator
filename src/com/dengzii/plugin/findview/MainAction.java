package com.dengzii.plugin.findview;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntheticElement;
import com.intellij.psi.util.PsiTreeUtil;

import javax.swing.*;
import java.util.List;

public class MainAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (project == null || psiFile == null || !isJavaLang(psiFile) || editor == null) {
            return;
        }
        PsiClass psiClass = getPsiClass(editor, psiFile);

        ViewIdMappingDialog viewIdMappingDialog = new ViewIdMappingDialog(project, LayoutFindUtils.findJava(psiFile, project));
        if (viewIdMappingDialog.showAndGet()) {
            List<AndroidView> androidViews = viewIdMappingDialog.getResult();
            WriteCommandAction.writeCommandAction(project).run(new FindViewCodeGenerator(psiClass, project, androidViews));
        }
    }

    private boolean isJavaLang(PsiFile psiFile) {
        return psiFile.getLanguage().is(Language.findLanguageByID("JAVA"))
                || psiFile.getLanguage().is(Language.findLanguageByID("KOTLIN"));
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
