package top.cuboid.vpsally.di

import android.app.Application

class VPSAlly : Application() {

    companion object {
        lateinit var appContainer: AppContainer
    }

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }

}