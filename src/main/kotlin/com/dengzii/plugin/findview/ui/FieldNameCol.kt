package com.dengzii.plugin.findview.ui

import com.dengzii.plugin.findview.tools.ui.ColumnInfo
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import java.awt.Component

class FieldNameCol : ColumnInfo<String>("Field Name") {

    override fun getEditorValue(component: Component, oldValue: String?, row: Int, col: Int): String {
        return (component as? JBTextField?)?.text ?: ""
    }

    override fun getEditComponent(item: String?, row: Int, col: Int): Component {
        return JBTextField(item ?: "")
    }

    override fun isCellEditable(item: String?): Boolean {
        return true
    }

    override fun getRendererComponent(item: String?, row: Int, col: Int): Component {
        return JBLabel(item ?: "")
    }

}