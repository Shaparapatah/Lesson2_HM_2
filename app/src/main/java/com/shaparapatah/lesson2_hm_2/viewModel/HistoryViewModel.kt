package com.shaparapatah.lesson2_hm_2.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaparapatah.lesson2_hm_2.MyApp
import com.shaparapatah.lesson2_hm_2.repository.LocalRepositoryImp

class HistoryViewModel(
    private val historyLiveDataObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepositoryImpl: LocalRepositoryImp =
        LocalRepositoryImp(MyApp.getHistoryDAO())
) : ViewModel() {

    fun getAllHistory() {
        historyLiveDataObserve.value = AppState.Loading
        Thread { //FIXME ПОТОК
            historyLiveDataObserve.postValue(AppState.SuccessMain(historyRepositoryImpl.getAllHistory()))
        }.start()
    }

    fun getLiveData() = historyLiveDataObserve
}