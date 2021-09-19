package com.shaparapatah.lesson2_hm_2.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shaparapatah.lesson2_hm_2.R
import com.shaparapatah.lesson2_hm_2.domain.Weather
import com.shaparapatah.lesson2_hm_2.view.OnItemViewClickListener

class MainFragmentAdapter(private var onItemViewClickListener: OnItemViewClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainFragmentViewHolder>() {

    private var weatherData: List<Weather> = listOf()


    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentViewHolder {
        return MainFragmentViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.fragment_main_recycler_item, parent, false) as View
        )
    }


    override fun onBindViewHolder(holder: MainFragmentViewHolder, position: Int) {
        holder.render(weatherData[position])
    }

    override fun getItemCount() = weatherData.size


    inner class MainFragmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun render(weather: Weather) {
            itemView.apply {
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                    weather.city.name
                setOnClickListener {
                    onItemViewClickListener?.onItemClick(weather)
                }
            }

        }
    }
}