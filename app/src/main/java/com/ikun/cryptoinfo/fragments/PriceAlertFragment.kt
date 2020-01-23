package com.ikun.cryptoinfo.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ikun.cryptoinfo.R
import com.ikun.cryptoinfo.workers.PriceAlertWorker
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
        view!!.findViewById<Button>(R.id.btn_set_alert)
            .setOnClickListener {
                val data = workDataOf(
                    "priceVal" to
                            view!!.findViewById<Button>(R.id.btn_set_alert).text.toString().toDouble()
                )
                
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
