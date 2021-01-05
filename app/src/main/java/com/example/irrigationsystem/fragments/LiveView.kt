package com.example.irrigationsystem.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.irrigationsystem.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var database: FirebaseDatabase
private lateinit var waterLevelValueText: TextView
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
        val ref = database.reference.child("Water_Level")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(waterLevelValueText.context,error.message,Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                waterLevelValueText.text = snapshot.value.toString()
            }

        })
        return view
    }


}

private fun updateLiveValues(){

}