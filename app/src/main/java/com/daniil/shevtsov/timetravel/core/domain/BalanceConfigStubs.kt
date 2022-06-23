package com.daniil.shevtsov.timetravel.core.domain

import com.daniil.shevtsov.timetravel.core.BalanceConfig
import kotlin.time.Duration

fun balanceConfig(
    tickRate: Duration = Duration.ZERO,
) = BalanceConfig(
    tickRate = tickRate,
)
