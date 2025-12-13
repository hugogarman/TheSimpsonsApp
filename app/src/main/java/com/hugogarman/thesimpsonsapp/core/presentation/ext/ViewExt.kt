package com.hugogarman.thesimpsonsapp.core.presentation.ext

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}