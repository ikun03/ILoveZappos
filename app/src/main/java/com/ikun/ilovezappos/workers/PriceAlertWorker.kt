package com.ikun.ilovezappos.workers

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ikun.ilovezappos.activities.MainActivity
import com.ikun.ilovezappos.activities.MainActivity.Companion.service
import com.ikun.ilovezappos.data.PriceAlertData
import retrofit2.Call
import retrofit2.Response

class PriceAlertWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : Worker(appContext, workerParameters) {
    override fun doWork(): Result {
        service.checkForPriceAlert().enqueue(object :
            retrofit2.Callback<PriceAlertData> {
            override fun onFailure(call: Call<PriceAlertData>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<PriceAlertData>,
                response: Response<PriceAlertData>
            ) {
                if (response.body()?.last?.toFloat()!! < inputData.getDouble("priceVal", 0.0)) {


                    val noti = NotificationCompat.Builder(applicationContext, "1")
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle("Currency Prices Changed !")
                        .setContentText("Price has fallen to " + response.body()!!.last + " which is below your threshold!")
                        .setStyle(NotificationCompat.BigTextStyle())
                        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true).build()

                    val resultIntent = Intent(applicationContext, MainActivity::class.java)
                    val resultPendingIntent: PendingIntent?
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        resultPendingIntent =
                            TaskStackBuilder.create(applicationContext).run {
                                addNextIntentWithParentStack(resultIntent)
                                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                            }
                        noti.apply { contentIntent = resultPendingIntent }
                    }


                    with(NotificationManagerCompat.from(applicationContext)) {
                        notify(1, noti)
                    }
                }
            }

        })
        return Result.success()
    }

}