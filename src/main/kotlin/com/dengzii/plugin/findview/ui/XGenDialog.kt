package com.dengzii.plugin.findview.ui

import com.dengzii.plugin.findview.ViewInfo
import com.dengzii.plugin.findview.tools.ui.ColumnInfo
import com.dengzii.plugin.findview.tools.ui.TableAdapter
import com.denzii.plugin.findview.ui.GenDialog
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.DefaultComboBoxModel

/**
 * <pre>
 * author : dengzi
 * e-mail : dengzii@foxmail.com
 * github : <a href="https://github.com/dengzii">...</a>
 * time   : 2023/4/23
 * desc   :
 * </pre>
 */
class XGenDialog(
        private val layoutFileIndex: Map<String, List<ViewInfo>>,
        private val callback: (vis: List<ViewInfo>) -> Unit
) : GenDialog() {

    private val cols = mutableListOf(
            ColumnInfo("ID"), ColumnInfo("View Class"), FieldNameCol(), EnableCol()
    )

    private val tableData = mutableListOf<MutableList<Any?>>()

    private val adapter = TableAdapter(tableData, cols as MutableList<ColumnInfo<Any>>)

    init {
        isModal = true
        defaultCloseOperation = DISPOSE_ON_CLOSE
        title = "Generate FindViewById Code"

        tableFieldMapping.fillsViewportHeight = true
        tableFieldMapping.showHorizontalLines = true
        tableFieldMapping.showVerticalLines = true

        layoutFileIndex.values.first().forEach {
            tableData.add(mutableListOf(it.id, it.type, it.field, it.enable))
        }
    }

    override fun dialogInit() {
        super.dialogInit()
        addWindowListener(object : WindowAdapter() {
            override fun windowOpened(e: WindowEvent?) {
                super.windowOpened(e)
                initData()
            }
        })
    }

    fun initData() {
        adapter.setup(tableFieldMapping)
        adapter.fireTableDataChanged()
        adapter.fireTableStructureChanged()

        tableFieldMapping.putClientProperty("terminateEditOnFocusLost", true)

        comboBoxLayoutRes.model = DefaultComboBoxModel(layoutFileIndex.keys.toTypedArray())
        comboBoxLayoutRes.addItemListener {
            tableData.clear()
            layoutFileIndex[comboBoxLayoutRes.selectedItem]?.forEach {
                tableData.add(mutableListOf(it.id, it.type, it.field, it.enable))
            }
            adapter.fireTableDataChanged()
        }
    }

    override fun onOK() {
        super.onOK()

        val result = mutableListOf<ViewInfo>()
        for (r in 0 until adapter.rowCount) {

            val id = adapter.getValueAt(r, 0) as String
            val type = adapter.getValueAt(r, 1) as String
            val fieldName = adapter.getValueAt(r, 2) as String
            val enable = adapter.getValueAt(r, 3) as Boolean

            val vi = ViewInfo(type, id, fieldName).apply {
                this.enable = enable
            }
            result.add(vi)
        }
        callback(result)
    }
}