package top.cuboid.vpsally.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.statement.HttpResponse

interface SolusVMService {

    suspend fun connect(action: String): HttpResponse

    companion object {
        fun create(): SolusVMService {
            return SolusVMServiceImpl(
                client = HttpClient(Android)
            )
        }
    }
}