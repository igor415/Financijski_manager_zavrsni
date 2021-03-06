package com.varivoda.igor.tvz.financijskimanager

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleObserver
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.*
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseInventoryRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.service_locator.ServiceLocator
import com.varivoda.igor.tvz.financijskimanager.workmanager.BroadcastNotification
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.*


class App: Application(), LifecycleObserver{

    val storeRepository: StoreRepository
        get() = ServiceLocator.provideStoreRepository(this)

    val productRepository: BaseProductRepository
        get() = ServiceLocator.provideProductRepository(this)

    val customerRepository: CustomerRepository
        get() = ServiceLocator.provideCustomerRepository(this)

    val employeeRepository: EmployeeRepository
        get() = ServiceLocator.provideEmployeeRepository(this)

    val billRepository: BillRepository
        get() = ServiceLocator.provideBillRepository(this)

    val preferences: Preferences
        get() = ServiceLocator.providePreferences(this)

    val loginRepository: LoginRepository
        get() = ServiceLocator.provideLoginRepository(this)

    val inventoryRepository: BaseInventoryRepository
        get() = ServiceLocator.provideInventoryRepository(this)

    fun setDatabase(password: String): Boolean{
        val v = ServiceLocator.createDatabase(this,password)
        return v != null
    }

    fun clearDatabase(){
        ServiceLocator.clearDatabase()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        //setTimerForDatabaseUpdate()
    }
    private fun setTimerForDatabaseUpdate(){
        val alarmFor: Calendar = Calendar.getInstance()
        alarmFor.set(Calendar.HOUR_OF_DAY, 12)
        alarmFor.set(Calendar.MINUTE, 40)
        alarmFor.set(Calendar.SECOND, 0)

        val i = Intent(
            applicationContext,
            BroadcastNotification::class.java
        )
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            i,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val timer =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

        timer!!.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmFor.timeInMillis,
            pendingIntent
        )
    }

}