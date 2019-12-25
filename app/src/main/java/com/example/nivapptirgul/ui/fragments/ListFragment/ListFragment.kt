package com.example.nivapptirgul.ui.fragments.ListFragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.nivapptirgul.R
import com.example.nivapptirgul.data.provider.DataPreferenceProviderImpl
import com.example.nivapptirgul.ui.ListViewModel
import kotlinx.android.synthetic.main.list_fragment.*

class ListFragment : Fragment() {


    private lateinit var viewModelFactory: ListViewModelFactory
    private lateinit var viewModel: ListViewModel

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        navController = findNavController()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelFactory = ListViewModelFactory(
            DataPreferenceProviderImpl(context!!)
        )
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)

        viewModel.getUserName()
        viewModel.userName.observe(this@ListFragment, Observer {title->
            toolbar_listFragment.title = title
        })

        toolbar_listFragment.setOnMenuItemClickListener{
            navController.navigate(R.id.open_preference_fragment)
            true
        }
    }



}
