package com.shaparapatah.lesson2_hm_2.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.shaparapatah.lesson2_hm_2.databinding.FragmentDetailsBinding
import com.shaparapatah.lesson2_hm_2.domain.Weather
import com.shaparapatah.lesson2_hm_2.utils.showSnackbar
import com.shaparapatah.lesson2_hm_2.viewModel.AppState
import com.shaparapatah.lesson2_hm_2.viewModel.DetailsViewModel
import com.squareup.picasso.Picasso

class DetailsFragment : Fragment() {


    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
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
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        getWeatherFromRemote()
    }


    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.INVISIBLE
                binding.mainView.visibility = View.VISIBLE

                val throwable = appState.error
                binding.root.showSnackbar("ERROR $throwable", "RELOAD") {
                    getWeatherFromRemote()
                }
            }
            AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
                binding.mainView.visibility = View.INVISIBLE

            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.INVISIBLE
                binding.mainView.visibility = View.VISIBLE
                val weather = appState.weatherData
                showWeather(weather[0])
                Snackbar.make(binding.root, "Success", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun showWeather(weather: Weather) {
        with(binding) {
            cityName.text = localWeather.city.name
            cityCoordinates.text =
                "lat ${localWeather.city.lat}\n lon ${localWeather.city.lon}"
            temperatureValue.text = weather.temp.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
            weatherCondition.text = weather.condition
            when (weather.condition) {
                "clear" -> weatherCondition.text = "Ясно"
                "partly-cloudy" -> weatherCondition.text = "Малооблачно"
                "cloudy" -> weatherCondition.text = "Облачно с прояснениями"
                "overcast" -> weatherCondition.text = "Пасмурно"
                "drizzle" -> weatherCondition.text = "Морось"
                "light-rain" -> weatherCondition.text = "Небольшой дождь"
                "rain" -> weatherCondition.text = "Дождь"
                "moderate-rain" -> weatherCondition.text = "Умеренно сильный дождь"
                "heavy-rain" -> weatherCondition.text = "Сильный дождь"
                "continuous-heavy-rain" -> weatherCondition.text = "Длительный сильный дождь"
                "showers" -> weatherCondition.text = "Ливень"
                "wet-snow" -> weatherCondition.text = "Дождь со снегом"
                "light-snow" -> weatherCondition.text = "Небольшой снег"
                "snow" -> weatherCondition.text = "Снег"
                "snow-showers" -> weatherCondition.text = "Снегопад"
                "hail" -> weatherCondition.text = "Град"
                "thunderstorm" -> weatherCondition.text = "Гроза"
                "thunderstorm-with-rain" -> weatherCondition.text = "Дождь с грозой"
                "thunderstorm-with-hail" -> weatherCondition.text = "Гроза с градом"
                "night" -> weatherCondition.text = "Ночь"
                "morning" -> weatherCondition.text = "Утро"
                "day" -> weatherCondition.text = "День"
                "evening" -> weatherCondition.text = "вечер"
                else -> weatherCondition.text = "Ясно"
            }

            imageView
            Picasso
                .get()
                //  .load("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")
                .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                .into(imageViewHeader)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getWeatherFromRemote() {
        viewModel.getWeatherFromRemoteSource(localWeather.city.lat, localWeather.city.lon)
    }

}

