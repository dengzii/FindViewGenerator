package com.dengzii.plugin.findview;

import com.dengzii.plugin.findview.gen.FindViewCodeWriter;
import com.dengzii.plugin.findview.ui.ViewIdMappingDialog;
import com.dengzii.plugin.findview.utils.PsiFileUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * author : dengzi
 * e-mail : denua@foxmail.com
 * github : https://github.com/MrDenua
 * time   : 2019/9/27
 * desc   :
 * </pre>
 */
public class MainAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        if (Objects.isNull(project) || Objects.isNull(psiFile) || Objects.isNull(editor)) {
            return;
        }

        ViewIdMappingDialog viewIdMappingDialog = new ViewIdMappingDialog(project,
                PsiFileUtils.getViewInfoFromPsiFile(psiFile, project));

        if (viewIdMappingDialog.showAndGet()) {
            List<ViewInfo> viewInfos = viewIdMappingDialog.getResult();
            if (viewInfos.isEmpty()) {
                return;
            }
            WriteCommandAction.writeCommandAction(project).run(new FindViewCodeWriter(psiFile, viewInfos));
        }
    }
}
