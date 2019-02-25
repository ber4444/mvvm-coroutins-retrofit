package com.androidinterview

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.server.Server
import com.server.pojo.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Repo {

    fun getItems(liveData: MutableLiveData<Image>, prefs: SharedPreferences)
            = CoroutineScope(Dispatchers.IO).launch {
        try {
            val result = Server().getItemsAsync().await()
            val set = prefs.getStringSet("favs", null)
            if (set != null) for (img in result.GalleryImages)
                if (set.contains(img.ImageUrls.SizeLarge)) img.Checked = true
            liveData.postValue(result)
        } catch (e: Throwable) {
            Log.e("Repo", e.message)
        }
    }

    fun saveOrDeleteFav(url: String, prefs: SharedPreferences) {
        val set = HashSet<String>(prefs.getStringSet("favs", HashSet<String>()))
        if (set.contains(url)) set.remove(url) else set.add(url)
        prefs.edit().putStringSet("favs", set).apply()
    }
}