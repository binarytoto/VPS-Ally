package top.cuboid.vpsally.domain

import androidx.core.text.isDigitsOnly

sealed interface SolusServerValidations: ServerValidations {

    fun isValidApiKey(apiKey: String): Result<Boolean, ValidationErrors.SolusValidationErrors>
    fun isValidApiHash(apiHash: String): Result<Boolean, ValidationErrors.SolusValidationErrors>
    fun isValidPort(port: String): Result<Boolean, ValidationErrors.SolusValidationErrors>
}

data object SolusServerValidator: SolusServerValidations {
    override fun isValidApiKey(apiKey: String): Result<Boolean, ValidationErrors.SolusValidationErrors> {
        if (apiKey.isBlank())
            return Result.Error(ValidationErrors.SolusValidationErrors.EMPTY_ENTRY)
        if (apiKey.length != 17)
            return Result.Error(ValidationErrors.SolusValidationErrors.INVALID_ENTRY)
        if ( apiKey.count {it == '-'} != 2 )
            return Result.Error(ValidationErrors.SolusValidationErrors.INVALID_ENTRY)

        return Result.Success(true)
    }

    override fun isValidApiHash(apiHash: String): Result<Boolean, ValidationErrors.SolusValidationErrors> {
        if (apiHash.isBlank())
            return Result.Error(ValidationErrors.SolusValidationErrors.EMPTY_ENTRY)
        if (apiHash.length != 40)
            return Result.Error(ValidationErrors.SolusValidationErrors.INVALID_ENTRY)
        if (apiHash.contains("[^A-za-z0-9]".toRegex()))
            return Result.Error(ValidationErrors.SolusValidationErrors.INVALID_ENTRY)

        return Result.Success(true)
    }

    override fun isValidPort(port: String): Result<Boolean, ValidationErrors.SolusValidationErrors> {
        if (port.isBlank())
            return Result.Error(ValidationErrors.SolusValidationErrors.EMPTY_ENTRY)
        if (!port.isDigitsOnly())
            return Result.Error(ValidationErrors.SolusValidationErrors.INVALID_ENTRY)
        if (port.toInt() !in 1..65535)
            return Result.Error(ValidationErrors.SolusValidationErrors.INVALID_ENTRY)
        return Result.Success(true)
    }

}