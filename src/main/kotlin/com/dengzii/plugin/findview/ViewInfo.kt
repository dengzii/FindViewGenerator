package com.dengzii.plugin.findview

import java.util.*

/**
 * <pre>
 * author : dengzi
 * e-mail : dengzii@foxmail.com
 * github : [...](https://github.com/dengzii)
 * time   : 2019/9/27
 * desc   :
</pre> *
 */
class ViewInfo(val type: String, val id: String, var field: String = "", var enable: Boolean = true) {

    var fullType = ""
    var typePkg = ""

    fun genMappingField() {
        idToCamelCaseField()
    }

    private fun idToCamelCaseField(prefix: String = "m") {
        val sb = StringBuilder(prefix)
        val split = id.split("_")
        split.forEachIndexed { index, s ->
            if (index == 0 && prefix.isEmpty()) {
                sb.append(s)
            } else {
                sb.append(s.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
            }
        }
        field = sb.toString()
    }

    override fun toString(): String {
        return "ViewInfo(type='$type', id='$id', mappingField=$field, enable=$enable)"
    }


}
