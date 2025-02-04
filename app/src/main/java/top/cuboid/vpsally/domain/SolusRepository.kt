package top.cuboid.vpsally.domain

interface SolusRepository {
    suspend fun performAction(action: String = SolusRequestConstants.STATUS_FLAG): Result<Server, DataErrors.Network>
}

data class Server(
    val host: String?,
    val statusMsg: String?,
    val ip: String?,
    val serverStatus: String?,
    val totalStorage: Long?,
    val usedStorage: Long?,
    val totalBw: Long?,
    val usedBw: Long?,
    var totalMem: Long?,
    var usedMem: Long?
)

