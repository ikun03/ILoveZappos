package com.ikun.ilovezappos.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikun.ilovezappos.R
import com.ikun.ilovezappos.activities.MainActivity.Companion.service
import com.ikun.ilovezappos.adapters.OrderBookAdapter
import com.ikun.ilovezappos.data.OrderBookData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class OrderBookFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var bidRecyclerView: RecyclerView
    private lateinit var askRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        bidRecyclerView = view!!.findViewById(R.id.bid_recycler_view)
        askRecyclerView = view!!.findViewById(R.id.ask_recycler_view)

        val bidLayoutManager = LinearLayoutManager(context)
        val askLayoutManager = LinearLayoutManager(context)
        bidRecyclerView.layoutManager = bidLayoutManager
        askRecyclerView.layoutManager = askLayoutManager

        val bidDecorationDivider =
            DividerItemDecoration(bidRecyclerView.context, bidLayoutManager.orientation)
        bidDecorationDivider.setDrawable(resources.getDrawable(R.drawable.divider))
        bidRecyclerView.addItemDecoration(bidDecorationDivider)

        val askDecorationDivider =
            DividerItemDecoration(askRecyclerView.context, askLayoutManager.orientation)
        askDecorationDivider.setDrawable(resources.getDrawable(R.drawable.divider))
        askRecyclerView.addItemDecoration(askDecorationDivider)


    }

    override fun onResume() {
        super.onResume()
        service.getOrderBookData().enqueue(object : Callback<OrderBookData> {
            override fun onFailure(call: Call<OrderBookData>, t: Throwable) {

            }

            override fun onResponse(call: Call<OrderBookData>, response: Response<OrderBookData>) {
                bidRecyclerView.adapter =
                    response.body()?.bids?.let { OrderBookAdapter(it, context) }
                bidRecyclerView.adapter?.notifyDataSetChanged()

                askRecyclerView.adapter =
                    response.body()?.asks?.let { OrderBookAdapter(it, context) }
                askRecyclerView.adapter?.notifyDataSetChanged()
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_book, container, false)
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
