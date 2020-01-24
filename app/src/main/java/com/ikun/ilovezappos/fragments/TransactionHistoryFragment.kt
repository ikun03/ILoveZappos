package com.ikun.ilovezappos.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import com.ikun.ilovezappos.R
import com.ikun.ilovezappos.activities.MainActivity.Companion.service
import com.ikun.ilovezappos.data.TransactionHistoryData
import com.ikun.ilovezappos.interfaces.DateValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionHistoryFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var chart: LineChart
    override fun onStart() {
        super.onStart()

        view!!.findViewById<LineChart>(R.id.chrt_transaction_history)
        chart = view!!.findViewById(R.id.chrt_transaction_history)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        service.getTransactionData().enqueue(object : Callback<List<TransactionHistoryData>> {
            override fun onFailure(call: Call<List<TransactionHistoryData>>, t: Throwable) {
                Snackbar.make(
                    view!!.findViewById(android.R.id.content),
                    t.message.toString(),
                    Snackbar.LENGTH_LONG
                )
            }

            override fun onResponse(
                call: Call<List<TransactionHistoryData>>,
                response: Response<List<TransactionHistoryData>>
            ) {

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
                chart.xAxis.valueFormatter = (DateValueFormatter())
                chart.invalidate()
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_transaction_history, container, false)
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

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}
