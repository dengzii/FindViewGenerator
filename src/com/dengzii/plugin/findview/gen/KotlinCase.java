package com.dengzii.plugin.findview.gen;

import com.dengzii.plugin.findview.Config;
import com.dengzii.plugin.findview.ViewInfo;
import com.dengzii.plugin.findview.utils.KtPsiUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.kotlin.psi.*;

import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * author : dengzi
 * e-mail : denua@foxmail.com
 * github : https://github.com/MrDenua
 * time   : 2019/9/27
 * desc   :
 * </pre>
 */
@SuppressWarnings({"ConstantConditions", "FieldCanBeLocal"})
public class KotlinCase extends BaseCase {

    private static String STATEMENT_LAZY_INIT_VIEW = " %s val %s by lazy  { findViwById<%s>(R.id.%s) }";
    private static String FUN_INIT_VIEW = "private fun %s() {\n\n}";
    private static String MODIFIER_INIT_VIEW_PROPERTY = "private";
    private static boolean INIT_VIEW_BY_LAZY = true;

    private InsertPlace mFieldPlace = InsertPlace.FIRST;
    private InsertPlace mMethodPlace = InsertPlace.FIRST;

    @Override
    void dispose(PsiFile psiElement, List<ViewInfo> viewInfos) {

        if (!psiElement.getLanguage().is(Config.KOTLIN)) {
            next(psiElement, viewInfos);
            return;
        }

        KtClass ktClass = getKtClass(psiElement);
        if (Objects.isNull(ktClass)) {
            return;
        }
        KtPsiFactory ktPsiFactory = KtPsiFactoryKt.KtPsiFactory(psiElement.getProject());

        checkAndCreateClassBody(ktClass, ktPsiFactory);

        if (!INIT_VIEW_BY_LAZY) {
            insertInitViewKtFun(ktClass, ktPsiFactory);
        }

        for (ViewInfo viewInfo : viewInfos) {
            insertViewField(viewInfo, ktPsiFactory, ktClass);
        }
    }

    private void insertViewField(ViewInfo viewInfo, KtPsiFactory ktPsiFactory, KtClass ktClass) {

        KtClassBody body = ktClass.getBody();
        PsiElement lBrace = body.getLBrace();
        String lazyViewProperty = String.format(STATEMENT_LAZY_INIT_VIEW,
                MODIFIER_INIT_VIEW_PROPERTY,
                viewInfo.getMappingField(),
                viewInfo.getType(),
                viewInfo.getId());
        KtProperty ktProperty = ktPsiFactory.createProperty(lazyViewProperty);
        body.addAfter(ktProperty, lBrace);
    }

    private void insertInitViewKtFun(KtClass ktClass, KtPsiFactory factory) {

        PsiElement firstFun = KtPsiUtils.getFirstFun(ktClass);
        KtClassBody ktClassBody = ktClass.getBody();
        PsiElement rBrace = ktClassBody.getRBrace();
        KtFunction initViewFun = factory.createFunction(String.format(FUN_INIT_VIEW, Config.METHOD_INIT_VIEW));

        ktClassBody.addBefore(initViewFun, Objects.isNull(firstFun) ? rBrace : firstFun);
    }

    private void checkAndCreateClassBody(KtClass ktClass, KtPsiFactory ktPsiFactory) {
        if (Objects.isNull(ktClass.getBody())) {
            ktClass.add(ktPsiFactory.createEmptyClassBody());
        }
    }

    private KtClass getKtClass(PsiFile file) {

        PsiElement[] psiElements = file.getChildren();
        for (PsiElement element : psiElements) {
            if (element instanceof KtClass) {
                return ((KtClass) element);
            }
        }
        return null;
    }
}
