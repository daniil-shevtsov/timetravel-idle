package com.daniil.shevtsov.timetravel.core.di

import android.content.Context
import com.daniil.shevtsov.timetravel.application.TimeTravelGameApplication
import com.daniil.shevtsov.timetravel.core.BalanceConfig
import com.daniil.shevtsov.timetravel.core.navigation.ScreenHostFragment
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class,
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance appContext: Context,
            @BindsInstance initialGameState: GameState,
        ): AppComponent
    }

    fun inject(screenHostFragment: ScreenHostFragment)
    fun inject(application: TimeTravelGameApplication)
}
