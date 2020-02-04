package com.example.nivapptirgul.ui.fragments.recycler

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nivapptirgul.R
import com.example.nivapptirgul.data.db.entity.Reminder
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SwipeAdapter(context: Context,  val items: ArrayList<Reminder>) :
    RecyclerView.Adapter<SwipeAdapter.ViewHolder>() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var listener: OnItemClickListener

    /**
     * Will inflate the item file to create the view of every row
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = inflater.inflate(R.layout.reminder_list_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * Will set the title and body text for each element
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items.elementAt(position)
        holder.title.text = item.title
        holder.body.text = item.body

        var dateTime = formatDateToTime(item.date)
        var dateDate = formatDateToPropperDate(item.date)
        holder.time.text = dateTime
        holder.date.text = dateDate

        if (item.isDone()){
            holder.itemView.setBackgroundColor(Color.CYAN)
        }else{
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)

        }
    }

    override fun getItemCount(): Int = items.size


    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    fun restoreItem(item: Reminder, position: Int) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    fun setData(newItems: ArrayList<Reminder>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateItem(item: Reminder, position: Int) {
        removeItem(position)
        restoreItem(item, position)
    }

    fun addItem(item: Reminder) {
        items.add(item)
        notifyItemInserted(items.size)
        notifyItemRangeChanged(items.size, items.size)
    }

    fun getItem(position: Int): Reminder = items.elementAt(position)


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.item_title)
        var body: TextView = itemView.findViewById(R.id.item_body)
        var time: TextView = itemView.findViewById(R.id.item_time_time)
        var date: TextView = itemView.findViewById(R.id.item_time_date)


        init {
            itemView.setOnClickListener {
                var position = adapterPosition
                if (position != RecyclerView.NO_POSITION && listener != null)
                    listener.onItemClick(items.elementAt(position), position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(reminder: Reminder, position: Int)

    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        listener = onItemClickListener
    }

    private fun formatDateToTime(date:Date): String{
        val formatter = SimpleDateFormat("hh:mm")
        return formatter.format(date)
    }
    private fun formatDateToPropperDate(date: Date):String{
        val formatter = SimpleDateFormat("dd.MM.yy\nE")
        return formatter.format(date)
    }
}