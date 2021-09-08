package com.shaparapatah.lesson2_hm_2.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.shaparapatah.lesson2_hm_2.R
import com.shaparapatah.lesson2_hm_2.databinding.FragmentMainBinding
import com.shaparapatah.lesson2_hm_2.domain.Weather
import com.shaparapatah.lesson2_hm_2.view.OnItemViewClickListener
import com.shaparapatah.lesson2_hm_2.viewModel.AppState
import com.shaparapatah.lesson2_hm_2.viewModel.MainViewModel

class MainFragment : Fragment(), OnItemViewClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() {
            return _binding!!
        }
    private lateinit var viewModel: MainViewModel
    private var isDataSetRus: Boolean = true


    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemClick(weather: Weather) {
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(DetailsFragment.BUNDLE_WEATHER_KAY, weather)
                manager.beginTransaction()
                    .add(R.id.fragment_container, DetailsFragment.newInstance(bundle))
            }
        }
    })


    companion object {
        fun newInstance() = MainFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.mainFragmentRecyclerView.adapter = adapter
        adapter.setOnItemViewClickListener(this)
        binding.mainFragmentFAB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                isDataSetRus = !isDataSetRus
                if (isDataSetRus) {
                    viewModel.getWeatherFromLocalSourceRussian()
                    binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
                } else {
                    viewModel.getWeatherFromLocalSourceWorld()
                    binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
                }
            }
        })
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData()
            .observe(viewLifecycleOwner, Observer<AppState> { appState: AppState ->
                renderData(appState)
            })
        viewModel.getWeatherFromLocalSourceRussian()
    }

    fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                val throwable = appState.error
                Snackbar.make(binding.root, "ERROR $throwable", Snackbar.LENGTH_LONG).show()
            }
            AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                val weather = appState.weatherData
                adapter.setWeather(weather)
                Snackbar.make(binding.root, "Success", Snackbar.LENGTH_SHORT).show()


            }
        }
    }


    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        bundle.putParcelable(DetailsFragment.BUNDLE_WEATHER_KAY, weather)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, DetailsFragment.newInstance(bundle))
            .addToBackStack("")
            .commit()
    }

}

