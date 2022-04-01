package ru.iu3.numlab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import ru.iu3.numlab.databinding.ItemNumberBinding
import java.text.SimpleDateFormat

class NumberAdapter(private val myNumber: MutableList<NumberItem>): RecyclerView.Adapter<NumberAdapter.ViewHolder>() {
    inner class ViewHolder(
        override val containerView: View,
        private var Number: ItemNumberBinding):RecyclerView.ViewHolder(Number.root),
        LayoutContainer {
            private var currentItem: NumberItem? = null

            fun bind(item:NumberItem) = with(containerView){
                currentItem = item
                Number.number.text=item.toString()
                Number.numberSpecifics.text=item.createSubtitle()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val numberView = inflater.inflate(R.layout.item_number,parent,false)
        val inflateNumberItem = ItemNumberBinding.inflate(inflater,parent,false)
        return ViewHolder(numberView,inflateNumberItem)
    }

    override fun onBindViewHolder(viewHolder: NumberAdapter.ViewHolder, position: Int) {
        viewHolder.bind(myNumber[position])
    }

    override fun getItemCount(): Int {
        return myNumber.size
    }

    fun swapNumbers(newNumbers:MutableList<NumberItem>){
        val diffCallback = NumberDiffCallback(this.myNumber,newNumbers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.myNumber.clear()
        this.myNumber.addAll(newNumbers)
        diffResult.dispatchUpdatesTo(this)
    }
}