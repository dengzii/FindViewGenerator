package com.dengzii.plugin.findview.gen;

import com.dengzii.plugin.findview.ViewInfo;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtClassBody;

import java.util.List;

public class KotlinCase extends BaseCase {

    private static final Language KOTLIN = Language.findLanguageByID("kotlin");

    @Override
    void dispose(PsiFile psiElement, List<ViewInfo> viewInfos) {

        if (!psiElement.getLanguage().is(KOTLIN)) {
            next(psiElement, viewInfos);
            return;
        }

        KtClass ktClass =getKtClass(psiElement);
        if (ktClass == null){
            return;
        }

        PsiElementFactory factory = PsiElementFactory.getInstance(psiElement.getProject());

        if (ktClass.getBody() != null) {
            KtClassBody body = ktClass.getBody();
            for (ViewInfo viewInfo : viewInfos) {
                String statement = String.format("private val %s by lazy { findViewById<%s>(R.id.%s) }",
                        viewInfo.getMappingField(), viewInfo.getType(), viewInfo.getId());
                body.add(factory.createStatementFromText(statement, null));
            }
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
