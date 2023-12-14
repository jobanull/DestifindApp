package com.example.mobiledevelopment.util

import android.content.Context
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun showToast(context : Context, message: String) {
    Toast.makeText(  context, message, Toast.LENGTH_SHORT).show()
}


fun AppCompatActivity.setupView() {
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
    supportActionBar?.hide()
}


fun EditText.setupTextWatcher(onTextChanged: (CharSequence) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onTextChanged(s)
        }

        override fun afterTextChanged(s: Editable) {}
    })
}