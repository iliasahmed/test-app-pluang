package com.iliasahmed.testpluang.model

data class QuotesModel(
    val sid: String,
    val price: Double,
    val close: Double,
    val change: Double,
    val high: Double,
    val low: Double,
    val volume: Double,
    val date: String
)