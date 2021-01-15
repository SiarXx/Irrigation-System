package com.example.irrigationsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.irrigationsystem.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.NumberFormatException

private lateinit var database: FirebaseDatabase
private lateinit var minWaterEditText :EditText
private lateinit var maxWaterEditText :EditText
class Controls : Fragment(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_controls, container, false)
        minWaterEditText = view.findViewById(R.id.MinWaterValue)
        maxWaterEditText = view.findViewById(R.id.MaxWaterValue)

        val saveValuesBtn = view.findViewById<Button>(R.id.SaveValuesBtn)
        val startPumpBtn = view.findViewById<Button>(R.id.ManualWateringBtn)
        saveValuesBtn.setOnClickListener(this)
        startPumpBtn.setOnClickListener(this)

        val waterReference = database.getReference("Watering")
        waterReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view.context,error.message,Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                minWaterEditText.setText(snapshot.child("Min").value.toString())
                maxWaterEditText.setText(snapshot.child("Max").value.toString())
            }

        })
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.SaveValuesBtn -> validateValues()
            R.id.ManualWateringBtn -> ManualWatering()
        }
    }

    private fun validateValues() {
        var minValue = 0
        var maxValue = 0
        try {
             minValue = Integer.parseInt(minWaterEditText.text.toString())
             maxValue = Integer.parseInt(maxWaterEditText.text.toString())
            if (maxValue <= minValue) {
                Toast.makeText(context,"Stop value cannot be equal or lower than start value",Toast.LENGTH_SHORT).show()
                return
            }
            if(maxValue > 700 || maxValue <= 0){
                Toast.makeText(context,"Stop value have to be between 1 and 700",Toast.LENGTH_SHORT).show()
                return
            }
            if(minValue >= 700 || minValue < 0){
                Toast.makeText(context,"Start value have to be between 0 and 699",Toast.LENGTH_SHORT).show()
                return
            }
        }catch (e:NumberFormatException) {
            Toast.makeText(context,"Given values have to be numbers",Toast.LENGTH_SHORT).show()
        }
        saveValues(minValue,maxValue)


    }

    private fun ManualWatering() {
        var canWater = false
        val ref = database.reference
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                canWater = snapshot.child("ManualWatering").getValue() as Boolean
            }

        })
        if(canWater){
            ref.child("ManualWatering").setValue(true)
        }
        else
            Toast.makeText(context,"There is watering request pending",Toast.LENGTH_SHORT).show()
    }

    private fun saveValues(min:Int,max:Int) {
        var canChange = false
        val ref = database.reference
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                canChange = snapshot.child("ChangeLevel").getValue() as Boolean
            }

        })
        if(canChange){
            ref.child("ChangeLevel").setValue(true)
            ref.child("Watering").child("Min").setValue(min)
            ref.child("Watering").child("Max").setValue(max)
        }
        else
            Toast.makeText(context,"There is change request pending",Toast.LENGTH_SHORT).show()
    }
}
