package top.cuboid.vpsally.data

import android.database.sqlite.SQLiteException
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import top.cuboid.vpsally.data.local.AppDB
import top.cuboid.vpsally.data.local.SolusServer
import top.cuboid.vpsally.domain.DataErrors
import top.cuboid.vpsally.domain.Result
import top.cuboid.vpsally.data.Server
import top.cuboid.vpsally.domain.SolusRepository
import top.cuboid.vpsally.domain.SolusResponseConstants
import top.cuboid.vpsally.data.remote.SolusVMService

class SolusRepositoryImpl(
    private val service: SolusVMService,
    private val db: AppDB
): SolusRepository {

    override suspend fun performRemoteAction(action: String, server: SolusServer): Result<Server, DataErrors.Network> {

        try {
            val response = service.connect(action,server)
            val responseBody = response.body<String>()
            val requestStatus = response.status

            // returns 500 error code with mem flag if server is offline
            if (requestStatus == HttpStatusCode.OK || requestStatus == HttpStatusCode.InternalServerError) {
                val status =
                    """<${SolusResponseConstants.STATUS}>(.*?)</${SolusResponseConstants.STATUS}>""".toRegex()
                        .find(responseBody)?.groupValues?.get(1)

                val statusMsg =
                    """<${SolusResponseConstants.STATUSMSG}>(.*?)</${SolusResponseConstants.STATUSMSG}>""".toRegex()
                        .find(responseBody)?.groupValues?.get(1)

                if (status.equals(SolusResponseConstants.SUCCESS)) {

                    val hostName =
                        """<${SolusResponseConstants.HOSTNAME}>(.*?)</${SolusResponseConstants.HOSTNAME}>""".toRegex()
                            .find(responseBody)?.groupValues?.get(1)
                    val serverStatus =
                        """<${SolusResponseConstants.VMSTAT}>(.*?)</${SolusResponseConstants.VMSTAT}>""".toRegex()
                            .find(responseBody)?.groupValues?.get(1)
                    val ipAddr =
                        """<${SolusResponseConstants.IPADDR}>(.*?)</${SolusResponseConstants.IPADDR}>""".toRegex()
                            .find(responseBody)?.groupValues?.get(1)
                    val hdd =
                        """<${SolusResponseConstants.HDD}>(.*?)</${SolusResponseConstants.HDD}>""".toRegex()
                            .find(responseBody)?.groupValues?.get(1)
                    val hddValues = hdd?.let { parseCSV(it) }
                    val bw =
                        """<${SolusResponseConstants.BW}>(.*?)</${SolusResponseConstants.BW}>""".toRegex()
                            .find(responseBody)?.groupValues?.get(1)
                    val bwValues = bw?.let { parseCSV(bw) }
                    val memory =
                        """<${SolusResponseConstants.MEM}>(.*?)</${SolusResponseConstants.MEM}>""".toRegex()
                            .find(responseBody)?.groupValues?.get(1)
                    val memoryValues = memory?.let { parseCSV(memory) }

                    return Result.Success(
                        Server(
                            id = server.sid,
                            host = hostName,
                            statusMsg = statusMsg,
                            ip = ipAddr,
                            serverStatus = serverStatus,
                            totalStorage = hddValues?.get(0),
                            usedStorage = hddValues?.get(1),
                            totalBw = bwValues?.get(0),
                            usedBw = bwValues?.get(1),
                            totalMem = memoryValues?.get(0),
                            usedMem = memoryValues?.get(1),
                        )
                    )
                } else return if (statusMsg.equals(SolusResponseConstants.INVALID_KEY))
                    Result.Error(DataErrors.Network.INVALID_KEY)
                else if (statusMsg.equals(SolusResponseConstants.INVALID_HASH))
                    Result.Error(DataErrors.Network.INVALID_HASH)
                else if (requestStatus.equals(HttpStatusCode.InternalServerError))
                    Result.Error(DataErrors.Network.RESOURCE_SERVER_ERROR)
                else
                    Result.Error(DataErrors.Network.API_ERROR)
            } else return when (requestStatus.value) {
                in 300..399 -> Result.Error(DataErrors.Network.RESOURCE_REDIRECT)
                in 400..499 -> Result.Error(DataErrors.Network.RESOURCE_NOT_FOUND)
                in 501..599 -> Result.Error(DataErrors.Network.RESOURCE_SERVER_ERROR)
                else -> Result.Error(DataErrors.Network.BAD_RESPONSE)
            }

        } catch (e: Exception) {
            return Result.Error(DataErrors.Network.BAD_RESPONSE)
        }

    }

    private fun parseCSV(line: String): List<Long> {
        return line.split(",").map { it.trim().toLong() }
    }

    override suspend fun getServerDetailsLocally(server: SolusServer): SolusServer? {
       return db.solusServerDao().findByDetail(
           server.key,
           server.hash,
           server.requestUrl)
    }

    override suspend fun saveServer(server: SolusServer): Result<Boolean, DataErrors.Local> {
        return Result.Error(DataErrors.Local.SQL_ERROR)
        try {
            db.solusServerDao().addServer(server)
            return Result.Success(true)
        } catch (e: SQLiteException) {
            return Result.Error(DataErrors.Local.SQL_ERROR)
        } catch (e: Exception) {
            return Result.Error(DataErrors.Local.UNKNOWN_ERROR)
        }
    }



}