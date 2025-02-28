package top.cuboid.vpsally.presentation.server_list

import top.cuboid.vpsally.domain.DataErrors

sealed interface ServerListEvents {
    data class Success(val success: UiEvents) : ServerListEvents
    data class Error(val error: DataErrors) : ServerListEvents
}

enum class UiEvents {
    SERVER_SAVED
}