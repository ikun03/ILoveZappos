package com.ikun.cryptoinfo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.ikun.cryptoinfo.R
import com.ikun.cryptoinfo.interfaces.TransactionInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var navController: NavController

    companion object {
        private val TAG = MainActivity::class.qualifiedName
        lateinit var service: TransactionInterface
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

    }

    override fun onResume() {
        super.onResume()

    }
}
