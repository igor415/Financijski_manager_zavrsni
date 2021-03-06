package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Employee",
    foreignKeys = [ForeignKey(
        entity = Location::class,
        parentColumns = arrayOf("locationId"),
        childColumns = arrayOf("locationId"),
        onDelete = CASCADE
    ), ForeignKey(
        entity = Store::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("storeId"),
        onDelete = CASCADE
    )],
    indices = [Index("locationId"), Index("storeId")]
)
data class Employee (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var employeeName: String,
    var employeeLastName: String,
    var address: String? = null,
    var storeId: Int,
    var locationId: Int
)