package com.dengzii.plugin.findview.utils

object AndroidViewImportUtil {

    val androidViewClasses = mapOf(
            Pair("View", "android.view.View"),
            Pair("ViewStub", "android.view.ViewStub"),
            Pair("ViewGroup", "android.view.ViewGroup"),
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