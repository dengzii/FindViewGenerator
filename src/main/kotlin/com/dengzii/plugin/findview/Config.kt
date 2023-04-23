package com.dengzii.plugin.findview;

import com.intellij.lang.Language;

/**
 * <pre>
 * author : dengzi
 * e-mail : dengzii@foxmail.com
 * github : <a href="https://github.com/dengzii">...</a>
 * time   : 2019/9/27
 * desc   :
 * </pre>
 */
@SuppressWarnings("WeakerAccess")
public class Config {

    public static final Language JAVA = Language.findLanguageByID("JAVA");
    public static final Language KOTLIN = Language.findLanguageByID("kotlin");
    public static String METHOD_INIT_VIEW = "initView";
    public static String FIELD_NAME_PREFIX = "m";

}
