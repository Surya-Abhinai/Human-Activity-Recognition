package com.manish.movementdetection

import java.io.Serializable

data class Value(
    val timestamp: Long,
    val accX: String,
    val accY: String,
    val accZ: String,
    val gyroX: String,
    val gyroY: String,
    val gyroZ: String
) : Serializable
