package com.dengzii.plugin.findview.tools.ui

import javax.swing.JDialog


fun JDialog.packAndShow() {
    pack()
    isVisible = true
}

fun JDialog.hideAndDispose() {
    isVisible = false
    dispose()
}

fun JDialog.showAtCenterScreen() {
    val screen = toolkit.screenSize
    val x = screen.width / 2 - preferredSize.width / 2
    val y = screen.height / 2 - preferredSize.height / 2
    setLocation(x, y)
    packAndShow()
}