package com.dengzii.plugin.findview;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Dialog extends DialogWrapper {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private JBPanel contentPanel;
    private JBScrollPane viewIdMappingScroll;
    private JBList<String> layoutFileList;
    private JBPanel viewIdMappingPanel;

    private int mSelectedLayoutFileIndex = 0;
    private List<String> mLayoutFiles = new ArrayList<>();
    private Map<String, List<AndroidView>> mMappingData = new HashMap<>();

    Dialog(@Nullable Project project, Map<String, List<AndroidView>> layoutFileIndex) {
        super(project);
        if (layoutFileIndex != null && !layoutFileIndex.isEmpty()) {
            mMappingData.putAll(layoutFileIndex);
            mLayoutFiles.addAll(mMappingData.keySet());
        }
        init();
        setTitle("Test Dialog");
    }

    private void initContentPanel() {

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(10);
        borderLayout.setVgap(10);

        contentPanel = new JBPanel(borderLayout);
        contentPanel.setPreferredSize(new Dimension(WIDTH + 60, 400));
    }

    private void initLayoutFileList() {

        JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.setPreferredSize(new Dimension(WIDTH, 70));

        layoutFileList = new JBList<String>(new XLayoutModel(mMappingData.keySet()));
        layoutFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane.setViewportView(layoutFileList);
        scrollPane.setBorder(new BorderUIResource.TitledBorderUIResource("Layout Resource"));
        contentPanel.add(scrollPane, BorderLayout.NORTH);

        layoutFileList.addListSelectionListener(e -> {
            mSelectedLayoutFileIndex = layoutFileList.getSelectedIndex();
            refreshViewIdMappingList();
        });
    }

    private void initViewIdMappingPanel() {

        viewIdMappingScroll = new JBScrollPane();
        viewIdMappingScroll.setPreferredSize(new Dimension(WIDTH, 300));

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        viewIdMappingPanel = new JBPanel(flowLayout);
        viewIdMappingPanel.setBorder(new BorderUIResource.TitledBorderUIResource("View Id Mapping"));

        viewIdMappingScroll.setViewportView(viewIdMappingPanel);
        contentPanel.add(viewIdMappingScroll);
        refreshViewIdMappingList();
    }

    private void refreshViewIdMappingList() {

        viewIdMappingPanel.removeAll();
        List<AndroidView> views = mMappingData.get(mLayoutFiles.get(mSelectedLayoutFileIndex));

        viewIdMappingPanel.add(getMappingListTitleRow());
        viewIdMappingPanel.setPreferredSize(new Dimension(WIDTH, views.size() * 15));
        for (AndroidView androidView : views) {
            viewIdMappingPanel.add(getMappingListRow(androidView));
        }
        viewIdMappingPanel.revalidate();
        viewIdMappingPanel.repaint();
    }

    private JPanel getMappingListRow(AndroidView androidView) {

        JBPanel panel = new JBPanel();
        panel.setPreferredSize(new Dimension(WIDTH, 15));
        panel.setLayout(new GridLayout(1, 4));
        panel.add(getLabel(androidView.id));
        panel.add(getLabel(androidView.name));
        panel.add(getLabel(androidView.layout));
        JBCheckBox jbCheckBox = new JBCheckBox();
        jbCheckBox.setSelected(true);
        panel.add(new JBCheckBox());
        return panel;
    }

    private JPanel getMappingListTitleRow() {

        JBPanel panel = new JBPanel();
        panel.setPreferredSize(new Dimension(WIDTH, 15));
        panel.setLayout(new GridLayout(1, 4));
        panel.add(getTitleLabel("View Id"));
        panel.add(getTitleLabel("View Class"));
        panel.add(getTitleLabel("Field"));
        panel.add(getTitleLabel("Generate"));

        return panel;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        initContentPanel();
        initLayoutFileList();
        initViewIdMappingPanel();

        return contentPanel;
    }

    private static JBLabel getTitleLabel(String title) {
        JBLabel JBLabel = new JBLabel(title);
        JBLabel.setFont(JBLabel.getFont().deriveFont(Font.BOLD));
        return JBLabel;
    }

    private static JBLabel getLabel(String title) {
        JBLabel label = new JBLabel(title);
        label.setPreferredSize(new Dimension(WIDTH / 4, 15));
        return label;
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