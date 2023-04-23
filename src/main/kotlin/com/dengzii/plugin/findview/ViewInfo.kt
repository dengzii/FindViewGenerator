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

    var typePkg = ""

    fun genMappingField() {
        val builder = StringBuilder(Config.FIELD_NAME_PREFIX)
        if (id.contains("_")) {
            val split = id.lowercase(Locale.getDefault()).split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (s in split) {
                if (s.isNotEmpty()) {
                    val c = s.substring(0, 1).uppercase(Locale.getDefault())
                    builder.append(c).append(s.substring(1))
                }
            }
        } else {
            val c = id.substring(0, 1).uppercase(Locale.getDefault())
            builder.append(c).append(id.substring(1))
        }
        field = builder.toString()
    }

    override fun toString(): String {
        return "ViewInfo(type='$type', id='$id', mappingField=$field, enable=$enable)"
    }


}
