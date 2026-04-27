package com.littlelemon.application.cart.data

import com.littlelemon.application.cart.data.remote.CartRemoteDataSource
import com.littlelemon.application.cart.data.remote.FakeCartRemoteDataSource
import com.littlelemon.application.cart.domain.CartRepository
import com.littlelemon.application.database.cart.CartDao
import com.littlelemon.application.database.cart.FakeCartDao
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class DefaultCartRepositoryTest {

    private lateinit var remoteDS: CartRemoteDataSource
    private lateinit var localDS: CartDao
    private lateinit var repository: CartRepository


    @BeforeEach
    fun setUp() {
        remoteDS = FakeCartRemoteDataSource()
        localDS = FakeCartDao()

        repository = DefaultCartRepository(remoteDS, localDS)
    }

    @Nested
    inner class UpdateCartItem {

        @Test
        fun success_updatesItemLocallyAndInRemote() {
            
        }

        @Test
        fun remoteFailure_rollbackItem() {

        }

        @Test
        fun localFailure_rethrowsError() {

        }
    }


}