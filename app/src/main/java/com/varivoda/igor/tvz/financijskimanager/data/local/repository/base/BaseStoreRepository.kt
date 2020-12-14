package com.varivoda.igor.tvz.financijskimanager.data.local.repository.base

import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Location
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.model.PieChartEntry
import kotlinx.coroutines.flow.Flow

interface BaseStoreRepository {
    fun getStores(): Flow<List<Store>>
    fun getAllStores(): List<Store>
    fun storeTotalPerYear(year: String): List<PieChartEntry>
    fun storeBestSellProduct(
        month: String,
        year: String,
        productId: Int,
        productName: String
    ): String
    fun getAllLocations(): List<Location>
}