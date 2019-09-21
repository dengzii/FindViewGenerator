package com.dengzii.plugin.findview;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.openapi.util.BusyObject;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
        content.setBusyObject(new BusyObject() {
            @NotNull
            @Override
            public ActionCallback getReady(@NotNull Object o) {
                System.out.println("====> "+o.toString());
                return new ActionCallback.Done();
            }
        });

        button1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField1.setText(project.getName());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        toolWindow.getContentManager().addContent(content);
    }
}
