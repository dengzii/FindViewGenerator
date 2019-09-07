package com.dengzii.findview;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.impl.IdeFrameImpl;

public class GenerateFindViewAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();
        Messages.showMessageDialog(project, "HelloWorld","Title1", Messages.getInformationIcon());

        System.out.println(e.getPlace());
        System.out.println(e.getInputEvent().getSource());
        System.out.println(e.getModifiers());
        if (e.getInputEvent().getSource() instanceof IdeFrameImpl){
            IdeFrameImpl ideFrame = ((IdeFrameImpl) e.getInputEvent().getSource());
            System.out.println(ideFrame.getInsets().toString());
            System.out.println(ideFrame.getContentPane().getName());
        }
    }

    private void showEvent(){

    }
}
