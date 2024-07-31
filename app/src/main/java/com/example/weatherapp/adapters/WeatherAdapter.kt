package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemRvBinding
import com.example.weatherapp.models.Forecastday
import com.example.weatherapp.models.Havo

class WeatherAdapter(val list: List<Forecastday>):RecyclerView.Adapter<WeatherAdapter.Vh>() {
    inner class Vh(val itemRvBinding: ItemRvBinding):ViewHolder(itemRvBinding.root){
        fun onBind(forecastday: Forecastday,position: Int){
            itemRvBinding.date.text = forecastday.date
            itemRvBinding.minHarorat.text = forecastday.day.mintemp_c.toString()
            itemRvBinding.maxHarorat.text = forecastday.day.maxtemp_c.toString()
            if (forecastday.hour[10].condition.text == "Sunny" || forecastday.hour[10].condition.text == "Clear"){
                itemRvBinding.imageView.setImageResource(R.drawable.sun)
            }else if (forecastday.hour[10].condition.text == "Light rain shower"){
                itemRvBinding.imageView.setImageResource(R.drawable.rainy)
            }else{
                itemRvBinding.imageView.setImageResource(R.drawable.cloud)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position],position)
    }
}