package com.example.irrigationsystem.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.irrigationsystem.R
import com.example.irrigationsystem.models.HumidityModel
import com.example.irrigationsystem.models.SoilModel
import com.example.irrigationsystem.models.TempModel
import com.example.irrigationsystem.tools.Mapper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.Series
import java.util.*
import kotlin.collections.ArrayList

private lateinit var graph :GraphView
private lateinit var databaseRef : FirebaseDatabase
private val mapper = Mapper()

class Graphs : Fragment(),View.OnClickListener {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        databaseRef = Firebase.database
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        graph = view.findViewById(R.id.mainGraph)
        view.findViewById<Button>(R.id.btnHumGraph).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnTempGraph).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnSoilGraph).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnCombinedGraph).setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnHumGraph -> startCreateHumGraph()
            R.id.btnTempGraph -> startCreateTempGraph()
            R.id.btnSoilGraph -> startCreateSoilGraph()
            R.id.btnCombinedGraph -> startCreateCombinedGraph()
        }
    }

    private fun startCreateCombinedGraph() {
        TODO("Not yet implemented")
    }

    private fun startCreateSoilGraph() {
        val humRef = databaseRef.reference.child("Soil_Moisture")
        val measurements = arrayListOf<SoilModel>()
        humRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(graph.context,"Error loading data",Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(measurementSnapshot in snapshot.children){
                    val measurement = measurementSnapshot.getValue(SoilModel::class.java)
                    measurements.add(measurement!!)
                }
                val series = generateSoilLineGraphSeries("Soil Moisture",measurements)
                generateSingleSeriesGraph("Soil Moisture","%",series)
            }
        })
    }

    private fun startCreateTempGraph() {
        val humRef = databaseRef.reference.child("Temperature")
        val measurements = arrayListOf<TempModel>()
        humRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(graph.context,"Error loading data",Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(measurementSnapshot in snapshot.children){
                    val measurement = measurementSnapshot.getValue(TempModel::class.java)
                    measurements.add(measurement!!)
                }
                val series = generateTempLineGraphSeries("Temperature",measurements)
                generateSingleSeriesGraph("Temperature","C",series)
            }
        })
    }
}

fun startCreateHumGraph(){
    val humRef = databaseRef.reference.child("Humidity")
    val measurements = arrayListOf<HumidityModel>()
    humRef.addListenerForSingleValueEvent(object:ValueEventListener{
        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(graph.context,"Error loading data",Toast.LENGTH_SHORT).show()
        }
        override fun onDataChange(snapshot: DataSnapshot) {
            for(measurementSnapshot in snapshot.children){
                val measurement = measurementSnapshot.getValue(HumidityModel::class.java)
                measurements.add(measurement!!)
            }
            val series = generateHumLineGraphSeries("Humidity",measurements)
            generateSingleSeriesGraph("Humidity","%",series)
        }
    })

}
fun generateSingleSeriesGraph(label:String,affix:String,series:LineGraphSeries<DataPoint?>){
    graph.removeAllSeries()
    graph.addSeries(series)
    graph.gridLabelRenderer.verticalAxisTitle = label
    graph.gridLabelRenderer.horizontalAxisTitle = "Date"
    graph.legendRenderer.isVisible = true
    graph.legendRenderer.align = LegendRenderer.LegendAlign.TOP
    graph.legendRenderer.width = 350
    graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
        override fun formatLabel(value: Double, isValueX: Boolean): String {
            return if (isValueX) {
                super.formatLabel(value, isValueX)
            }else{
                super.formatLabel(value, isValueX) + affix
            }
        }
    }
}
fun generateHumLineGraphSeries(title:String,measurements:ArrayList<HumidityModel>):LineGraphSeries<DataPoint?>{
    val dataPoints = arrayOfNulls<DataPoint>(measurements.size)
    val dates = arrayListOf<Date>()
    var counter = 0.0
    for(measure in measurements){

        dataPoints[counter.toInt()] = DataPoint(counter,measure.value)
        counter +=1.0
    }
    val series = LineGraphSeries(dataPoints)
    series.title = title
    series.setOnDataPointTapListener { _, dataPoint -> Toast.makeText(graph.context,dataPoint.toString(),Toast.LENGTH_SHORT).show() }
    series.isDrawDataPoints = true
    return series
}
fun generateTempLineGraphSeries(title:String,measurements:ArrayList<TempModel>):LineGraphSeries<DataPoint?>{
    val dataPoints = arrayOfNulls<DataPoint>(measurements.size)
    val dates = arrayListOf<Date>()
    var counter = 0.0
    for(measure in measurements){

        dataPoints[counter.toInt()] = DataPoint(counter,measure.value)
        counter +=1.0
    }
    val series = LineGraphSeries(dataPoints)
    series.title = title
    series.setOnDataPointTapListener { _, dataPoint -> Toast.makeText(graph.context,dataPoint.toString(),Toast.LENGTH_SHORT).show() }
    series.isDrawDataPoints = true
    return series
}
fun generateSoilLineGraphSeries(title:String,measurements:ArrayList<SoilModel>):LineGraphSeries<DataPoint?>{
    val dataPoints = arrayOfNulls<DataPoint>(measurements.size)
    val dates = arrayListOf<Date>()
    var counter = 0.0
    for(measure in measurements){

        dataPoints[counter.toInt()] = DataPoint(counter,mapper.humValueToPercent(measure.value))
        counter +=1.0
    }
    val series = LineGraphSeries(dataPoints)
    series.title = title
    series.setOnDataPointTapListener { _, dataPoint -> Toast.makeText(graph.context,dataPoint.toString(),Toast.LENGTH_SHORT).show() }
    series.isDrawDataPoints = true
    return series
}
fun generateHumGraph(measurements: ArrayList<HumidityModel>) {
    graph.removeAllSeries()
    val dataPoints = arrayOfNulls<DataPoint>(measurements.size)
    val dates = arrayListOf<Date>()
    var counter = 0.0
    for(measure in measurements){

        dataPoints[counter.toInt()] = DataPoint(counter,measure.value)
        counter +=1.0
    }
    val series = LineGraphSeries(dataPoints)
    series.title = "Humidity"
    series.setOnDataPointTapListener { _, dataPoint -> Toast.makeText(graph.context,dataPoint.toString(),Toast.LENGTH_SHORT).show() }
    graph.addSeries(series)
    graph.gridLabelRenderer.verticalAxisTitle = "Humidity"
    graph.gridLabelRenderer.horizontalAxisTitle = "Date"
    graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
        override fun formatLabel(value: Double, isValueX: Boolean): String {
            return if (isValueX) {
                super.formatLabel(value, isValueX)
            }else{
                super.formatLabel(value, isValueX) + "%"
            }
        }
    }
}
