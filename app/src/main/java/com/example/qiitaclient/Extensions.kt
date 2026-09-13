package com.example.qiitaclient

import android.view.View
import androidx.annotation.IdRes

fun <T : View> View.bindView(@IdRes id: Int): Lazy<T> = lazy {
    findViewById(id) as T
}


