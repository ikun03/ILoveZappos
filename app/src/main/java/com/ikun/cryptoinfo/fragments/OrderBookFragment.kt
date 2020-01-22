package com.ikun.cryptoinfo.fragments

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
import com.ikun.cryptoinfo.R
import com.ikun.cryptoinfo.activities.MainActivity.Companion.service
import com.ikun.cryptoinfo.adapters.OrderBookAdapter
import com.ikun.cryptoinfo.data.OrderBookData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OrderBookFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OrderBookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderBookFragment : Fragment() {
    // TODO: Rename and change types of parameters
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

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderBookFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderBookFragment().apply {

            }
    }
}
