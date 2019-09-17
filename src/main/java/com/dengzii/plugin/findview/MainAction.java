package com.dengzii.plugin.findview;

import com.dengzii.plugin.findview.utils.XLog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
//import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MainAction extends AnAction {

    private static final String TAG = "MainAction";

    private PsiClass mClass;
    private PsiJavaFile mPsiJavaFile;
    private Project mProject;
    private PsiFile mPsiFile;

//    public MainAction() {
//        super(null);
//    }
//
//    protected MainAction(CodeInsightActionHandler handler) {
//        super(handler);
//    }

    @Override
    public void actionPerformed(AnActionEvent e) {

        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        this.mProject = e.getProject();
        this.mPsiFile = e.getData(LangDataKeys.PSI_FILE);
        this.mClass = getTargetClass(editor, mPsiFile);

        mPsiJavaFile = ((com.intellij.psi.impl.source.PsiJavaFileImpl) mPsiFile);

        System.out.println(mPsiJavaFile);

        Document document = editor.getDocument();
        XLog.info(TAG, "className: " + mClass.getName());
        XLog.info(TAG, "superClass: " + Arrays.toString(mClass.getSupers()));
        XLog.info(TAG, "fields: " + Arrays.toString(mClass.getAllFields()));

        document.addDocumentListener(new DocumentListener() {
            @Override
            public void beforeDocumentChange(@NotNull DocumentEvent event) {
                XLog.info(TAG, "beforeDocumentChange: " + event.getOldFragment());
            }

            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                XLog.info(TAG, "documentChanged: " + event.getNewFragment());
            }
        });
    }

    private PsiClass getTargetClass(Editor editor, PsiFile file) {
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);
        if (element == null) {
            return null;
        } else {
            PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            return target instanceof SyntheticElement ? null : target;
        }
    }
}
