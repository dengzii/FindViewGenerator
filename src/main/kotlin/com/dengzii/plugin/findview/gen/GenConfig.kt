package com.dengzii.plugin.findview.gen

import com.dengzii.plugin.findview.ViewInfo
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

data class GenConfig(
    var viewInfo: List<ViewInfo>,
    val psiFile: PsiFile,
    val editor: Editor,
    var insertAt: InsertPlace = InsertPlace.FIRST,
    var bindBy: BindBy = BindBy.LazyPropertyFindViewById,
    var autoImport: Boolean = true,
)