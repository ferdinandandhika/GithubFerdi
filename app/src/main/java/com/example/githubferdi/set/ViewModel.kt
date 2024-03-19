package com.example.githubferdi.set

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ViewModel(private val pref: SettingTheme): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingView::class.java)) {
            return SettingView(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class" + modelClass.name)
    }
}