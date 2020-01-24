package com.ikun.cryptoinfo.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ikun.cryptoinfo.R
import com.ikun.cryptoinfo.activities.MainActivity.Companion.service
import com.ikun.cryptoinfo.data.PriceAlertData
import com.ikun.cryptoinfo.workers.PriceAlertWorker
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PriceAlertFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PriceAlertFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PriceAlertFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_price_alert, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onStart() {
        super.onStart()
        val sharedPref = activity?.getPreferences(
            Context.MODE_PRIVATE
        )

        service.checkForPriceAlert().enqueue(object : retrofit2.Callback<PriceAlertData> {
            override fun onFailure(call: Call<PriceAlertData>, t: Throwable) {

            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<PriceAlertData>,
                response: Response<PriceAlertData>
            ) {
                view!!.findViewById<TextView>(R.id.tv_price_noti)
                    .setText("Last price was: " + response.body()?.last)
            }

        })

        view!!.findViewById<EditText>(R.id.et_price_threshold)
            .setText(sharedPref?.getString(getString(R.string.price_alert_string), "0"))
        view!!.findViewById<Button>(R.id.btn_set_alert)
            .setOnClickListener {
                context?.let { it1 -> WorkManager.getInstance(it1) }?.cancelAllWork()
                view!!.findViewById<TextView>(R.id.tv_price_noti)
                    .setText(
                        "New price threshold set to : "
                                +
                                view!!.findViewById<EditText>(R.id.et_price_threshold).text.toString()
                    )
                view!!.findViewById<TextView>(R.id.tv_price_noti).setTextColor(Color.GREEN)

                val data = workDataOf(
                    "priceVal" to
                            view!!.findViewById<EditText>(R.id.et_price_threshold).text.toString().toDouble()
                )


                with(sharedPref?.edit()) {
                    this?.putString(
                        getString(R.string.price_alert_string),
                        view!!.findViewById<EditText>(R.id.et_price_threshold).text.toString()
                    )
                    this?.commit()
                }

                val updateWorkRequest =
                    PeriodicWorkRequestBuilder<PriceAlertWorker>(1, TimeUnit.HOURS)
                        .setInputData(data)
                        .build()

                WorkManager.getInstance(this.context!!).enqueue(updateWorkRequest)

            }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

}
