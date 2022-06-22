package com.daniil.shevtsov.timetravel.core.domain

import com.daniil.shevtsov.timetravel.core.BalanceConfig

fun balanceConfig(
    tickRateMillis: Long = 0L,
) = BalanceConfig(
    tickRateMillis = tickRateMillis,
)
