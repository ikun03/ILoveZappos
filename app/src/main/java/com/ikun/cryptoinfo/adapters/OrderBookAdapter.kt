package com.ikun.cryptoinfo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikun.cryptoinfo.R

class OrderBookAdapter(
    private val dataset: List<List<String>>,
    private val context: Context?
) : RecyclerView.Adapter<OrderBookAdapter.MyViewHolder>() {
    class MyViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val priceView: TextView = itemView.findViewById(R.id.tv_price)
        val amountView: TextView = itemView.findViewById(R.id.tv_amount)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.view_price_amount,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == 0) {
            holder.priceView.text = "Price"
            holder.amountView.text = "Amount"

            holder.priceView.textSize = 12F
            holder.amountView.textSize = 12F
        } else {
            holder.priceView.text = dataset[position][0]
            holder.amountView.text = dataset[position][1]
            holder.priceView.textSize = 12F
            holder.amountView.textSize = 12F
        }
    }
}

