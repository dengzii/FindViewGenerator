package com.dengzii.plugin.findview;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class Dialog extends DialogWrapper {

    protected Dialog(@Nullable Project project) {
        super(project);
        init();
        setTitle("Test Dialog");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("testing");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(200, 50));
        dialogPanel.add(label, BorderLayout.NORTH);


        JBList<String> list = new JBList<>();
        JLabel label1 = new JLabel("testing111");
        label1.setPreferredSize(new Dimension(200, 100));
        list.add(label1, 0);
        JLabel label2 = new JLabel("testing222");
        label2.setPreferredSize(new Dimension(200, 100));
        list.add( label1,1);

        JButton jButton = new JButton("button");
        jButton.setPreferredSize(new Dimension(200, 20));
        dialogPanel.add(jButton, BorderLayout.SOUTH);

        dialogPanel.add(list, BorderLayout.CENTER);
        return dialogPanel;
    }
}
