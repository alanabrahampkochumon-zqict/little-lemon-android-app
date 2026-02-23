package com.littlelemon.application.address.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.littlelemon.application.address.data.local.models.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(addresses: List<AddressEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Query("SELECT COUNT(*) FROM ADDRESSENTITY")
    suspend fun getAddressCount(): Long

    @Query("SELECT * FROM ADDRESSENTITY ORDER BY createdAt DESC")
    fun getAllAddress(): Flow<List<AddressEntity>>

}