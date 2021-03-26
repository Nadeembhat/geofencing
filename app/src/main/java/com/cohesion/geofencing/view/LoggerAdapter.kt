package com.cohesion.geofencing.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cohesion.geofencing.Logger
import com.cohesion.geofencing.R


/**
 * Created by Er Nadeem Bhat on 26/3/21
 *Time : 19 :33
 *Project Name: Geo Fencing
 *Company: Mobinius Technology Pvt Ltd.
 *Email: nadeem.nb@mobinius.com
 * Copyright (c)
 */
class LoggerAdapter(private var loggerarray:ArrayList<Logger>):RecyclerView.Adapter<LoggerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var index = itemView.findViewById<TextView>(R.id.index)
        var message = itemView.findViewById<TextView>(R.id.message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.logger_itemview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.index.setText(position.toString())
        holder.message.setText(loggerarray[position].error)
    }

    override fun getItemCount(): Int {
        return loggerarray.size
    }
}