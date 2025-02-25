package top.cuboid.vpsally.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.statement.HttpResponse
import top.cuboid.vpsally.data.local.SolusServer

interface SolusVMService {

    suspend fun connect(action: String, server: SolusServer): HttpResponse

    companion object {
        fun create(): SolusVMService {
            return SolusVMServiceImpl(
                client = HttpClient(Android)
            )
        }
    }
}