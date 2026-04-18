package com.littlelemon.application.address.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.littlelemon.application.address.data.local.models.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(addresses: List<AddressEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Query("SELECT COUNT(*) FROM ADDRESSENTITY")
    suspend fun getAddressCount(): Int

    @Query("SELECT * FROM ADDRESSENTITY ORDER BY createdAt DESC")
    fun getAllAddress(): Flow<List<AddressEntity>>

    @Delete
    suspend fun deleteAddress(address: AddressEntity): Int

    @Query("DELETE FROM ADDRESSENTITY")
    suspend fun clear(): Int

    @Transaction
    suspend fun clearAndInsertAllAddress(addresses: List<AddressEntity>) {
        clear()
        insertAddress(addresses)
    }

}