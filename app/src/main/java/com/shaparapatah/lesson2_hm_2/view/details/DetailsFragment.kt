package com.shaparapatah.lesson2_hm_2.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.google.android.material.snackbar.Snackbar
import com.shaparapatah.lesson2_hm_2.databinding.FragmentDetailsBinding
import com.shaparapatah.lesson2_hm_2.domain.Weather
import com.shaparapatah.lesson2_hm_2.utils.showSnackbar
import com.shaparapatah.lesson2_hm_2.viewModel.AppState
import com.shaparapatah.lesson2_hm_2.viewModel.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private val binding: FragmentDetailsBinding by viewBinding(CreateMethod.INFLATE)


    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

   /* private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    */

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
       // _binding = FragmentDetailsBinding.inflate(inflater, container, false)
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
            is AppState.SuccessDetails -> {
                binding.loadingLayout.visibility = View.INVISIBLE
                binding.mainView.visibility = View.VISIBLE
                val weather = appState.weatherData
                viewModel.saveWeather(
                    Weather(
                        localWeather.city,
                        weather.temperature,
                        weather.feelsLike,
                        weather.condition
                    )
                )
                showWeather(weather)
                Snackbar.make(binding.root, "Success", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun showWeather(weather: Weather) {
        with(binding) {
            cityName.text = localWeather.city.name
            cityCoordinates.text =
                "lat ${localWeather.city.lat}\n lon ${localWeather.city.lon}"
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
            conditionStatus(weather)

            /*   imageView
               Picasso
                   .get()
                   //  .load("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")
                   .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                   .into(imageViewHeader)

             */

            /*   Glide.with(imageViewHeader)
                   .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                   .into(imageViewHeader)
             */

            imageView.loadUrl(("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg"))
        }
    }


 /*   override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

  */

    private fun getWeatherFromRemote() {
        viewModel.getWeatherFromRemoteSource(localWeather.city.lat, localWeather.city.lon)
    }

    private fun conditionStatus(weather: Weather) {
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
    }

    private fun ImageView.loadUrl(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()
        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }
}

