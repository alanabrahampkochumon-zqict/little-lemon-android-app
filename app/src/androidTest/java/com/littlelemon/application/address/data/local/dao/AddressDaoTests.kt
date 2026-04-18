package com.littlelemon.application.address.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.littlelemon.application.address.data.local.AddressDatabase
import com.littlelemon.application.address.utils.AddressGenerator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddressDaoTests {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit
    var db: AddressDatabase
    private lateinit var dao: AddressDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(context, AddressDatabase::class.java).build()
        dao = db.addressDao
    }

    @After
    fun tearDown() {
        db.close()
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
        val duplicateAddress = AddressGenerator.generateAddressEntity()
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
        println("ACTUAL: $result")
        println("EXPECTED: $address")

        // Assert
        assertEquals(1, result.size)
        assertEquals(address, result.first())
    }

    @Test
    fun onGetAddressCount_emptyDb_returnsZero() = runTest {
        // Arrange & Act
        val count = dao.getAddressCount()

        // Assert
        assertEquals(0, count)
    }

    @Test
    fun onGetAddressCount_filledDb_returnsCorrectAddressCount() = runTest {
        // Arrange
        val numAddresses = 5
        val duplicateAddress = List(numAddresses) { AddressGenerator.generateAddressEntity() }
        dao.insertAddress(duplicateAddress)

        // Act
        val count = dao.getAddressCount()

        // Assert
        assertEquals(numAddresses, count)
    }

    //////// DELETE ADDRESS ///////////

    @Test
    fun onDeleteAddress_emptyDb_returnsZero() = runTest {
        val address = AddressGenerator.generateAddressEntity()
        assertEquals(0, dao.deleteAddress(address))
    }

    @Test
    fun onDeleteAddress_validAddress_returnsOne() = runTest {
        // Given a list of address
        val addresses = List(5) { AddressGenerator.generateAddressEntity() }
        val addressToRemove = addresses[0]
        dao.insertAddress(addresses)

        // When an address is removed
        val status = dao.deleteAddress(addressToRemove)

        // Then a status of 1 is returned
        assertEquals(1, status)
    }

    @Test
    fun onDeleteAddress_validAddress_removesOnlyThePassedInAddress() = runTest {
        // Given a list of address
        val addresses = List(5) { AddressGenerator.generateAddressEntity() }
        val addressToRemove = addresses[0]
        dao.insertAddress(addresses)

        // When an address is removed
        dao.deleteAddress(addressToRemove)
        val retrievedAddress = dao.getAllAddress().first()

        // Then that address is not in the list
        assertFalse { retrievedAddress.contains(addressToRemove) }
        // And all the other address are in the list
        addresses.drop(1).forEach { address ->
            assertTrue("$address was not found!") { retrievedAddress.contains(address) }
        }
    }

    @Test
    fun onDeleteAddress_invalidAddress_returnsZero() = runTest {
        // Given a list of address
        val addresses = List(5) { AddressGenerator.generateAddressEntity() }
        val addressToRemove = AddressGenerator.generateAddressEntity()
        dao.insertAddress(addresses)

        // When an address is removed
        val status = dao.deleteAddress(addressToRemove)

        // Then a status of 0 is returned
        assertEquals(0, status)
    }

    @Test
    fun onDeleteAddress_invalidAddress_removesNoAddress() = runTest {
        // Given a list of address
        val addresses = List(5) { AddressGenerator.generateAddressEntity() }
        val addressToRemove = AddressGenerator.generateAddressEntity()
        dao.insertAddress(addresses)

        // When an address is removed
        dao.deleteAddress(addressToRemove)
        val retrievedAddress = dao.getAllAddress().first()

        // Then no address is removed
        addresses.forEach { address ->
            assertTrue("$address was not found!") { retrievedAddress.contains(address) }
        }
    }
}