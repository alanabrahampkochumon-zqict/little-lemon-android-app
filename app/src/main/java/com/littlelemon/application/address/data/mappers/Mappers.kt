package com.littlelemon.application.address.data.mappers

import android.location.Location
import com.littlelemon.application.address.domain.models.LocalLocation

fun Location.toLocalLocation(): LocalLocation = LocalLocation(this.latitude, this.longitude)
