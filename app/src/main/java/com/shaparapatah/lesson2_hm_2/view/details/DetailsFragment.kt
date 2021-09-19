package com.shaparapatah.lesson2_hm_2.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.shaparapatah.lesson2_hm_2.R
import com.shaparapatah.lesson2_hm_2.databinding.FragmentDetailsBinding
import com.shaparapatah.lesson2_hm_2.domain.Weather
import com.shaparapatah.lesson2_hm_2.repository.WeatherDTO
import com.shaparapatah.lesson2_hm_2.repository.WeatherLoader
import com.shaparapatah.lesson2_hm_2.repository.WeatherLoaderListener

class DetailsFragment : Fragment(), WeatherLoaderListener {

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val weatherDTO = it.getParcelableExtra<WeatherDTO>(DETAILS_LOAD_RESULT_EXTRA)
                if (weatherDTO != null) {
                    showWeather(weatherDTO)
                } else {
                    showError()
                }
            }

        }

    }

    fun showError() {
        Toast.makeText(requireActivity(), "Connection lost", Toast.LENGTH_LONG).show()
    }


    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    companion object {

        const val BUNDLE_WEATHER_KAY = "KEY"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    private val localWeather: Weather by lazy {
        (arguments?.getParcelable(BUNDLE_WEATHER_KAY)) ?: Weather()


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = Intent(requireActivity(), DetailsService::class.java)
        intent.putExtra(LATITUDE_EXTRA, localWeather.city.lat)
        intent.putExtra(LONGITUDE_EXTRA, localWeather.city.lon)
        requireActivity().startService(intent)
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(receiver, IntentFilter(DETAILS_INTENT_FILTER))

    }


    private fun showWeather(weatherDTO: WeatherDTO) {

        with(binding) {

            cityName.text = localWeather.city.name
            cityCoordinates.text =
                "lat ${localWeather.city.lat}\n lon ${localWeather.city.lon}"
            temperatureValue.text = weatherDTO.fact.temp.toString()
            feelsLikeValue.text = weatherDTO.fact.feelsLike.toString()
            weatherCondition.text = weatherDTO.fact.condition

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(receiver)
    }

    override fun onLoaded(weatherDTO: WeatherDTO) {
        showWeather(weatherDTO)
    }

    override fun onFailed(throwable: Throwable) {
        Toast.makeText(requireContext(), "Ошибка $throwable", Toast.LENGTH_LONG).show()
    }

}

