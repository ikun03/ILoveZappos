package com.ikun.ilovezappos.data

data class OrderBookData(
    val timestamp: String,
    val bids: List<List<String>>,
    val asks: List<List<String>>
)