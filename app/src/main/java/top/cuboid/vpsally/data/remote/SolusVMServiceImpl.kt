package top.cuboid.vpsally.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import top.cuboid.vpsally.data.local.SolusServer
import top.cuboid.vpsally.domain.SolusRequestConstants

class SolusVMServiceImpl(
    private val client: HttpClient
) : SolusVMService {
    override suspend fun connect(action: String, server: SolusServer): HttpResponse {

        return client.post {
            url(server.requestUrl)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(SolusRequestConstants.KEY, server.key)
                        append(
                            SolusRequestConstants.HASH, server.hash)
                        append(SolusRequestConstants.ACTION, action)
                        append(SolusRequestConstants.HDD_FLAG, true)
                        append(SolusRequestConstants.MEMORY_FLAG, true)
                        append(SolusRequestConstants.BANDWIDTH_FLAG, true)
                        append(SolusRequestConstants.IPADDR_FLAG, true)
                        append(SolusRequestConstants.STATUS_FLAG, true)
                    })
            )
            //expectSuccess = true
        }
    }
}