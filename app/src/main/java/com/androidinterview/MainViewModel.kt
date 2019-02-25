package com.androidinterview

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.server.pojo.Image

class MainViewModel : ViewModel() {

    val itemsLiveData = MutableLiveData<Image>()

    fun getItems(prefs: SharedPreferences) {
       if (itemsLiveData.value==null) Repo.getItems(itemsLiveData, prefs)
    }

    fun toggleFav(url: String, prefs: SharedPreferences) {
        Repo.saveOrDeleteFav(url, prefs)
    }
}