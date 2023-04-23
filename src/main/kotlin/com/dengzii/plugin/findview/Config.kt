package com.dengzii.plugin.findview

import com.intellij.lang.Language

/**
 * <pre>
 * author : dengzi
 * e-mail : dengzii@foxmail.com
 * github : [...](https://github.com/dengzii)
 * time   : 2019/9/27
 * desc   :
</pre> *
 */
object Config {
    val JAVA = Language.findLanguageByID("JAVA")
    val KOTLIN = Language.findLanguageByID("kotlin")
    var METHOD_INIT_VIEW = "initView"
    var FIELD_NAME_PREFIX = "m"
}
