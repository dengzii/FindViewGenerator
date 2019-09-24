package com.dengzii.plugin.findview;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Dialog extends DialogWrapper {

    private Map<String, List<AndroidView>> mMappingData = new HashMap<>();

    protected Dialog(@Nullable Project project, Map<String,List<AndroidView>> layoutFileIndex) {
        super(project);
        if (layoutFileIndex != null)
            this.mMappingData.putAll(layoutFileIndex);
        init();
        setTitle("Test Dialog");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(16);
        borderLayout.setVgap(16);

        JPanel dialogPanel = new JPanel(borderLayout);
        dialogPanel.setPreferredSize(new Dimension(600, 420));

        JBScrollPane jbScrollPane = new JBScrollPane();
        jbScrollPane.setPreferredSize(new Dimension(600, 50));
        JBList<String> list = new JBList<String>(new XLayoutModel(mMappingData.keySet()));
        jbScrollPane.setViewportView(list);
        dialogPanel.add(jbScrollPane, BorderLayout.CENTER);
        list.setBorder(new BorderUIResource.TitledBorderUIResource("Layout Resource"));

        JBScrollPane jbScrollPane1 = new JBScrollPane();
        jbScrollPane1.setPreferredSize(new Dimension(600, 300));
        GridLayout gridLayout = new GridLayout(25, 4);
        gridLayout.setHgap(5);
        gridLayout.setVgap(5);
        JPanel panelMapping = new JPanel(gridLayout);
        for (String f:mMappingData.keySet()) {
            for (AndroidView androidView : mMappingData.get(f)) {
                panelMapping.add(new JLabel(androidView.layout));
                panelMapping.add(new JLabel(androidView.name));
                panelMapping.add(new JLabel(androidView.id));
                panelMapping.add(new JLabel(androidView.name));
            }
        }
        panelMapping.setBorder(new BorderUIResource.TitledBorderUIResource("Mapping View"));
        jbScrollPane1.setViewportView(panelMapping);
        dialogPanel.add(jbScrollPane1, BorderLayout.SOUTH);

        list.revalidate();

        return dialogPanel;
    }
}

class XLayoutModel extends AbstractListModel<String> {

    private ArrayList<String> list = new ArrayList<String>();

    XLayoutModel(Collection<String> list) {
        this.list.addAll(list);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public String getElementAt(int index) {
        return list.get(index);
    }
}

class XMappingModel extends AbstractListModel<AndroidView> {

    private List<AndroidView> data = new ArrayList<>();
    private int index = 0;

    XMappingModel(Collection<AndroidView> list) {
        data.addAll(list);
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public AndroidView getElementAt(int index) {
        return data.get(index);
    }
}