package com.shaparapatah.lesson2_hm_2.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shaparapatah.lesson2_hm_2.Contacts.ContentProviderFragment
import com.shaparapatah.lesson2_hm_2.R
import com.shaparapatah.lesson2_hm_2.maps.MapsFragment
import com.shaparapatah.lesson2_hm_2.view.history.HistoryFragment
import com.shaparapatah.lesson2_hm_2.view.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, filter)


        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance()).commit()

    }

    private var networkChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val noConnectivity =
                intent?.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
            if (noConnectivity != null) {
                onConnectionFound()
            } else {
                onConnectionLost()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_fragment_history -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HistoryFragment.newInstance())
                    .addToBackStack("").commit()
                true
            }
            R.id.action_open_fragment_content_provider -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ContentProviderFragment.newInstance())
                    .addToBackStack("").commit()
                true
            }
            R.id.action_open_fragment_menu_google_maps -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MapsFragment.newInstance()).addToBackStack("")
                    .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun onConnectionLost() {
        Toast.makeText(this, "Connection lost", Toast.LENGTH_LONG).show()
    }

    fun onConnectionFound() {
        Toast.makeText(this, "Connection found", Toast.LENGTH_LONG).show()
    }

}