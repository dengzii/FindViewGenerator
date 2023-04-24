package com.dengzii.plugin.findview.utils

import com.dengzii.plugin.findview.ViewInfo
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import org.jetbrains.kotlin.idea.core.util.toPsiFile

object PsiFileUtils {

    private const val LAYOUT_REF_PREFIX = "R.layout."
    private const val LAYOUT_FILE_SUFFIX = ".xml"
    private const val ANDROID_ID_ATTR_NAME = "android:id"
    private const val ANDROID_ID_PREFIX = "@+id/"

    fun getViewInfoFromPsiFile(psiFile: PsiFile, project: Project): Map<String, List<ViewInfo>> {
        val layoutRefExpr = findLayoutStatement(psiFile)
        val layoutPsi: MutableList<PsiFile> = ArrayList()
        for (layoutName in layoutRefExpr) {
            val fileName = layoutName.substring(LAYOUT_REF_PREFIX.length) + LAYOUT_FILE_SUFFIX
            val psiFiles = FilenameIndex.getVirtualFilesByName(fileName, GlobalSearchScope.allScope(project))
            layoutPsi.addAll(psiFiles.mapNotNull { it.toPsiFile(project) })
        }
        val layoutViews: MutableMap<String, List<ViewInfo>> = HashMap()
        for (p in layoutPsi) {
            if (p is XmlFile) {
                layoutViews[p.getName()] = getAndroidViewInfoFrom(p)
            }
        }
        return layoutViews
    }

    private fun getAndroidViewInfoFrom(xmlFile: XmlFile): List<ViewInfo> {
        val result: MutableList<ViewInfo> = ArrayList()
        visitLayoutXmlFile(xmlFile, xmlFile, result)
        return result
    }

    private fun findLayoutStatement(psiFile: PsiFile): List<String> {
        val list: MutableList<String> = ArrayList()
        visitStatement(psiFile, list)
        return list
    }

    private fun visitLayoutXmlFile(psiFile: PsiFile, psiElement: PsiElement, result: MutableList<ViewInfo>) {
        psiElement.acceptChildren(object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)
                if (element is XmlTag) {
                    val id = element.getAttributeValue(ANDROID_ID_ATTR_NAME)?.removePrefix(ANDROID_ID_PREFIX)
                    val type = if (element.name.contains(".")) element.name.substring(element.name.lastIndexOf(".") + 1) else element.name
                    if (id != null) {
                        val viewInfo = ViewInfo(type, id)
                        viewInfo.fullType = element.name
                        viewInfo.genMappingField()
                        result.add(viewInfo)
                    }
                }
                visitLayoutXmlFile(psiFile, element, result)
            }
        })
    }

    private fun visitStatement(psiElement: PsiElement, result: MutableList<String>) {
        psiElement.acceptChildren(object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)
                if (isReferenceExpression(element)) {
                    val expression = element.text
                    if (expression.startsWith(LAYOUT_REF_PREFIX)) {
                        result.add(expression)
                    }
                }
                visitStatement(element, result)
            }
        })
    }

    private fun isReferenceExpression(element: PsiElement): Boolean {
        return isKotlinExpression(element) || isJavaExpression(element)
    }

    private fun isKotlinExpression(element: PsiElement): Boolean {
        return element.toString().contains("DOT_QUALIFIED_EXPRESSION")
    }

    private fun isJavaExpression(element: PsiElement): Boolean {
        return element is PsiReferenceExpression
    }
}
