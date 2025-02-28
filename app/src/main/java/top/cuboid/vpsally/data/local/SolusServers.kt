package top.cuboid.vpsally.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SolusServer(
    @PrimaryKey(autoGenerate = true)
    var sid: Int = 0,
    val requestUrl: String,
    val key: String,
    val hash: String
)
