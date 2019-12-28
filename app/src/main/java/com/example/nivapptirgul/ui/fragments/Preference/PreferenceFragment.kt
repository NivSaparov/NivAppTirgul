package com.example.nivapptirgul.ui.fragments.Preference

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.example.nivapptirgul.R
import kotlinx.android.synthetic.main.preference_fragment.*

class PreferenceFragment : PreferenceFragmentCompat() {
    private lateinit var navController: NavController


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        imageButton_back.setOnClickListener {
            activity!!.onBackPressed()
        }
    }

}