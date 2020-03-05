package com.supinfo.instabus

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.supinfo.instabus.data.NearstationsItem


class MainRecycleAdapter(val buses : List<NearstationsItem>) : RecyclerView.Adapter<MainRecycleAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.street_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            name?.let {
                it.text = buses[position].street_name
            }
        }

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("nameStation", "${holder.name.text}")
            intent.putExtra("idStation", buses[position].id)
            intent.putExtra("lat", buses[position].lat)
            intent.putExtra("lon", buses[position].lon)
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.streetName)
    }

    override fun getItemCount() =  buses.size
}