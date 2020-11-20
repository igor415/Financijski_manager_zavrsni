package com.varivoda.igor.tvz.financijskimanager.ui.top10

import android.content.Context
import androidx.lifecycle.*
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import com.varivoda.igor.tvz.financijskimanager.model.ProductDTO
import com.varivoda.igor.tvz.financijskimanager.util.asDomainModel
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentMonth
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear

class Top10ViewModel(private val context: Context) : ViewModel(){

    private val productRepository = ProductRepository(AppDatabase.getInstance(context))

    var monthAndYear = MutableLiveData<Pair<String,String>>()
    init {
        monthAndYear.value = Pair(getCurrentMonth(),getCurrentYear())
    }

    var top10Products: LiveData<List<Product>> = Transformations.switchMap(monthAndYear){
        productRepository.getTop10Products(it.first,it.second)
    }

    var topProducts: LiveData<List<ProductDTO>> = Transformations.map(top10Products){
        it.asDomainModel()
    }

}