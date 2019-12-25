package com.example.nivapptirgul.ui.fragments.Preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.nivapptirgul.R
import kotlinx.android.synthetic.main.activity_main.*

class PreferenceFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var toolbar = activity !!.activity_toolbar
        toolbar.title = ""
    }






}
