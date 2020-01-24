package com.ikun.ilovezappos.interfaces

import com.ikun.ilovezappos.data.OrderBookData
import com.ikun.ilovezappos.data.PriceAlertData
import com.ikun.ilovezappos.data.TransactionHistoryData
import retrofit2.Call
import retrofit2.http.GET

interface TransactionInterface {
    @GET("api/v2/transactions/btcusd/")
    fun getTransactionData(): Call<List<TransactionHistoryData>>

    @GET("api/v2/order_book/btcusd/")
    fun getOrderBookData(): Call<OrderBookData>

    @GET("api/v2/ticker_hour/btcusd")
    fun checkForPriceAlert(): Call<PriceAlertData>

}