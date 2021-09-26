package com.shaparapatah.lesson2_hm_2

import android.app.Application
import androidx.room.Room
import com.shaparapatah.lesson2_hm_2.room.HistoryDAO
import com.shaparapatah.lesson2_hm_2.room.HistoryDataBase
import java.util.*

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: MyApp? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "HistoryDataBase.db"

        fun getHistoryDAO(): HistoryDAO {
            if (db == null) {
                if (appInstance != null) {
                    db = Room.databaseBuilder(
                        appInstance!!.applicationContext,
                        HistoryDataBase::class.java,
                        DB_NAME
                    )
                        .allowMainThreadQueries()
                        .build()
                } else {
                    throw IllformedLocaleException("appInstance==null")
                }
            }
            return db!!.historyDAO()
        }
    }
}