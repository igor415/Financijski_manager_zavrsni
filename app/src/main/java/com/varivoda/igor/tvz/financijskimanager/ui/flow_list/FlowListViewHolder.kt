package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import kotlinx.android.synthetic.main.menu_list_item.view.*

class FlowListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val item = itemView.menuItem

    fun bind(product: Product){
        item.text = product.productName
    }

    companion object{
        fun create(parent: ViewGroup) : FlowListViewHolder{
            return FlowListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.menu_list_item,parent,false))
        }
    }
}