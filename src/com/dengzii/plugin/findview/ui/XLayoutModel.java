package com.dengzii.plugin.findview.ui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

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
