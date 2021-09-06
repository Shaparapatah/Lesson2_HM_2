package com.shaparapatah.lesson2_hm_2.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.lesson_1423_2_2_main.repository.RepositoryImpl
import java.lang.IllegalStateException
import java.lang.Thread.sleep
import kotlin.random.Random

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
           // val random = Random(15).nextInt()
            //if (random > 5)
            liveDataToObserver.postValue(AppState.Success(repositoryImpl.getWeatherFromRemoteSource()))
           // else
             //   liveDataToObserver.postValue(AppState.Error(IllegalStateException()))
        }.start()
    }
}
