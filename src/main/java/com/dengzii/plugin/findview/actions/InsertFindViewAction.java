package com.dengzii.plugin.findview.actions;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public class InsertFindViewAction extends WriteCommandAction.Simple {

    protected InsertFindViewAction(Project project, PsiFile... files) {
        super(project, files);

    }

    @Override
    protected void run() throws Throwable {


    }
}
