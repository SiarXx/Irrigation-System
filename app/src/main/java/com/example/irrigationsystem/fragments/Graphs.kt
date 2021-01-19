package com.example.irrigationsystem.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.irrigationsystem.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

private lateinit var graph :GraphView

class Graphs : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        graph = view.findViewById(R.id.mainGraph)

        //Populate with Example Data
        val series =
            LineGraphSeries(
                arrayOf<DataPoint>(
                    DataPoint(0.0, 1.0),
                    DataPoint(1.0, 5.0),
                    DataPoint(2.0, 3.0),
                    DataPoint(3.0, 2.0),
                    DataPoint(4.0, 6.0)
                )
            )
        series.color = Color.RED
        val series2 =
            LineGraphSeries(
                arrayOf<DataPoint>(
                    DataPoint(0.0,2.1),
                    DataPoint(1.0,3.3),
                    DataPoint(2.0,5.7),
                    DataPoint(3.0,8.3),
                    DataPoint(4.0,0.3)
                )
            )
        graph.addSeries(series2)
        graph.addSeries(series)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}