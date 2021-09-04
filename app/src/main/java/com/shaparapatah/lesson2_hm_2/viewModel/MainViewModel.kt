package com.shaparapatah.lesson2_hm_2.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.lesson_1423_2_2_main.repository.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
    val repositoryImpl: RepositoryImpl = RepositoryImpl()
) :
    ViewModel() {


    fun getLiveData() = liveDataToObserver


    fun getDataFromRemoteSource() {
        liveDataToObserver.postValue(AppState.Loading)
        Thread {
            sleep(2000)
            liveDataToObserver.postValue(AppState.Success(repositoryImpl.getWeatherFromRemoteSource()))
        }.start()
    }
}
