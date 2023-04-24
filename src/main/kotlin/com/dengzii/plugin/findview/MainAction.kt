package com.dengzii.plugin.findview

import com.dengzii.plugin.findview.gen.FindViewCodeWriter
import com.dengzii.plugin.findview.gen.GenConfig
import com.dengzii.plugin.findview.tools.ui.showAtCenterScreen
import com.dengzii.plugin.findview.ui.XGenDialog
import com.dengzii.plugin.findview.utils.PsiFileUtils
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction

/**
 * <pre>
 * author : dengzi
 * e-mail : dengzii@foxmail.com
 * github : [...](https://github.com/dengzii)
 * time   : 2019/9/27
 * desc   :
</pre> *
 */
class MainAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val editor = e.getData(CommonDataKeys.EDITOR)
        if (isNull(project!!, psiFile!!, editor!!)) {
            return
        }
        val vis = PsiFileUtils.getViewInfoFromPsiFile(psiFile, project)
        val conf = GenConfig(emptyList(), psiFile, editor)
        XGenDialog(conf, vis) {
            if (it.viewInfo.isEmpty()) {
                return@XGenDialog
            }
            WriteCommandAction.writeCommandAction(project).run(FindViewCodeWriter(it))
        }.showAtCenterScreen()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        val project = e.project
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = true
        if (isNull(project, psiFile, editor)) {
            e.presentation.isEnabledAndVisible = false
            return
        }
        val language = psiFile!!.language
        if (language != Config.JAVA && language != Config.KOTLIN) {
            e.presentation.isEnabledAndVisible = false
        }
    }

    private fun isNull(vararg objects: Any?): Boolean {
        for (o in objects) {
            if (o == null) {
                return true
            }
        }
        return false
    }
}
