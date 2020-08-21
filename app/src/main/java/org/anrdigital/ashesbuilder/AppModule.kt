package org.anrdigital.ashesbuilder

import android.app.Application
import org.anrdigital.ashesbuilder.data.CardRepository
import org.anrdigital.ashesbuilder.ui.CardListViewModel
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val appModule = module {

    single { CardRepository(androidContext()) }

    viewModel {CardListViewModel(get())}
}

open class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))
    }
}