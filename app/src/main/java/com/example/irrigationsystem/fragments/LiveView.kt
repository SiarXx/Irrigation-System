package com.example.irrigationsystem.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.irrigationsystem.R
import com.example.irrigationsystem.tools.Mapper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var database: FirebaseDatabase
private lateinit var waterLevelValueText: TextView
private lateinit var curHumValueText: TextView
private lateinit var curTempValueText: TextView
private lateinit var curSoilValueText: TextView
private val mapper = Mapper()

class LiveView : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_live_view, container, false)

        waterLevelValueText = view.findViewById(R.id.water_level_value)
        curHumValueText = view.findViewById(R.id.cur_hum_value)
        curTempValueText = view.findViewById(R.id.cur_temp_value)
        curSoilValueText = view.findViewById(R.id.cur_soil_value)
        view.findViewById<FloatingActionButton>(R.id.btnBackLive).setOnClickListener{v ->
            Navigation.findNavController(v).popBackStack()
        }

        val ref = database.reference
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(waterLevelValueText.context,error.message,Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                waterLevelValueText.text = snapshot.child("Water_Level").value.toString() + "%"
                curHumValueText.text = snapshot.child("CurHumidity").value.toString() + "%"
                curTempValueText.text = snapshot.child("CurTemperature").value.toString() + "C"
                curSoilValueText.text = mapper.humValueToPercent(
                        (snapshot.child("CurSoil").value as Long).toDouble()).toString() + "%"
            }

        })
        return view
    }


}