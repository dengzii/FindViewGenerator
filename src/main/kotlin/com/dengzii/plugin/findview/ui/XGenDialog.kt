package com.dengzii.plugin.findview.ui

import com.dengzii.plugin.findview.ViewInfo
import com.dengzii.plugin.findview.tools.ui.ColumnInfo
import com.dengzii.plugin.findview.tools.ui.TableAdapter
import com.dengzii.plugin.findview.tools.ui.showAtCenterScreen

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
//        private val project: Project,
        private val layoutFileIndex: List<ViewInfo>,
) : GenDialog() {

    init {
        isModal = true
        defaultCloseOperation = DISPOSE_ON_CLOSE
        title = "Generate FindViewById Code"

        tableFieldMapping.fillsViewportHeight = true
        tableFieldMapping.showHorizontalLines = true
        tableFieldMapping.showVerticalLines = true
        tableFieldMapping.model = TableAdapter(mutableListOf(
                mutableListOf("1", "2", "3", "4")),
                mutableListOf(
                        ColumnInfo("ID"),
                        ColumnInfo("View Class"),
                        ColumnInfo("Field Name"),
                        ColumnInfo("Enable")
                ))
        tableFieldMapping.putClientProperty("terminateEditOnFocusLost", true)
    }

    override fun dialogInit() {
        super.dialogInit()


    }

    fun initData() {
//        tableFieldMapping.model = TableAdapter(mutableListOf(
//                mutableListOf("1", "2", "3", "4")),
//                mutableListOf(
//                        ColumnInfo("ID"),
//                        ColumnInfo("View Class"),
//                        ColumnInfo("Field Name"),
//                        ColumnInfo("Enable")
//                ))
    }
}

fun main() {
    val dialog = XGenDialog(emptyList())
    dialog.showAtCenterScreen()
    dialog.initData()
}