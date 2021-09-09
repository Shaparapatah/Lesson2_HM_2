package com.shaparapatah.lesson2_hm_2.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.lesson_1423_2_2_main.repository.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
) :
    ViewModel() {


    fun getLiveData() = liveDataToObserver


    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(false)

    fun getWeatherFromLocalSourceRussian() = getDataFromLocalSource(true)


    private fun getDataFromLocalSource(isRussian: Boolean) {
        with(liveDataToObserver) {
            postValue(AppState.Loading)
            Thread {
                sleep(1000)
                if (isRussian) {
                    postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorageRus()))
                } else
                    postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorageWorld()))
            }.start()
        }

    }
}
