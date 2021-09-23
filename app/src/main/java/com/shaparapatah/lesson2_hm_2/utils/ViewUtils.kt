package com.shaparapatah.lesson2_hm_2.utils


import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(text: String, actionText: String, action: (View) -> Unit) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).setAction(actionText, action).show()
}