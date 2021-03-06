package com.varivoda.igor.tvz.financijskimanager.ui.bills

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.BillRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.EmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.model.BillDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class BillViewModel(private val billRepository: BillRepository,
                    private val employeeRepository: EmployeeRepository,
                    private val productRepository: BaseProductRepository,
                    private val context: Context
) : ViewModel(){

   // private val billRepository = BillRepository(AppDatabase.getInstance(context))
    //private val employeeRepository = EmployeeRepository(AppDatabase.getInstance(context))
    //private val productRepository = ProductRepository(AppDatabase.getInstance(context))
    //var filteredBills = MutableLiveData<List<BillDTO>>()
    var employeeInvoiceNumberOfDays = MutableLiveData<String>()

    fun getBills(month: String, year: String): LiveData<PagedList<BillDTO>> {
        val bills = billRepository.getBills(month, year)
        println("debug bills je ${bills.value?.size}")
        return bills
    }

    suspend fun getEmployeeTotalPerMonthAndYear(month: String, year: String): String? {
        return employeeRepository.getEmployeeTotalPerMonthAndYear(month, year)
    }

    fun getEmployeeMostDaysIssuedInvoice(year: String){
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO){
                employeeInvoiceNumberOfDays.postValue(employeeRepository.getEmployeeMostDaysIssuedInvoice(year))
            }
        }
    }

    val yearSelected = MutableLiveData<String>()
    var productSmallestShareResult = MutableLiveData<String>()

    val mostItemsOnBill: LiveData<String?> = Transformations.switchMap(yearSelected){
        productRepository.getMostItemsOnBill(yearSelected.value)
    }

    fun productSmallestShare(month: String, year: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = productRepository.productSmallestShare(month, year)
            val total = productRepository.totalPerMonth(month, year)
            if(result != null && total != null) {
                val splitted = result.split("#")
                val percent = String.format(Locale.getDefault(), "%.2f", splitted[1].toFloat() / total.toFloat() * 100)
                productSmallestShareResult.postValue(context.getString(R.string.product_smallest_share,splitted[0],percent))
            }else{
                productSmallestShareResult.postValue(result)
            }

        }
    }
}