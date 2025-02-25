package top.cuboid.vpsally.di

import android.content.Context
import androidx.room.Room
import top.cuboid.vpsally.data.SolusRepositoryImpl
import top.cuboid.vpsally.data.local.AppDB
import top.cuboid.vpsally.data.remote.SolusVMService

class AppContainer(context: Context) {

    private val service = SolusVMService.create()

    private val db by lazy {
        Room.databaseBuilder(
            context,
            AppDB::class.java,
            "servers.db"
        ).build()
    }

    val solusRepository = SolusRepositoryImpl(service, db)
}