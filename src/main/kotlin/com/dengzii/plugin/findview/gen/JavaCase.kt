package com.dengzii.plugin.findview.gen;

import com.dengzii.plugin.findview.Config;
import com.dengzii.plugin.findview.ViewInfo;
import com.dengzii.plugin.findview.utils.PsiClassUtils;
import com.intellij.psi.*;

import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * author : dengzi
 * e-mail : dengzii@foxmail.com
 * github : <a href="https://github.com/dengzii">...</a>
 * time   : 2019/9/27
 * desc   :
 * </pre>
 */
public class JavaCase extends BaseCase {


    private static final String STATEMENT_FIELD = "private %s %s;";
    private static final String STATEMENT_FIND_VIEW = "%s = findViewById(R.id.%s);\n";

//    private static final InsertPlace mFieldInsertPlace = InsertPlace.FIRST; // TODO: 2019/9/30 add insert place support
//    private static final InsertPlace mFindViewInsertPlace = InsertPlace.FIRST; // TODO: 2019/9/30

    @Override
    void dispose(PsiFile psiElement, List<ViewInfo> viewInfos) {

        if (!psiElement.getLanguage().is(Config.JAVA)) {
            next(psiElement, viewInfos);
            return;
        }

        PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiElement.getProject());
        PsiClass psiClass = getPsiClass(psiElement);
        if (Objects.isNull(psiClass)) {
            return;
        }
        PsiMethod initViewMethod = genInitViewMethod(factory, psiClass);

        for (ViewInfo viewInfo : viewInfos) {
            if (!viewInfo.isGenerate()) continue;
            psiClass.add(genViewDeclareField(factory, viewInfo));
            if (!Objects.isNull(initViewMethod.getBody())) {
                initViewMethod.getBody().add(genFindViewStatement(factory, viewInfo));
            }
        }
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

    private PsiMethod genInitViewMethod(PsiElementFactory factory, PsiClass psiClass) {

        PsiMethod method1 = PsiClassUtils.getMethod(psiClass, Config.METHOD_INIT_VIEW);
        if (Objects.isNull(method1)) {
            method1 = factory.createMethod(Config.METHOD_INIT_VIEW, PsiTypes.voidType());
            psiClass.add(method1);
        }
        method1.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
        return method1;
    }

    private PsiStatement genFindViewStatement(PsiElementFactory factory, ViewInfo viewInfo) {

        String findStatement = String.format(STATEMENT_FIND_VIEW, viewInfo.getType().trim(), viewInfo.getId());
        return factory.createStatementFromText(findStatement, null);
    }
}
