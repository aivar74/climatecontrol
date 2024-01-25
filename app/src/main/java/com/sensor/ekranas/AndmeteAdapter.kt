package com.sensor.ekranas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.sql.Struct

data class ListItem(val ipAddress: String)


class AndmetePank(){

    val koll_list= deviceList

    val pordid= arrayOf(
        ":8088",
        ":8099",
        ":8100"
    )


    val dataset = arrayOf(
        "82.131.3.41",
        "82.131.3.41",
        "82.131.3.41")

}

data class DeviceData(
    val IP_aadress: String,
    val PortNr: String,
    val In1: String,
    val In2: String,
    val Out1: String,
    val Out2: String
)

val deviceList = listOf(
    DeviceData("192.168.0.41", "8091","IN1=ON", "IN2=OFF","OUT1=OFF","OUT2=ON"),
    DeviceData("192.168.0.42", "8092","IN1=ON", "IN2=OFF","OUT1=OFF","OUT2=ON"),
    DeviceData("192.168.0.42", "8093","IN1=ON", "IN2=OFF","OUT1=OFF","OUT2=ON"),
    DeviceData("192.168.0.42", "8094","IN1=ON", "IN2=OFF","OUT1=OFF","OUT2=ON"),
    DeviceData("192.168.0.42", "8095","IN1=ON", "IN2=OFF","OUT1=OFF","OUT2=ON")

    // ja nii edasi...
)


var aadress:String="http://82.131.3.41"
//val dataset = arrayOf("January", "February", "March")

private val dataSet: List<String> = listOf(
    "http://82.131.3.41:8091",
    "http://82.131.3.41:8092",
    "http://82.131.3.41:8093",
    "http://82.131.3.41:8094",
    "http://82.131.3.41:8097"
)


// class StringListAdapter(private val itemList: MutableList<ListItem>) : RecyclerView.Adapter<StringListAdapter.ViewHolder>() {



class CustomAdapter(private val dataSet: List<DeviceData>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        // val port:TextView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.textView_IP)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_layout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position].IP_aadress
        viewHolder.textView.text = dataSet[position].PortNr
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}



