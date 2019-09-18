package com.dengzii.plugin.findview;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class MainAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        Project project = anActionEvent.getProject();
        PsiFile psiFile = anActionEvent.getData(LangDataKeys.PSI_FILE);

        if (psiFile == null) return;

        print(psiFile.getLanguage());
        print(psiFile.getClass().toString());

        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);

        if (editor != null) editor.addEditorMouseListener(new EditorMouseListener() {
            @Override
            public void mousePressed(@NotNull EditorMouseEvent event) {

            }

            @Override
            public void mouseClicked(@NotNull EditorMouseEvent event) {

                if (event.getSource() instanceof EditorImpl){
                    EditorImpl editor1 = ((EditorImpl) event.getSource());
                    print("state ", editor1.dumpState());
                }
                print("area = ", event.getArea().toString());
            }

            @Override
            public void mouseReleased(@NotNull EditorMouseEvent event) {
                TextAttributes attributes = new TextAttributes();
                attributes.setForegroundColor(JBColor.GREEN);
                editor.getMarkupModel().addLineHighlighter(0,3, attributes);
                print("selectedText = ", editor.getSelectionModel().getSelectedText());
            }

            @Override
            public void mouseEntered(@NotNull EditorMouseEvent event) {

            }

            @Override
            public void mouseExited(@NotNull EditorMouseEvent event) {

            }
        });

        if (psiFile instanceof PsiJavaFile) {
            PsiJavaFile psiJavaFile = ((PsiJavaFile) psiFile);

            if (isImportRClass((PsiJavaFileImpl) psiJavaFile)) {
                print("find R");
//                findLayout((PsiJavaFileImpl) psiJavaFile);
                psiJavaFile.accept(visitor);
            }
            print("package ", ((PsiJavaFile) psiFile).getPackageName());
            print("psiJavaFile", psiJavaFile);
        }

        print(project.getBasePath());
        print(project.getName());
        print(project.getProjectFilePath());
        print(project.getName());
        print(project.getPresentableUrl());
    }

    private boolean isImportRClass(PsiJavaFileImpl psiJavaFile) {

        PsiImportList importList = psiJavaFile.getImportList();
        if (importList != null) {
            for (PsiImportStatementBase allImportStatement : importList.getAllImportStatements()) {
                if (allImportStatement != null && allImportStatement.getText().endsWith(".R;")) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> findLayout(PsiJavaFileImpl psiJavaFile) {
        psiJavaFile.accept(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);
            }

            @Override
            public void visitPlainText(PsiPlainText content) {
                super.visitPlainText(content);
                print(content.getText());
            }
        });
        return null;
    }

    private void visitor(int level, PsiElement element) {
        element.accept(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);
                print(level, "->", element);
                visitor(level + 1, element);
            }

            @Override
            public void visitPlainText(PsiPlainText content) {
                super.visitPlainText(content);
                print(content.getText());
            }
        });
    }

    private static void print(Object... objects) {
        for (Object o : objects) {
            System.out.print(o.toString() + " ");
        }
        System.out.println("");
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }

    private PsiElementVisitor visitor = new PsiElementVisitor() {
        @Override
        public void visitElement(PsiElement element) {
            super.visitElement(element);
            print(element);
            element.acceptChildren(this);
        }
    };
}
