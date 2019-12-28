package com.example.nivapptirgul.ui.fragments.ListFragment.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nivapptirgul.R
import com.example.nivapptirgul.data.db.entity.Reminder

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
}