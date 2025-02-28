package top.cuboid.vpsally.data

data class Server(
    val id: Int,
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

