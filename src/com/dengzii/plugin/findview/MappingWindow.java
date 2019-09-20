package com.dengzii.plugin.findview;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MappingWindow implements ToolWindowFactory {

    private JPanel rootPanel;
    private JTextField textField1;
    private JButton button1;

    public JPanel getContent() {
        return rootPanel;
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(getContent(), "MappingWindow", false);
        toolWindow.getContentManager().addContent(content);
    }

    @Override
    public void init(ToolWindow window) {

    }


    @Override
    public boolean isDoNotActivateOnStart() {
        return false;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
