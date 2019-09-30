package com.dengzii.plugin.findview;

import com.dengzii.plugin.findview.gen.FindViewCodeWriter;
import com.dengzii.plugin.findview.ui.ViewIdMappingDialog;
import com.dengzii.plugin.findview.utils.PsiFileUtils;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

        if (isNull(project, psiFile, editor)) {
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        Project project = e.getProject();
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        e.getPresentation().setEnabledAndVisible(true);

        if (isNull(project, psiFile, editor)) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }
        Language language = psiFile.getLanguage();
        if (!language.is(Config.JAVA) && !language.is(Config.KOTLIN)) {
            e.getPresentation().setEnabledAndVisible(false);
        }

    }

    private boolean isNull(Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }
}
