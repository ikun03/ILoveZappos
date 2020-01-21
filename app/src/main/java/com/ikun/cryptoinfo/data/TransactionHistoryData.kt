package com.ikun.cryptoinfo.data

data class TransactionHistoryData(
    val date: String,
    val tid: String,
    val price: String,
    val type: String,
    val amount: String
)