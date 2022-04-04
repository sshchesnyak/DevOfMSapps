package ru.iu3.electronicslab

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.iu3.electronicslab.databinding.ListPosBinding

class ElectronicsAdapter(private var myDevices: MutableList<ElectronicsItem>,
                         private val onShortClicked: (item: ElectronicsItem)->Unit): RecyclerView.Adapter<ElectronicsAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val containerView: View,
        private var Device: ListPosBinding,
        private val onShort: (item: ElectronicsItem) -> Unit,
        private val context: Context): RecyclerView.ViewHolder(Device.root), View.OnClickListener, View.OnLongClickListener{
        private var currentDevice:ElectronicsItem ?= null

        init{
            Device.electronicsContainer.setOnClickListener(this)
            Device.electronicsContainer.setOnLongClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(device:ElectronicsItem) = with(containerView){
            currentDevice = device
            Device.cellName.text = device.name
            Device.cellPrice.text = Utils().returnPrice(device.price)+" ₽"
        }

        override fun onClick(view: View?) {
            currentDevice?.let(onShort)
        }

        override fun onLongClick(p0: View?): Boolean {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.alertTitle))
                .setMessage(context.getString(R.string.alertBody))
                .setNegativeButton(context.getString(R.string.no)) { dialog, _ -> dialog.cancel() }
                .setPositiveButton(context.getString(R.string.yes))
                { dialog, _ ->
                    run {
                        val file = Utils().getFile(context)
                        val devices: MutableList<ElectronicsItem> = Utils().getAllDevices(context)
                        val index = devices.indexOf(currentDevice)

                        if (index != -1) devices.removeAt(index)
                        Utils().writeToFile(file,devices)
                        dispatchUpdates(devices)
                        dialog.cancel()
                    }
                }
            builder.create()
            builder.show()
            return true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectronicsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val deviceView = inflater.inflate(R.layout.list_pos,parent,false)
        val inflateElectronicsItem = ListPosBinding.inflate(inflater,parent,false)
        return ViewHolder(deviceView,inflateElectronicsItem,onShortClicked,parent.context)
    }

    override fun onBindViewHolder(viewHolder: ElectronicsAdapter.ViewHolder, position: Int) {
        viewHolder.bind(myDevices[position])
    }

    override fun getItemCount(): Int {
        return myDevices.size
    }

    fun dispatchUpdates(newDevices: MutableList<ElectronicsItem>){
        val diffCallback = ElectronicsDiffCallback(this.myDevices, newDevices)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.myDevices = newDevices
        diffResult.dispatchUpdatesTo(this)
    }
}