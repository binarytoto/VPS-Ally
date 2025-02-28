package top.cuboid.vpsally.presentation.models

data class ServerUi(
    val id: Int,
    val name: String,
    val ip: String,
    val serverStatus: ServerStatus,
    val totalStorage: DisplayableMemoryNum,
    val usedStorage: DisplayableMemoryNum,
    val totalBw: DisplayableMemoryNum,
    val usedBw: DisplayableMemoryNum,
    val totalMem: DisplayableMemoryNum,
    val usedMem: DisplayableMemoryNum
)

enum class ServerStatus {
    ONLINE,
    OFFLINE
}

data class DisplayableMemoryNum(
    val value: Long,
    val formatted: String
)

fun Long.toDisplayableMemoryNum(): DisplayableMemoryNum {
    val megaBytes = this / (1024 * 1024)
    return DisplayableMemoryNum(
        value = this,
        formatted = "$megaBytes MB"
    )
}



