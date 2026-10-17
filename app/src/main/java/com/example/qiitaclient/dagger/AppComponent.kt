package com.example.qiitaclient.dagger

import dagger.Component
import com.example.qiitaclient.MainActivity
import javax.inject.Singleton

@Component(modules = arrayOf(ClientMoudule::class))
@Singleton
interface AppComponent {

    fun inject(mainActivity: MainActivity)
}
