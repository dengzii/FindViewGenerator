package com.dengzii.plugin.findview.actions;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.ThrowableRunnable;
import org.jetbrains.annotations.NotNull;

public class InsertFindViewAction extends WriteCommandAction {

    protected InsertFindViewAction(Project project, PsiFile... files) {
        super(project, files);
        try {
            WriteCommandAction.writeCommandAction(project, files).run((ThrowableRunnable<Throwable>) () -> {

            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    protected void run(@NotNull Result result) throws Throwable {

    }
}
