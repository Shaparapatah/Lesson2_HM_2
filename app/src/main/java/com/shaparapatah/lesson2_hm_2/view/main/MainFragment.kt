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

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)
            .get(MainViewModel::class.java)
    }
    private var isDataSetRus: Boolean = true


    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemClick(weather: Weather) {
            saveMode(weather)
        }
    })

    private fun saveMode(weather: Weather) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .add(R.id.fragment_container, DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(DetailsFragment.BUNDLE_WEATHER_KAY, weather)
                }))
                .addToBackStack("")
                .commit()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            mainFragmentRecyclerView.adapter = adapter
            mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
            viewModel.getLiveData()
                .observe(viewLifecycleOwner, Observer<AppState> {
                    renderData(it)
                })
            viewModel.getWeatherFromLocalSourceRussian()
        }

    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceRussian()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }.also { isDataSetRus = !isDataSetRus }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                val throwable = appState.error
                binding.root.showSnackbarWithoutAction(
                    binding.root,
                    R.string.error,
                    Snackbar.LENGTH_LONG
                ).also { "$throwable" } // FIXME Можно ли так записать? Подскажите пожалуйста
            }
            AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                val weather = appState.weatherData
                adapter.setWeather(weather)
                binding.root.showSnackbarWithoutAction(
                    binding.root,
                    R.string.success,
                    Snackbar.LENGTH_SHORT
                )
            }
        }
    }

    fun View.showSnackbarWithoutAction(view: View, stringId: Int, snackbarLength: Int) {
        Snackbar.make(view, getString(stringId), snackbarLength).show()
    }


    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
        _binding = null
    }
}

