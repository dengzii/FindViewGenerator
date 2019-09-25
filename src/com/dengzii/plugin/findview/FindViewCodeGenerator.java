package com.dengzii.plugin.findview;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.util.ThrowableRunnable;

import java.util.List;

public class FindViewCodeGenerator implements ThrowableRunnable<RuntimeException> {

    private static final String STATEMENT_FIELD = "private %s %s;";
    private static final String STATEMENT_FIND_VIEW = "%s = findViewById<%s>(R.id.%s);";

    private PsiClass psiClass;
    private Project project;
    private List<AndroidView> androidViews;

    public FindViewCodeGenerator(PsiClass psiClass, Project project, List<AndroidView> androidViews) {
        this.psiClass = psiClass;
        this.project = project;
        this.androidViews = androidViews;
    }

    @Override
    public void run() throws RuntimeException {

        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
        PsiMethod method = psiClass.findMethodsByName("onCreate", false)[0];
        for (AndroidView androidView : androidViews) {
            String statement = String.format(STATEMENT_FIELD, androidView.getType(), androidView.getMappingField());
            psiClass.add(factory.createFieldFromText(statement, null));
            String findStatement = String.format(STATEMENT_FIND_VIEW,
                    androidView.getMappingField(),
                    androidView.getType(), androidView.getId());
            method.add(factory.createStatementFromText(findStatement, null));
        }
    }
}
