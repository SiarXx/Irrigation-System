package com.example.irrigationsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.irrigationsystem.R
import com.example.irrigationsystem.tools.Mapper
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
private val mapper = Mapper()
private var minValue: Double = 0.0
private var maxValue: Double = 0.0

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
        view.findViewById<FloatingActionButton>(R.id.btnBackControls).setOnClickListener(this)
        saveValuesBtn.setOnClickListener(this)
        startPumpBtn.setOnClickListener(this)

        val waterReference = database.getReference("Watering")
        waterReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view.context,error.message,Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                minWaterEditText.setText(mapper.humValueToPercent((snapshot.child("Min").value as Long).toDouble()).toString())
                maxWaterEditText.setText(mapper.humValueToPercent((snapshot.child("Max").value as Long).toDouble()).toString())
            }

        })
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.SaveValuesBtn -> if(validateValues()){
                saveValues(minValue, maxValue)
            }
            R.id.ManualWateringBtn -> ManualWatering()
            R.id.btnBackControls -> Navigation.findNavController(v).popBackStack()
        }
    }

    private fun validateValues():Boolean {
        try {
             minValue = minWaterEditText.text.toString().toDouble()
             maxValue = maxWaterEditText.text.toString().toDouble()
            if (maxValue <= minValue) {
                Toast.makeText(context,"Stop value cannot be equal or lower than start value",Toast.LENGTH_SHORT).show()
                return false
            }
            if(maxValue > 100.0 || maxValue < 2.0){
                Toast.makeText(context,"Stop value have to be between 2.0 and 100.0",Toast.LENGTH_SHORT).show()
                return false
            }
            if(minValue >= 100.0 || minValue < 1.0){
                Toast.makeText(context,"Start value have to be between 1.0 and 99.0",Toast.LENGTH_SHORT).show()
                return false
            }
        }catch (e:NumberFormatException) {
            Toast.makeText(context,"Given values have to be numbers",Toast.LENGTH_SHORT).show()
            return false
        }
        return true;
    }

    private fun ManualWatering() {
        val ref = database.reference
            ref.child("ManualWatering").setValue(true)
                    .addOnSuccessListener{
                        Toast.makeText(context,"Pump request send",Toast.LENGTH_SHORT).show()
                    }

    }

    private fun saveValues(min:Double,max:Double) {
        val ref = database.reference
            ref.child("Watering").child("Min").setValue(mapper.humPercentToValue(min))
            ref.child("Watering").child("Max").setValue(mapper.humPercentToValue(max))
            ref.child("ChangeLevel").setValue(true)
                    .addOnSuccessListener {
                        Toast.makeText(context,"Values saved successfully",Toast.LENGTH_SHORT).show()
                    }

    }
}
