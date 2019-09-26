package com.dengzii.plugin.findview.gen;

import com.dengzii.plugin.findview.ViewInfo;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.ThrowableRunnable;

import java.util.List;

public class FindViewCodeWriter implements ThrowableRunnable<RuntimeException> {

    private static final String STATEMENT_FIELD = "private %s %s;";
    private static final String METHOD_INIT_VIEW = "initView";
    private static final String STATEMENT_FIND_VIEW = "%s = findViewById(R.id.%s);\n";

    private PsiClass psiClass;
    private Project project;
    private List<ViewInfo> viewInfos;
    private PsiElementFactory factory;

    public FindViewCodeWriter(PsiClass psiClass, Project project, List<ViewInfo> viewInfos) {
        this.psiClass = psiClass;
        this.project = project;
        this.viewInfos = viewInfos;
        factory = JavaPsiFacade.getElementFactory(project);
    }

    @Override
    public void run() throws RuntimeException {

        PsiMethod initViewMethod = genInitViewMethod();
        for (ViewInfo viewInfo : viewInfos) {
            if (!viewInfo.isGenerate()) continue;
            psiClass.add(genViewDeclareField(viewInfo));
            if (initViewMethod.getBody() != null) {
                initViewMethod.getBody().add(genFindViewStatement(viewInfo));
            }
        }

        psiClass.add(initViewMethod);
    }

    private PsiField genViewDeclareField(ViewInfo viewInfo) {
        String statement = String.format(STATEMENT_FIELD, viewInfo.getType(), viewInfo.getMappingField());
        return factory.createFieldFromText(statement, null);
    }

    private PsiMethod genInitViewMethod() {

        PsiMethod method1 = factory.createMethod(METHOD_INIT_VIEW, PsiType.VOID);
        method1.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
        return method1;
    }

    private PsiStatement genFindViewStatement(ViewInfo viewInfo) {

        String findStatement = String.format(STATEMENT_FIND_VIEW, viewInfo.getType().trim(), viewInfo.getId());
        return factory.createStatementFromText(findStatement, null);
    }
}
