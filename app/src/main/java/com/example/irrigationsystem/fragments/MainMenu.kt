package com.example.irrigationsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.irrigationsystem.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainMenu : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_menu_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.BtnGraphs).setOnClickListener {
            findNavController().navigate(R.id.action_MainMenu_to_Graphs)
        }
        view.findViewById<Button>(R.id.BtnLiveView).setOnClickListener {
            findNavController().navigate(R.id.action_MainMenu_to_liveView)
        }

        view.findViewById<Button>(R.id.BtnControls).setOnClickListener{
            findNavController().navigate(R.id.action_MainMenu_to_controls)
        }
    }
}