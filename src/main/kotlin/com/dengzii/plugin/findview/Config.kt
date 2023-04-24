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


    val AndroidViewClasses = mapOf(
        Pair("View", "android.view.View"),
        Pair("ViewGroup", "android.view.ViewGroup"),
        Pair("FrameLayout", "android.widget.FrameLayout"),
        Pair("LinearLayout", "android.widget.LinearLayout"),
        Pair("GridLayout", "android.widget.GridLayout"),
        Pair("ListView", "android.widget.ListView"),
        Pair("RelativeLayout", "android.widget.RelativeLayout"),
        Pair("ScrollView", "android.widget.ScrollView"),
        Pair("HorizontalScrollView", "android.widget.HorizontalScrollView"),
        Pair("ConstraintLayout", "androidx.constraintlayout.widget.ConstraintLayout"),
        Pair("AppBarLayout", "com.google.android.material.appbar.AppBarLayout"),
        Pair("CollapsingToolbarLayout", "com.google.android.material.appbar.CollapsingToolbarLayout"),
        Pair("CoordinatorLayout", "androidx.coordinatorlayout.widget.CoordinatorLayout"),
        Pair("TabLayout", "com.google.android.material.tabs.TabLayout"),
        Pair("ViewPager", "androidx.viewpager.widget.ViewPager"),
        Pair("ViewPager2", "androidx.viewpager2.widget.ViewPager2"),
        Pair("RecyclerView", "androidx.recyclerview.widget.RecyclerView"),
        Pair("CardView", "androidx.cardview.widget.CardView"),
        Pair("NestedScrollView", "androidx.core.widget.NestedScrollView"),

        Pair("TextView", "android.widget.TextView"),
        Pair("EditText", "android.widget.EditText"),
        Pair("Button", "android.widget.Button"),
        Pair("ImageView", "android.widget.ImageView"),
        Pair("ImageButton", "android.widget.ImageButton"),
        Pair("CheckBox", "android.widget.CheckBox"),
        Pair("RadioButton", "android.widget.RadioButton"),
        Pair("RadioGroup", "android.widget.RadioGroup"),
        Pair("CheckedTextView", "android.widget.CheckedTextView"),
        Pair("AutoCompleteTextView", "android.widget.AutoCompleteTextView"),
        Pair("MultiAutoCompleteTextView", "android.widget.MultiAutoCompleteTextView"),
        Pair("RatingBar", "android.widget.RatingBar"),
        Pair("SeekBar", "android.widget.SeekBar"),
        Pair("Spinner", "android.widget.Spinner"),
        Pair("Switch", "android.widget.Switch"),
        Pair("ToggleButton", "android.widget.ToggleButton"),
        Pair("ProgressBar", "android.widget.ProgressBar"),
    )
}
