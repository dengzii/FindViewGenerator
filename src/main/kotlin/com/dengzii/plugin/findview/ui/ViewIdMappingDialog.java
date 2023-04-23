package com.dengzii.plugin.findview.ui;

import com.dengzii.plugin.findview.ViewInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * author : dengzi
 * e-mail : dengzii@foxmail.com
 * github : <a href="https://github.com/dengzii">...</a>
 * time   : 2019/9/27
 * desc   :
 * </pre>
 */
public class ViewIdMappingDialog extends DialogWrapper {

    private static final int NAMED_BY_ID = 467;
    private static final int NAMED_BY_CLASS_AND_ID = 245;

    private static final int CONTENT_WIDTH = 600;
    private static final int MAPPING_ROW_HEIGHT = 26;
    private static final int MAPPING_COL_WIDTH = 600 / 4;

    private JBPanel mContentPanel;
    private JBList<String> mLayoutFileList;
    private JBPanel mViewIdMappingPanel;
    private List<JBTextField> mFieldName = new ArrayList<>();
    private List<JBCheckBox> mGenerateCheckoutBox = new ArrayList<>();

    private int mSelectedLayoutFileIndex = 0;
    private List<String> mLayoutFiles = new ArrayList<>();
    private Map<String, List<ViewInfo>> mFileMappingData = new HashMap<>();
    private List<ViewInfo> mCurrent = new ArrayList<>();

    public ViewIdMappingDialog(@Nullable Project project, Map<String, List<ViewInfo>> layoutFileIndex) {
        super(project);
        if (layoutFileIndex != null && !layoutFileIndex.isEmpty()) {
            mFileMappingData.putAll(layoutFileIndex);
            mLayoutFiles.addAll(mFileMappingData.keySet());
        }
        init();
        setTitle("Generate Find View");
    }

    public List<ViewInfo> getResult() {

        if (mFieldName.size() != mCurrent.size() || mGenerateCheckoutBox.size() != mCurrent.size()) {
            return mCurrent;
        }
        for (int i = 0; i < mCurrent.size(); i++) {
            mCurrent.get(i).setMappingField(mFieldName.get(i).getText());
            mCurrent.get(i).setGenerate(mGenerateCheckoutBox.get(i).isSelected());
        }
        return mCurrent;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        initContentPanel();
        initLayoutFileList();
        initViewIdMappingPanel();
        return mContentPanel;
    }

    private void initContentPanel() {

        BorderLayout borderLayout = new BorderLayout(10, 10);

        mContentPanel = new JBPanel(borderLayout);
        mContentPanel.setPreferredSize(new Dimension(CONTENT_WIDTH + 60, 400));
    }

    private void initLayoutFileList() {

        JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.setPreferredSize(new Dimension(CONTENT_WIDTH, 70));

        mLayoutFileList = new JBList<String>(new XLayoutModel(mFileMappingData.keySet()));
        mLayoutFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane.setViewportView(mLayoutFileList);
        scrollPane.setBorder(new BorderUIResource.TitledBorderUIResource("Layout Resource"));
        mContentPanel.add(scrollPane, BorderLayout.NORTH);

        mLayoutFileList.addListSelectionListener(e -> {
            mSelectedLayoutFileIndex = mLayoutFileList.getSelectedIndex();
            refreshViewIdMappingList();
        });
    }

    private void initViewIdMappingPanel() {

        JBScrollPane viewIdMappingScroll = new JBScrollPane();
        viewIdMappingScroll.setPreferredSize(new Dimension(CONTENT_WIDTH, 300));

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        mViewIdMappingPanel = new JBPanel(flowLayout);
        mViewIdMappingPanel.setBorder(new BorderUIResource.TitledBorderUIResource("View Id Mapping"));

        viewIdMappingScroll.setViewportView(mViewIdMappingPanel);
        mContentPanel.add(viewIdMappingScroll);
        refreshViewIdMappingList();
    }

    private void refreshViewIdMappingList() {

        mViewIdMappingPanel.removeAll();
        mCurrent.clear();
        if (!mFileMappingData.isEmpty() && mLayoutFiles.size() > 0) {
            mCurrent.addAll(mFileMappingData.get(mLayoutFiles.get(mSelectedLayoutFileIndex)));
        }

        mViewIdMappingPanel.add(getMappingListTitleRow());
        mViewIdMappingPanel.setPreferredSize(
                new Dimension(CONTENT_WIDTH, mCurrent.size() * (MAPPING_ROW_HEIGHT + 8)));
        mFieldName.clear();
        mGenerateCheckoutBox.clear();
        for (ViewInfo viewInfo : mCurrent) {
            mViewIdMappingPanel.add(getMappingListRow(viewInfo));
        }
        mViewIdMappingPanel.revalidate();
        mViewIdMappingPanel.repaint();
    }

    private JPanel getMappingListRow(ViewInfo viewInfo) {

        JBPanel panel = getMappingListRowPanel();
        panel.setLayout(new GridLayout(1, 4));
        panel.add(getLabel(viewInfo.getId()));
        panel.add(getLabel(viewInfo.getType()));

        panel.add(getMappingTextField(viewInfo));

        JBCheckBox jbCheckBox = new JBCheckBox();
        jbCheckBox.setSelected(true);
        mGenerateCheckoutBox.add(jbCheckBox);
        panel.add(jbCheckBox);
        return panel;
    }

    private JBPanel getMappingListTitleRow() {

        JBPanel panel = getMappingListRowPanel();
        panel.setLayout(new GridLayout(1, 4));
        panel.add(getTitleLabel("View Id"));
        panel.add(getTitleLabel("Type"));
        panel.add(getTitleLabel("Field"));
        panel.add(getTitleLabel("Enable"));

        return panel;
    }

    private JBPanel getMappingListRowPanel() {
        JBPanel panel = new JBPanel();
        panel.setPreferredSize(new Dimension(CONTENT_WIDTH, MAPPING_ROW_HEIGHT));
        return panel;
    }

    private JBLabel getTitleLabel(String title) {
        JBLabel label = new JBLabel(title);
        label.setToolTipText(title);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setPreferredSize(new Dimension(MAPPING_COL_WIDTH, MAPPING_ROW_HEIGHT));
        return label;
    }

    private JBLabel getLabel(String title) {
        JBLabel label = new JBLabel(title);
        label.setToolTipText(title);
        label.setPreferredSize(new Dimension(MAPPING_COL_WIDTH, MAPPING_ROW_HEIGHT));
        return label;
    }

    private JBTextField getMappingTextField(ViewInfo viewInfo) {
        JBTextField jbTextField = new JBTextField(viewInfo.getMappingField());
        jbTextField.setToolTipText(viewInfo.getMappingField());
        jbTextField.setPreferredSize(new Dimension(MAPPING_COL_WIDTH, MAPPING_ROW_HEIGHT));
        jbTextField.setBorder(new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1));
        jbTextField.setHorizontalAlignment(SwingConstants.LEFT);
        mFieldName.add(jbTextField);
        return jbTextField;
    }
}

