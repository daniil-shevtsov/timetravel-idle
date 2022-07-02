package com.daniil.shevtsov.timetravel.core.di

import com.daniil.shevtsov.timetravel.core.di.viewmodel.ViewModelModule

import dagger.Module

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
interface AppModule
