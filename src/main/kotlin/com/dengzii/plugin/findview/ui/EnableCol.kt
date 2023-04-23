package com.dengzii.plugin.findview.ui

import com.dengzii.plugin.findview.tools.ui.ColumnInfo
import com.intellij.ui.components.JBCheckBox
import java.awt.Component

class EnableCol : ColumnInfo<Boolean>("Enable") {

    override fun getEditorValue(component: Component, oldValue: Boolean?, row: Int, col: Int): Boolean {
        return (component as? JBCheckBox?)?.isSelected == true
    }

    override fun getEditComponent(item: Boolean?, row: Int, col: Int): Component {
        return JBCheckBox().apply { isSelected = item == true }
    }

    override fun isCellEditable(item: Boolean?): Boolean {
        return true
    }

    override fun getRendererComponent(item: Boolean?, row: Int, col: Int): Component {
        return JBCheckBox().apply { isSelected = item == true }
    }

}