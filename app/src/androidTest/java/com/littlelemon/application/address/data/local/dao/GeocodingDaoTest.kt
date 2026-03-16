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
        val entity = GeocodingGenerator.generateGeocodingEntity()
        dao.upsert(entity)

        // Then the database gets updated
        val result = dao.getAddress(entity.fullAddress)

        assertNotNull(result)
        assertEquals(entity, result)
    }

    @Test
    fun upsert_updatesExistingEntity() = runTest {
        // Given a database with existing geocoding entity
        val entity = GeocodingGenerator.generateGeocodingEntity()
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

}