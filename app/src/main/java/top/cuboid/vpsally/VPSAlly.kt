package top.cuboid.vpsally

import android.app.Application
import top.cuboid.vpsally.di.AppContainer

class VPSAlly : Application() {

    companion object {
        lateinit var appContainer: AppContainer
    }

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }

}