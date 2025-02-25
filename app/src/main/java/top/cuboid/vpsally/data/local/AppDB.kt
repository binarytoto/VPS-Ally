package top.cuboid.vpsally.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SolusServer::class], version = 1)
abstract class AppDB: RoomDatabase() {
    abstract fun solusServerDao(): SolusServersDao
}