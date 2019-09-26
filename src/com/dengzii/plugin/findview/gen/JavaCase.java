package com.dengzii.plugin.findview.gen;

import com.dengzii.plugin.findview.ViewInfo;
import com.intellij.lang.Language;
import com.intellij.psi.*;

import java.util.List;

public class JavaCase extends BaseCase {

    private static final Language JAVA = Language.findLanguageByID("JAVA");

    private static final String STATEMENT_FIELD = "private %s %s;";
    private static final String METHOD_INIT_VIEW = "initView";
    private static final String STATEMENT_FIND_VIEW = "%s = findViewById(R.id.%s);\n";

    @Override
    void dispose(PsiFile psiElement, List<ViewInfo> viewInfos) {

        if (!psiElement.getLanguage().is(JAVA)) {
            next(psiElement, viewInfos);
            return;
        }

        PsiElementFactory factory = PsiElementFactory.getInstance(psiElement.getProject());
        PsiClass psiClass = getPsiClass(psiElement);
        PsiMethod initViewMethod = genInitViewMethod(factory);

        for (ViewInfo viewInfo : viewInfos) {
            if (!viewInfo.isGenerate()) continue;
            psiClass.add(genViewDeclareField(factory, viewInfo));
            if (initViewMethod.getBody() != null) {
                initViewMethod.getBody().add(genFindViewStatement(factory, viewInfo));
            }
        }

        psiClass.add(initViewMethod);
    }

    private PsiClass getPsiClass(PsiFile file) {

        PsiElement[] psiElements = file.getChildren();
        for (PsiElement element : psiElements) {
            if (element instanceof PsiClass) {
                return ((PsiClass) element);
            }
        }
        return null;
    }

    private PsiField genViewDeclareField(PsiElementFactory factory, ViewInfo viewInfo) {
        String statement = String.format(STATEMENT_FIELD, viewInfo.getType(), viewInfo.getMappingField());
        return factory.createFieldFromText(statement, null);
    }

    private PsiMethod genInitViewMethod(PsiElementFactory factory) {

        PsiMethod method1 = factory.createMethod(METHOD_INIT_VIEW, PsiType.VOID);
        method1.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
        return method1;
    }

    private PsiStatement genFindViewStatement(PsiElementFactory factory, ViewInfo viewInfo) {

        String findStatement = String.format(STATEMENT_FIND_VIEW, viewInfo.getType().trim(), viewInfo.getId());
        return factory.createStatementFromText(findStatement, null);
    }
}
