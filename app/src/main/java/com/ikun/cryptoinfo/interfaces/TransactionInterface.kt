package com.ikun.cryptoinfo.interfaces

import com.ikun.cryptoinfo.data.TransactionHistoryData
import retrofit2.Call
import retrofit2.http.GET

interface TransactionInterface {
    @GET("api/v2/transactions/btcusd/")
    fun getTransactionData(): Call<List<TransactionHistoryData>>
}