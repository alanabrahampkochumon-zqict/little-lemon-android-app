package com.littlelemon.application.address.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.littlelemon.application.address.data.local.AddressDatabase
import com.littlelemon.application.address.data.local.models.GeocodingEntity
import com.littlelemon.application.address.utils.GeocodingGenerator
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GeocodingDaoTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var db: AddressDatabase
    private lateinit var dao: GeocodingDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(context, AddressDatabase::class.java).build()
        dao = db.geocodingDao
    }

    @After
    fun tearDown() {
        db.close()
    }


    @Test
    fun upsert_insertsEntityIntoDatabase() = runTest {
        // When an entity is upserted
        val (entity, _) = GeocodingGenerator.generateGeocodingEntities()
        dao.upsert(entity)

        // Then the database gets updated
        val result = dao.getAddress(entity.fullAddress)

        assertNotNull(result)
        assertEquals(entity, result)
    }

    @Test
    fun upsert_updatesExistingEntity() = runTest {
        // Given a database with existing geocoding entity
        val (entity, _) = GeocodingGenerator.generateGeocodingEntities()
        dao.upsert(entity)

        // When the entity is upserted
        val updatedEntity =
            entity.copy(latLng = GeocodingEntity.LatLng(5.5, 2.238))
        dao.upsert(updatedEntity)

        // Then, the entry is updated in the database
        val result = dao.getAddress(updatedEntity.fullAddress)
        assertNotNull(result)
        assertEquals(updatedEntity, result)
    }

    @Test
    fun getAddress_withLatLng_whenDatabaseHasMatchingLatLng_returnsCorrectAddress() = runTest {
        // Given a database with existing geocoding entity
        val entities = Array(5) { GeocodingGenerator.generateGeocodingEntities().first }
        entities.forEach { dao.upsert(it) }

        // When queried for a valid entity lat lng
        val result = dao.getAddress(entities[0].latLng.lat, entities[0].latLng.lng)

        // Then, the correct entity is returned
        assertNotNull(result)
        assertEquals(entities[0], result)
    }

    @Test
    fun getAddress_withLatLng_whenDatabaseHasNoMatchingLatLng_returnsNull() = runTest {
        // Given a database with existing geocoding entity
        val entities = Array(5) { GeocodingGenerator.generateGeocodingEntities().first }
        entities.forEach { dao.upsert(it) }

        // When queried for an entity with invalid lat lng
        val result = dao.getAddress(entities[0].latLng.lat - 50.0, entities[0].latLng.lng + 125.0)

        // Then, the result is null
        assertNull(result)
    }

    @Test
    fun getAddress_withLatLng_emptyDatabase_returnsNull() = runTest {
        // Given an empty database

        // When queried for an entity with valid lat lng
        val result = dao.getAddress(1.1234, 2.546)

        // Then, the result is null
        assertNull(result)
    }

    @Test
    fun getAddress_withFullAddress_whenDatabaseHasMatchingFullAddress_returnsCorrectAddress() =
        runTest {
            // Given a database with existing geocoding entity
            val entities = Array(5) { GeocodingGenerator.generateGeocodingEntities().first }
            entities.forEach { dao.upsert(it) }

            // When queried for an entity valid full address
            val result = dao.getAddress(entities[0].fullAddress)

            // Then, the correct entity is returned
            assertNotNull(result)
            assertEquals(entities[0], result)
        }

    @Test
    fun getAddress_withFullAddress_whenDatabaseHasNoMatchingLatLng_returnsNull() = runTest {
        // Given a database with existing geocoding entity
        val entities = Array(5) { GeocodingGenerator.generateGeocodingEntities().first }
        entities.forEach { dao.upsert(it) }

        // When queried for an entity with invalid full address
        val result = dao.getAddress("Address does not exist")

        // Then, the result is null
        assertNull(result)
    }

    @Test
    fun getAddress_withFullAddress_emptyDatabase_returnsNull() = runTest {
        // Given an empty database

        // When queried for an entity with valid fullAddress
        val result = dao.getAddress("Littlemon, Illinois, Chicago")

        // Then, the result is null
        assertNull(result)
    }

    @Test
    fun clearAll_clearsTheDatabase() = runTest {
        // Given a database with existing geocoding entity
        val entities = Array(5) { GeocodingGenerator.generateGeocodingEntities().first }
        entities.forEach { dao.upsert(it) }

        assertEquals(5, dao.getCount())

        // When the db is cleared
        dao.clearAll()

        // Then, all the items are cleared
        assertEquals(0, dao.getCount())
    }

    @Test
    fun getCount_getCountFromDatabase() = runTest {
        // Given a database with n items
        val n = 50
        val entities = Array(n) { GeocodingGenerator.generateGeocodingEntities().first }
        entities.forEach { dao.upsert(it) }

        // When database is queried for count
        val count = dao.getCount()

        // Then, the correct count(n) is returned
        assertEquals(n, count)
    }

    @Test
    fun clearExpired_clearsOnlyExpiredItems() = runTest {
        // Given a database with n items
        val n = 50
        val entities =
            Array(n) { GeocodingGenerator.generateGeocodingEntities(expiry = it.toLong()).first }
        entities.forEach { dao.upsert(it) }

        // When the database is cleared with expiry of 25
        dao.clearExpired(25)

        // Then, all the remaining items in the database have timestamp greater than 25
        entities.forEach { entity ->
            if (entity.createdTimestamp <= 25)
                assertNull(dao.getAddress(entity.fullAddress))
            else
                assertNotNull(dao.getAddress(entity.fullAddress))
        }
    }

}