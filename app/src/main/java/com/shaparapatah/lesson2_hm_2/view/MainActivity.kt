package com.shaparapatah.lesson2_hm_2.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.shaparapatah.lesson2_hm_2.Contacts.ContentProviderFragment
import com.shaparapatah.lesson2_hm_2.R
import com.shaparapatah.lesson2_hm_2.maps.MapsFragment
import com.shaparapatah.lesson2_hm_2.view.history.HistoryFragment
import com.shaparapatah.lesson2_hm_2.view.main.MainFragment

class MainActivity : AppCompatActivity() {


    companion object {
        private const val NOTIFICATION_ID_1 = 1
        private const val NOTIFICATION_ID_2 = 2
        private const val CHANNEL_ID_1 = "channel_id_1"
        private const val CHANNEL_ID_2 = "channel_id_2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, filter)
        notificationsChannels()


        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance()).commit()

    }

    private fun notificationsChannels() { /ff

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilderOne = NotificationCompat.Builder(this, CHANNEL_ID_1).apply {
            setSmallIcon(R.drawable.ic_earth)
            setContentTitle("Заголовок для $CHANNEL_ID_1")
            setContentText("Сообщение для $CHANNEL_ID_1")
            priority = NotificationCompat.PRIORITY_MAX
        }
        val notificationBuilderTwo = NotificationCompat.Builder(this, CHANNEL_ID_2).apply {
            setSmallIcon(R.drawable.ic_russia)
            setContentTitle("Заголовок для $CHANNEL_ID_2")
            setContentText("Сообщение для $CHANNEL_ID_2")
            priority = NotificationCompat.PRIORITY_MAX
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameChannelOne = "Name $CHANNEL_ID_1"
            val descChannelOne = "Description $CHANNEL_ID_1"
            val importanceChannelOne = NotificationManager.IMPORTANCE_MIN
            val channelOne =
                NotificationChannel(CHANNEL_ID_1, nameChannelOne, importanceChannelOne).apply {
                    description = descChannelOne
                }
            notificationManager.createNotificationChannel(channelOne)
        }
        notificationManager.notify(NOTIFICATION_ID_1, notificationBuilderOne.build())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameChannelTwo = "Name $CHANNEL_ID_2"
            val descChannelTwo = "Description $CHANNEL_ID_2"
            val importanceChannelTwo = NotificationManager.IMPORTANCE_HIGH
            val channelTwo =
                NotificationChannel(CHANNEL_ID_2, nameChannelTwo, importanceChannelTwo).apply {
                    description = descChannelTwo
                }
            notificationManager.createNotificationChannel(channelTwo)
        }
        notificationManager.notify(NOTIFICATION_ID_2, notificationBuilderTwo.build())
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