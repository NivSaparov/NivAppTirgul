package com.example.nivapptirgul.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.nivapptirgul.R
import com.example.nivapptirgul.data.provider.DataPreferenceProviderImpl
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)
        setSupportActionBar(activity_toolbar)



    }

    override fun onResume() {
        super.onResume()

        /** Get user name from provider and set toolbar in case user changed
         *
          */

        val provider = DataPreferenceProviderImpl(this)
        activity_toolbar.title = provider.getUserName()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        navController.navigate(R.id.open_preference_fragment)
        return super.onOptionsItemSelected(item)
    }
}
