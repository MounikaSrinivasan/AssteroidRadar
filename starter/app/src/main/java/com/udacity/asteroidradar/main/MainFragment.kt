package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.asteroidadapter.AsteroidDataAdapter
import com.udacity.asteroidradar.asteroidadapter.AsteroidListener
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.db.database.AsteroidDatabase

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = AsteroidDatabase.getInstance(application).asteroidDatabaseDao


        val viewModelFactory = MainViewModelFactory(application, dataSource)
        viewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(MainViewModel::class.java)

        val binding = FragmentMainBinding.inflate(inflater)
        val adapter = AsteroidDataAdapter(AsteroidListener { asteroidId ->
            viewModel.onAsteroidClicked(asteroidId)
        })

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = adapter

        binding.statusLoadingWheel.visibility = View.VISIBLE

        viewModel.asteroidId.observe(viewLifecycleOwner, Observer { asteroidId ->
            asteroidId?.let {
                val asteroid = viewModel.asteroidList.value?.find { it.id == asteroidId }
                asteroid?.let {
                    this.findNavController()
                        .navigate(MainFragmentDirections.actionShowDetail(asteroid))
                    viewModel.resetAsteroidId()
                }
            }
        })

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.statusLoadingWheel.visibility = View.GONE
                adapter.submitList(it)
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
