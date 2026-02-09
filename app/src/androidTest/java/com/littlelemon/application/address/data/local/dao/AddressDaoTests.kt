package com.littlelemon.application.address.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.littlelemon.application.address.data.local.AddressDatabase
import com.littlelemon.application.utils.AddressGenerator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddressDaoTests {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit
    var db: AddressDatabase
    private lateinit var dao: AddressDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(context, AddressDatabase::class.java).build()
        dao = db.dao
    }

    @Test
    fun onAddressInsert_withoutDuplicates_uniqueAddressesAreSaved() = runTest {
        // Arrange
        val numAddress = 5
        val addresses = List(numAddress) {
            AddressGenerator.generateAddressEntity()
        }

        // Act
        dao.insertAddress(addresses)
        val actualAddresses = dao.getAllAddress().first()

        // Assert
        assertTrue { actualAddresses.map { address -> address in addresses }.all { it } }
    }

    @Test
    fun onAddressInsert_withDuplicatesInDatabase_addressesAreUpserted() = runTest {
        // Arrange
        val numAddresses = 5
        val duplicateAddress = List(1) { AddressGenerator.generateAddressEntity() }
        dao.insertAddress(duplicateAddress)

        val expectedAddress =
            List(numAddresses) { AddressGenerator.generateAddressEntity() } + duplicateAddress

        // Act
        dao.insertAddress(expectedAddress)
        val actualAddresses = dao.getAllAddress().first()

        // Assert
        assertEquals(expectedAddress.size, actualAddresses.size)
        assertTrue { actualAddresses.map { address -> address in expectedAddress }.all { it } }
    }

    @Test
    fun onAddressInsert_uniqueAddress_insertAddress() = runTest {
        // Arrange
        val address = AddressGenerator.generateAddressEntity()

        // Act
        dao.insertAddress(address)
        val result = dao.getAllAddress().first()

        // Assert
        assertEquals(1, result.size)
        assertEquals(address, result.first())
    }

    @Test
    fun onAddressInsert_duplicateAddress_doesNotInsertAddress() = runTest {
        // Arrange
        val address = AddressGenerator.generateAddressEntity()
        dao.insertAddress(address)
        
        // Act
        dao.insertAddress(address)
        val result = dao.getAllAddress().first()

        // Assert
        assertEquals(1, result.size)
        assertEquals(address, result.first())
    }
}