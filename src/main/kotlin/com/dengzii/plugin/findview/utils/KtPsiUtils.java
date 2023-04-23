package com.dengzii.plugin.findview.utils;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KtPsiUtils {

    @Nullable
    public static KtFunction getFirstFun(KtClass ktClass) {
        List<KtFunction> functions = getFunList(ktClass);
        if (functions.size() > 0) {
            return functions.get(0);
        }
        return null;
    }

    public static List<KtFunction> getFunList(KtClass ktClass) {

        List<KtFunction> result = new ArrayList<>();

        if (Objects.isNull(ktClass.getBody())) {
            return result;
        }
        PsiElement[] elements = ktClass.getBody().getChildren();
        for (PsiElement element : elements) {
            if (element instanceof KtFunction) {
                result.add(((KtFunction) element));
            }
        }
        return result;
    }
}
