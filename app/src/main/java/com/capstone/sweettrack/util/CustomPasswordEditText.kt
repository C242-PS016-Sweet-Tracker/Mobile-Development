package com.capstone.sweettrack.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.coding.sweettrack.R

class CustomPasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {
    private var showButtonImage: Drawable


    init {
        showButtonImage =
            ContextCompat.getDrawable(context, R.drawable.baseline_remove_red_eye_24) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validatePassword(password: String) {
        error = if (password.length < 8) {
            context.getString(R.string.invalid_format_password)
        } else {
            null
        }
    }
}
