package com.example.githubferdi.set

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubferdi.databinding.ActivitySettingBinding
import com.example.githubferdi.fav.Favorite
import com.google.android.material.switchmaterial.SwitchMaterial

class Setting : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private var isDarkModeActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switchTheme: SwitchMaterial = binding.switchTheme

        val pref = SettingTheme.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModel(pref))[SettingView::class.java]

        mainViewModel.getThemeSettings().observe(this) { themeSetting: Boolean ->
            isDarkModeActive = themeSetting
            switchTheme.isChecked = isDarkModeActive
            updateAppTheme()
        }


        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != isDarkModeActive) {
                isDarkModeActive = isChecked
                mainViewModel.saveThemeSetting(isDarkModeActive)
                val toastMessage = if (isChecked) "Dark Mode" else "Light Mode"
                Toast.makeText(this@Setting, toastMessage, Toast.LENGTH_SHORT).show()
                updateAppTheme()
            }
        }

        binding.favorite.setOnClickListener {
            val intent = Intent(this, Favorite::class.java)
            startActivity(intent)
        }

        binding.back1.setOnClickListener {
            finish()
        }
    }

    private fun updateAppTheme() {
        val mode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

}

