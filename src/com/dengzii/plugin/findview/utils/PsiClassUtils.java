package com.dengzii.plugin.findview.utils;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

/**
 * <pre>
 * author : dengzi
 * e-mail : denua@foxmail.com
 * github : https://github.com/MrDenua
 * time   : 2019/9/30
 * desc   :
 * </pre>
 */
public class PsiClassUtils {

    public static PsiMethod getMethod(PsiClass psiClass, String methodName) {
        PsiMethod[] methods = getMethods(psiClass);
        for (PsiMethod method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    public static PsiMethod[] getMethods(PsiClass psiClass) {
        return psiClass.getMethods();
    }
}
