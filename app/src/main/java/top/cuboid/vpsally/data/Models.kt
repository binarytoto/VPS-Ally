package top.cuboid.vpsally.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable

