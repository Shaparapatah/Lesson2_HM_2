package com.shaparapatah.lesson2_hm_2.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.shaparapatah.lesson2_hm_2.repository.DetailsRepositoryImpl
import com.shaparapatah.lesson2_hm_2.repository.RemoteDataSource
import com.shaparapatah.lesson2_hm_2.repository.WeatherDTO
import com.shaparapatah.lesson2_hm_2.utils.convertDtoToModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class DetailsViewModel(
    private val detailsLiveDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepositoryImpl = DetailsRepositoryImpl(
        RemoteDataSource()
    )
) :
    ViewModel() {


    fun getLiveData() = detailsLiveDataToObserver

    fun getWeatherFromRemoteSource(requestLink: String) {
        detailsLiveDataToObserver.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(requestLink, callback)
    }


    val callback = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            TODO("Not yet implemented")
        }

        override fun onResponse(call: Call, response: Response) {
            val serverResponse: String? = response.body()?.string()
            if (response.isSuccessful && serverResponse != null) {
                val weatherDTO = Gson().fromJson(serverResponse, WeatherDTO::class.java)
                detailsLiveDataToObserver.value = AppState.Success(convertDtoToModel(weatherDTO))
            } else {
                // TODO
            }
        }

    }
}
