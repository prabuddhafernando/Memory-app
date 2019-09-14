package com.example.gameapp.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gameapp.R
import com.example.gameapp.common.inflate
import com.example.gameapp.data.BoxModel
import com.example.gameapp.data.BoxStatus
import kotlinx.android.synthetic.main.list_item.view.*

class BoxGridAdapter : RecyclerView.Adapter<BoxGridAdapter.HolderSearchProperty>() {

    private var list: List<BoxModel>? = ArrayList()
    var onBoxItemClickListener: OnBoxItemClickListener? = null


    fun setBoxItemList(itemList: List<BoxModel>) {
        list = itemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderSearchProperty {
        return HolderSearchProperty(parent)
    }

    override fun getItemCount() = list!!.size

    override fun onBindViewHolder(holder: HolderSearchProperty, position: Int) {
        holder.bind(list!![position])
    }

    inner class HolderSearchProperty(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.list_item)) {
        fun bind(boxModel: BoxModel) = with(itemView) {
            box_item.setBoxView(boxModel)
            itemView.setOnClickListener {
                if (boxModel.Status == BoxStatus.IDLE) {
                    boxModel.Status = BoxStatus.ACTIVE
                    box_item.setBoxView(boxModel)
                    onBoxItemClickListener?.onBoxItemClick(boxModel)
                }
            }
        }
    }

    interface OnBoxItemClickListener {
        fun onBoxItemClick(boxModel: BoxModel)
    }
}