package com.ikun.cryptoinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.ikun.cryptoinfo.data.TransactionHistoryData
import com.ikun.cryptoinfo.interfaces.TransactionInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var chart: LineChart
    private lateinit var service: TransactionInterface
    private lateinit var navController: NavController

    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Old way of setting up a toolbar
        //setSupportActionBar(findViewById(R.id.my_toolbar))

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration =
            AppBarConfiguration(navController.graph, findViewById(R.id.drawer_layout))

        //There are two ways to setup an application toolbar
        //Way 1 is :
//        setSupportActionBar(findViewById(R.id.my_toolbar))
//        setupActionBarWithNavController(navController, appBarConfiguration)

        //Way 2 is :
        //Used this for the drawer button to automatically appear
        findViewById<Toolbar>(R.id.my_toolbar)
            .setupWithNavController(navController, appBarConfiguration)

        //Configure the Navigation View
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)

        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.goto_order_book -> {
                    if (navController.currentDestination?.id != R.id.orderBookFragment) {
                        navController.navigate(R.id.to_order_book)
                    }
                    true
                }
                R.id.goto_price_alert -> {

                    if (navController.currentDestination?.id != R.id.priceAlertFragment) {
                        navController.navigate(R.id.to_price_alert)
                    }
                    true
                }
                R.id.goto_transaction_history -> {
                    if (navController.currentDestination?.id != R.id.transactionHistoryFragment) {
                        navController.navigate(R.id.to_transaction_history)
                    }
                    true
                }
                else -> false
            }
        }


        val retrofit = Retrofit.Builder().baseUrl("https://www.bitstamp.net/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        service = retrofit.create(TransactionInterface::class.java)
        chart = findViewById(R.id.chrt_transaction_history)
    }

    override fun onResume() {
        super.onResume()
        service.getTransactionData().enqueue(object : Callback<List<TransactionHistoryData>> {
            override fun onFailure(call: Call<List<TransactionHistoryData>>, t: Throwable) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    t.message.toString(),
                    Snackbar.LENGTH_LONG
                )
            }

            override fun onResponse(
                call: Call<List<TransactionHistoryData>>,
                response: Response<List<TransactionHistoryData>>
            ) {
                //Check if the request is successful
                response.body()?.get(0)?.let {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Request Successful",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                //Now handle the request by adding the data to the linegraph
                val entries: ArrayList<Entry> = ArrayList()
                response.body()
                    ?.forEach {
                        if (it.type == "0") entries.add(
                            Entry(
                                it.date.toFloat(),
                                it.price.toFloat()
                            )
                        )
                    }

                val dataSetType0 = LineDataSet(entries, "Label")
                chart.data = LineData(dataSetType0)
                chart.invalidate()
            }

        })
    }
}
