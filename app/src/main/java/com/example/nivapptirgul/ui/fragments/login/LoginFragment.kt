package com.example.nivapptirgul.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.nivapptirgul.R
import com.example.nivapptirgul.di.DaggerAppComponent
import com.example.nivapptirgul.ui.fragments.ScopedFragment
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginFragment : ScopedFragment() {

    @Inject
    lateinit var viewModel: LoginViewModel
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
        context?.let { context ->
            DaggerAppComponent.factory().create(context).inject(this)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        launch {
            if (viewModel.loginWithoutNetwork()) {
                navController.navigate(R.id.action_loginFragment_to_listFragment)

            } else {
                bindUI()

            }
        }
    }


    private fun bindUI() {
        showLoading(false)

        viewModel.isLoggedIn.observe(this@LoginFragment, Observer {
            if (it)
                navController.navigate(R.id.action_loginFragment_to_listFragment)
        })

        viewModel.updatesToUser.observe(this@LoginFragment, Observer { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            showLoading(false)
        })

        button_login.setOnClickListener {
            showLoading(true)
            val username = editText_username.text.trim().toString()
            if (username.isNotEmpty()) viewModel.login(username)
        }

        button_register.setOnClickListener {
            showLoading(true)
            val username = editText_username.text.trim().toString()
            if (username.isNotEmpty()) viewModel.register(username)

        }
    }


    /**
     * Show or hide group_loading_home -> progressbar and textView
     */
    private fun showLoading(show: Boolean) {
        if (group_loading_home != null) {
            if (show) {
                group_loading_home.visibility = View.VISIBLE
            } else {
                group_loading_home.visibility = View.GONE
            }
        }
    }
}
