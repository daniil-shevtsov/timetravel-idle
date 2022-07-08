package com.daniil.shevtsov.timetravel.feature.resources.presentation

data class ValidTransferDirection(
    val takeMax: Boolean,
    val take: Boolean,
    val store: Boolean,
    val storeMax: Boolean,
) {

    companion object {
        val None = validTransferDirection()
        val All = validTransferDirection(
            takeMax = true,
            take = true,
            store = true,
            storeMax = true,
        )
    }
}

fun validTransferDirection(
    takeMax: Boolean = false,
    take: Boolean = false,
    store: Boolean = false,
    storeMax: Boolean = false,
) = ValidTransferDirection(
    takeMax = takeMax,
    take = take,
    store = store,
    storeMax = storeMax,
)
