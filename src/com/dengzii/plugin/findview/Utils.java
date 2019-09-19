package com.dengzii.plugin.findview;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class Utils {

    public static void showMessage(Project project, String title, String msg, int icon){

        Messages.showMessageDialog(project, msg, title, Messages.getInformationIcon());
    }
}
