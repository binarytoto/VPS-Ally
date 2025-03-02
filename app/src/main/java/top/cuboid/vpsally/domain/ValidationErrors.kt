package top.cuboid.vpsally.domain

interface ValidationErrors: Error {

    enum class SolusValidationErrors: ValidationErrors {
        INVALID_ENTRY,
        EMPTY_ENTRY
    }
}