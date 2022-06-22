package com.daniil.shevtsov.timetravel.feature.debug.domain

sealed class DebugValue {

    data class Bool(val value: Boolean) : DebugValue()

    data class CustomValue<T>(val value: T) : DebugValue()

}
