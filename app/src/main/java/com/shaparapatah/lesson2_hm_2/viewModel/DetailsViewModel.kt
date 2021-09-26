package com.shaparapatah.lesson2_hm_2.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaparapatah.lesson2_hm_2.MyApp.Companion.getHistoryDAO
import com.shaparapatah.lesson2_hm_2.domain.Weather
import com.shaparapatah.lesson2_hm_2.repository.DetailsRepositoryImpl
import com.shaparapatah.lesson2_hm_2.repository.LocalRepositoryImp
import com.shaparapatah.lesson2_hm_2.repository.RemoteDataSource
import com.shaparapatah.lesson2_hm_2.repository.WeatherDTO
import com.shaparapatah.lesson2_hm_2.utils.REQUEST_ERROR
import com.shaparapatah.lesson2_hm_2.utils.convertDtoToModel

class DetailsViewModel(
    private val detailsLiveDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepositoryImpl = DetailsRepositoryImpl(
        RemoteDataSource()
    ),
    private val historyRepositoryImpl: LocalRepositoryImp = LocalRepositoryImp(getHistoryDAO())
) :
    ViewModel() {

    fun saveWeather(weather: Weather) {
        Thread {
            historyRepositoryImpl.saveEntity(weather)
        }.start()

    }


    fun getLiveData() = detailsLiveDataToObserver

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveDataToObserver.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(lat, lon, callback)
    }

    private val callback = object : retrofit2.Callback<WeatherDTO> {
        override fun onFailure(call: retrofit2.Call<WeatherDTO>, t: Throwable) {
            detailsLiveDataToObserver.postValue(AppState.Error(Throwable(REQUEST_ERROR)))
        }

        override fun onResponse(
            call: retrofit2.Call<WeatherDTO>,
            response: retrofit2.Response<WeatherDTO>
        ) {

            if (response.isSuccessful && response.body() != null) {
                val weatherDTO = response.body()
                weatherDTO?.let {
                    detailsLiveDataToObserver.postValue( AppState.SuccessDetails(convertDtoToModel(weatherDTO)))
                }
            } else AppState.Error(Throwable(REQUEST_ERROR))
        }
    }
}


