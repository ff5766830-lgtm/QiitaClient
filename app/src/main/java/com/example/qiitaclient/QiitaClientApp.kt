package com.example.qiitaclient

import android.app.Application
import com.example.qiitaclient.dagger.AppComponent
import com.example.qiitaclient.dagger.DaggerAppComponent

class QiitaClientApp : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent.create()
    }
}
