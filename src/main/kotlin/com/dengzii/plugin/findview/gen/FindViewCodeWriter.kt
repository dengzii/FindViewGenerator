package com.dengzii.plugin.findview.gen

import com.intellij.util.ThrowableRunnable

/**
 * <pre>
 * author : dengzi
 * e-mail : dengzii@foxmail.com
 * github : [...](https://github.com/dengzii)
 * time   : 2019/9/27
 * desc   :
</pre> *
 */
class FindViewCodeWriter(private val genConfig: GenConfig) : ThrowableRunnable<RuntimeException?> {
    @Throws(RuntimeException::class)
    override fun run() {
        val javaCase = JavaCase()
        javaCase.setNext(KotlinCase())
        javaCase.dispose(genConfig)
    }
}
