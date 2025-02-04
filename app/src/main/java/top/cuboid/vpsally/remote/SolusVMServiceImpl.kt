package top.cuboid.vpsally.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import top.cuboid.vpsally.domain.SolusRequestConstants

class SolusVMServiceImpl(
    private val client: HttpClient
) : SolusVMService {
    override suspend fun connect(action: String): HttpResponse {

        return client.post {
            url("")
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(SolusRequestConstants.KEY, "")
                        append(
                            SolusRequestConstants.HASH,
                            ""
                        )
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