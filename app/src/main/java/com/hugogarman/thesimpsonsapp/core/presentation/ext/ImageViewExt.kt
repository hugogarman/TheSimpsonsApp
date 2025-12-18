package com.hugogarman.thesimpsonsapp.core.presentation.ext

import android.widget.ImageView
import coil.load

fun ImageView.loadUrl(url: String) {
    this.load(url) {
        allowHardware(false)
    }
}