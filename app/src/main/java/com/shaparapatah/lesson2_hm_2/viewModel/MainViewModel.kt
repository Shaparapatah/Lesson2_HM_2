package com.shaparapatah.lesson2_hm_2.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(private val liveDataToObserver: MutableLiveData<Any> = MutableLiveData()) :
    ViewModel() {


    fun getLiveData() = liveDataToObserver

    fun getDataFromRemoteSource() {
        Thread {
            sleep(2000)
            liveDataToObserver.postValue(Any())
        }.start()
    }
}
