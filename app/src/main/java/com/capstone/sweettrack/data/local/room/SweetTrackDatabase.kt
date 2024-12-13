package com.capstone.sweettrack.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.capstone.sweettrack.data.local.entity.FavoriteFood
import com.capstone.sweettrack.data.local.entity.HistoryScan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [HistoryScan::class, FavoriteFood::class], version = 1, exportSchema = false)
abstract class SweetTrackDatabase : RoomDatabase() {

    abstract fun eventDao(): SweetTrackDao

    companion object {
        @Volatile
        private var INSTANCE: SweetTrackDatabase? = null

        fun getInstance(context: Context): SweetTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SweetTrackDatabase::class.java, "SweetTrack.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

//        fun getInstance(context: Context, applicationScope: CoroutineScope): SweetTrackDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    SweetTrackDatabase::class.java, "SweetTrack.db"
//                )
//                    .fallbackToDestructiveMigration()
//                    .addCallback(DatabaseCallback(applicationScope))
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
    }

    private class DatabaseCallback(private val applicationScope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                applicationScope.launch {
                    val sweetTrackDao = database.eventDao()
//                    sweetTrackDao.insertHistory(InitialDataSource.getDummyHistoryScans())
//                    sweetTrackDao.insertHistory(InitialDataSource.getDummyHistoryScan())
//                    sweetTrackDao.insertFavorite(InitialDataSource.getDummyFavoriteFoods())
                }
            }
        }
    }
}
