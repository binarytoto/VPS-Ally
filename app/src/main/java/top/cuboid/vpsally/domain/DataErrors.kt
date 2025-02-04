package top.cuboid.vpsally.domain

sealed interface DataErrors : Error {
    enum class Network : DataErrors {
        RESOURCE_REDIRECT,
        RESOURCE_NOT_FOUND,
        RESOURCE_SERVER_ERROR,
        BAD_RESPONSE,
        NO_INTERNET,
        API_ERROR,
        INVALID_KEY,
        INVALID_HASH
    }
}