package com.littlelemon.application.profile.data

import com.littlelemon.application.profile.data.remote.SupabaseProfileRemoteDataSource
import com.littlelemon.application.profile.domain.ProfileRepository
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

class DefaultProfileRepositoryTest {

    private lateinit var remoteDS: SupabaseProfileRemoteDataSource
    private lateinit var repository: ProfileRepository

    @BeforeEach
    fun setUp() {
        remoteDS = mockk()

        // TODO: Add tests

    }

}