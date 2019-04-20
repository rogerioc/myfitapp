package com.rogerio.myfitapp.core.databindings

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object TextBindings {

    @JvmStatic
    @BindingAdapter(value = ["text", "textParams"], requireAll = false)
    fun setText(textView: TextView, textId: Any?, params: Any?) {
        if (textId != null && textId != 0) {
            if (textId is String) {
                setTextString(textView, textId)
            } else if (textId is Int) {
                setTextResource(textView, textId, params)
            }
        }
    }

    private fun setTextString(textView: TextView, text: String) {
        textView.text = text
    }

    private fun setTextResource(
            textView: TextView,
            textId: Int,
            params: Any?
    ) {
        val text = textView.context.getString(textId)

        if (params != null) {
            textView.text = String.format(text.toString(), params)
        } else {
            textView.text = text
        }

    }

    @JvmStatic
    @BindingAdapter("textColor")
    fun setTextColor(textView: TextView, colorRes: Int) {
        val color = ContextCompat.getColor(textView.context, colorRes)
        textView.setTextColor(color)
    }
}