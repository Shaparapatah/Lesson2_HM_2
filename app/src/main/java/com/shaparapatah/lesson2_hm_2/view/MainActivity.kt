package com.shaparapatah.lesson2_hm_2.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shaparapatah.lesson2_hm_2.R
import com.shaparapatah.lesson2_hm_2.view.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance()).commit()

    }
}