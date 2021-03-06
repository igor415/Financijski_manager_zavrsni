package com.varivoda.igor.tvz.financijskimanager.data.local.repository.base

import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeProductDTO
import com.varivoda.igor.tvz.financijskimanager.model.HorizontalBarChartEntry
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface BaseEmployeeRepository {
    fun getEmployees(): Flow<List<Employee>>
    fun getEmployeesAndStores(): Flow<List<EmployeeDTO>>
    fun deleteEmployee(id: Int)
    suspend fun getEmployeeTotalPerMonthAndYear(month: String, year: String): String?
    fun getEmployeeMostDaysIssuedInvoice(year: String): String?
    fun insertEmployee(employee: Employee)
    fun getHorizontalBarChartData(month: String, year: String, storeId: Int): List<HorizontalBarChartEntry>
    fun employeeMostProductSell(
        dateSelected: String,
        product: Product
    ): String
    fun changeEmployeeInfo(token: String, employee: Employee): NetworkResult<Boolean>
}