package com.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weather.R
import com.weather.network.response.WeatherList

// weather forecast for next 7 day adapter
class WeatherAdapter(private val list: ArrayList<WeatherList>) :
    RecyclerView.Adapter<WeatherAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        val tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_weather, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvDay.text = list[position].temp.day.toString()
        holder.tvTemp.text = list[position].temp.day.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}