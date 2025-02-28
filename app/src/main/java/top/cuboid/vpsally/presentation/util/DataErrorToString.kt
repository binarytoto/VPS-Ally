package top.cuboid.vpsally.presentation.util

import androidx.annotation.StringRes
import top.cuboid.vpsally.R
import top.cuboid.vpsally.domain.DataErrors

@StringRes
fun DataErrors.toStringId(): Int {
    return when (this) {
        DataErrors.Local.SQL_ERROR -> R.string.sql_error
        DataErrors.Local.UNKNOWN_ERROR -> R.string.unknown_error
        DataErrors.Network.RESOURCE_REDIRECT -> R.string.redirect_error
        DataErrors.Network.RESOURCE_NOT_FOUND -> R.string.resource_not_found_error
        DataErrors.Network.RESOURCE_SERVER_ERROR -> R.string.server_error
        DataErrors.Network.BAD_RESPONSE -> R.string.bad_response_error
        DataErrors.Network.NO_INTERNET -> R.string.no_internet_error
        DataErrors.Network.API_ERROR -> R.string.api_error
        DataErrors.Network.INVALID_KEY -> R.string.invalid_key_error
        DataErrors.Network.INVALID_HASH -> R.string.invalid_api_hash
    }
}