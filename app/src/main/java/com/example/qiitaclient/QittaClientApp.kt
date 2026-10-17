package com.example.qiitaclient

import android.app.Application
import com.example.qiitaclient.dagger.AppComponent
import com.example.qiitaclient.dagger.DaggerAppComponent

class QittaClientApp : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent.create()
    }
}
