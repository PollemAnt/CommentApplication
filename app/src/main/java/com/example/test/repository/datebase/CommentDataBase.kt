package com.example.test.repository.datebase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.time.LocalDateTime


object LocalDateTimeConverter {
    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? {
        return dateString?.let { LocalDateTime.parse(dateString) }
    }
    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}

@Database(
    entities = [CommentEntity::class],
    version = 1,
    )


@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao
}

object DatabaseProvider {

    lateinit var database: AppDatabase
        private set

    fun init(context: Context) {
        database = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "database-name"
        )
           .addMigrations(object : Migration(startVersion = 1, endVersion = 2) {
                override fun migrate(db: SupportSQLiteDatabase) {
                   db.execSQL("CREATE TABLE something")
               }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

}