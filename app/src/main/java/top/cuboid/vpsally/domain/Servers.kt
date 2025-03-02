package top.cuboid.vpsally.domain

sealed class Servers {
    data class SolusServer(val url: String, val apiKey: String, val apiHash: String)
}