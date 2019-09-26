package com.dengzii.plugin.findview;

import com.dengzii.plugin.findview.gen.FindViewCodeWriter;
import com.dengzii.plugin.findview.ui.ViewIdMappingDialog;
import com.dengzii.plugin.findview.utils.PsiFileUtils;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.*;
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
import org.jetbrains.kotlin.psi.KtClass;

import javax.swing.*;
import java.util.List;

public class MainAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        if (project == null || psiFile == null || editor == null) {
            return;
        }

        PsiFileUtils.getViewInfoFromPsiFile(psiFile, project);

        PsiClass psiClass = getPsiClass(editor, psiFile);
        KtClass ktClass = getKtClass(editor, psiFile);

        ViewIdMappingDialog viewIdMappingDialog = new ViewIdMappingDialog(project,
                PsiFileUtils.getViewInfoFromPsiFile(psiFile, project));

        if (viewIdMappingDialog.showAndGet()) {
            List<ViewInfo> viewInfos = viewIdMappingDialog.getResult();
            WriteCommandAction.writeCommandAction(project).run(new FindViewCodeWriter(psiClass, project, viewInfos));
        }
    }

    private boolean isJavaKotlinLang(PsiFile psiFile) {
        return psiFile.getVirtualFile().getName().endsWith(".java")
                || psiFile.getVirtualFile().getName().endsWith(".kt");
    }

    private KtClass getKtClass(Editor editor, PsiFile file) {

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);

        if (element == null)
            element = file.findElementAt(offset - 1);
        if (element == null) {
            return null;
        } else {
            KtClass target = (KtClass) PsiTreeUtil.getParentOfType(element, KtClass.class);
            if (target == null) {
                element = file.findElementAt(offset - 1);
                if (element == null)
                    return null;
                target = (KtClass) PsiTreeUtil.getParentOfType(element, KtClass.class);
            }
            return target instanceof SyntheticElement ? null : target;
        }
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
