package com.example.swipeassignment.app

import android.app.Application
import com.example.swipeassignment.di.modules.networkModule
import com.example.swipeassignment.di.modules.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@BaseApplication)
            modules(networkModule, viewModelsModule)
        }
    }
}