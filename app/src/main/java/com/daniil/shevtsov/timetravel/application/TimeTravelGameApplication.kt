package com.daniil.shevtsov.timetravel.application

import android.app.Application
import com.daniil.shevtsov.timetravel.common.di.initKoin
import com.daniil.shevtsov.timetravel.core.BalanceConfig
import com.daniil.shevtsov.timetravel.core.di.DaggerAppComponent
import com.daniil.shevtsov.timetravel.core.di.koin.appModule
import com.daniil.shevtsov.timetravel.core.domain.SelectorKey
import com.daniil.shevtsov.timetravel.core.navigation.Screen
import com.daniil.shevtsov.timetravel.feature.actions.domain.createInitialActions
import com.daniil.shevtsov.timetravel.feature.coreshell.domain.GameState
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.timetravel.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.timetravel.feature.location.domain.Locations
import com.daniil.shevtsov.timetravel.feature.location.domain.createAllLocations
import com.daniil.shevtsov.timetravel.feature.plot.domain.createInitialPlots
import com.daniil.shevtsov.timetravel.feature.resources.domain.Resource
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceId
import com.daniil.shevtsov.timetravel.feature.resources.domain.ResourceValue
import com.daniil.shevtsov.timetravel.feature.resources.domain.StoredResource
import com.daniil.shevtsov.timetravel.feature.tags.domain.createAllTags
import com.daniil.shevtsov.timetravel.feature.tags.domain.createInitialPresentTags
import com.daniil.shevtsov.timetravel.feature.time.domain.PassedTime
import org.koin.core.Koin
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class TimeTravelGameApplication : Application() {
    lateinit var koin: Koin
    val appComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(
                appContext = applicationContext,
                initialGameState = GameState(
                    balanceConfig = createBalanceConfig(),
                    drawerTabs = createInitialDrawerTabs(),
                    currentScreen = Screen.Main,
                    screenStack = listOf(Screen.Main),
                    plot = createInitialPlots().first(),
                    plots = createInitialPlots(),
                    passedTime = PassedTime(Duration.ZERO),
                    resources = ResourceId.values().map { id ->
                        Resource(id = id, name = id.toString(), value = 0f)
                    },
                    storedResources = ResourceId.values().map { id ->
                        StoredResource(
                            id = id,
                            current = ResourceValue(0f),
                            max = ResourceValue(5f)
                        )
                    },
                    allTags = createAllTags(),
                    presentTags = createInitialPresentTags(),
                    actions = createInitialActions(),
                    timeMoments = emptyList(),
                    allLocations = createAllLocations(),
                    currentLocationId = Locations.researchLab.id,
                    selectorExpandedStates = SelectorKey.values().associateWith { false }
                )
            )
    }

    @Inject
    lateinit var viewModel: TimeTravelGameViewModel


    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        koin = initKoin {
            modules(appModule)
        }.koin

        appComponent.inject(this)

        viewModel.onStart()
    }

    override fun onTerminate() {
        viewModel.onCleared()
        super.onTerminate()
    }

    private fun createBalanceConfig() = BalanceConfig(
        tickRate = 500.milliseconds,
    )

    private fun createInitialDrawerTabs() = listOf(
        DrawerTab(id = DrawerTabId.Info, title = "Info", isSelected = true),
        DrawerTab(id = DrawerTabId.Debug, title = "Debug", isSelected = false),
    )

    private fun createInitialPlot() = "Lol"

}
