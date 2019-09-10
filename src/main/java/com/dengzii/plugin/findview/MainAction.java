package com.dengzii.plugin.findview;

import com.dengzii.plugin.findview.utils.XLog;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class MainAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        PsiFile psiFile = e.getData(PlatformDataKeys.PSI_FILE);
        FileEditor fileEditor = e.getData(PlatformDataKeys.FILE_EDITOR);


        XLog.info("isValid", psiFile.isValid());
        XLog.info("name", psiFile.getName());
        XLog.info("type", psiFile.getFileType());
        XLog.info("language", psiFile.getLanguage());
        System.out.println(psiFile.getText());


        PsiFile file = PsiFileFactory.getInstance(e.getProject()).createFileFromText(psiFile.getLanguage(), "HelloWorld");
        psiFile.add(file);

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                new WriteCommandAction(e.getProject()) {

                    @Override
                    protected void run(@NotNull Result result) throws Throwable {

                    }
                };
            }
        });
    }
}
