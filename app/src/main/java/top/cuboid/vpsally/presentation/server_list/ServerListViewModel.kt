package top.cuboid.vpsally.presentation.server_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import top.cuboid.vpsally.R
import top.cuboid.vpsally.VPSAlly
import top.cuboid.vpsally.data.SolusRepositoryImpl
import top.cuboid.vpsally.data.local.SolusServer
import top.cuboid.vpsally.domain.Result
import top.cuboid.vpsally.domain.SolusServerValidator
import top.cuboid.vpsally.domain.ValidationErrors
import top.cuboid.vpsally.presentation.models.TextFieldUiState

object SavedStateKeys {
    const val PORT = "port"
    const val USE_HTTPS = "useHttps"
    const val SUB_DOMAIN = "subdomain"
    const val HOST = "host"
    const val SHOW_SHEET = "showSheet"
    const val PATH = "path"
    const val API_KEY = "api_key"
    const val API_HASH = "api_hash"
    const val IS_LOADING = "is_loading"
}

object DefaultValues {
    const val SUBDOMAIN = "solusvm"
    const val HOST = "example.com"
    const val PORT = "5656"
    const val PATH = "api/client/command.php"
}

class HomeViewModel(
    private val state: SavedStateHandle,
    private val repository: SolusRepositoryImpl
) : ViewModel() {

    var httpsChecked by mutableStateOf(
        state.get<Boolean>(SavedStateKeys.USE_HTTPS) ?: true
    )
        private set

    var isLoading by mutableStateOf(
        state.get<Boolean>(SavedStateKeys.IS_LOADING) ?: false
    )

    fun updateHttpsFlag(useHttps: Boolean) {
        httpsChecked = useHttps
        state[SavedStateKeys.USE_HTTPS] = useHttps
    }

    val showSheet = state.getStateFlow(SavedStateKeys.SHOW_SHEET, false)

    fun updateSheetVisibility(showSheet: Boolean) {
        state[SavedStateKeys.SHOW_SHEET] = showSheet
    }
    //TODO error strings may not update after users changes language
    var subDomainUiState by mutableStateOf(
        state.get<TextFieldUiState>(SavedStateKeys.SUB_DOMAIN) ?: TextFieldUiState(
            text = DefaultValues.SUBDOMAIN,
            error = false,
        )
    )
        private set

    private val _events = Channel<ServerListEvents>()
    val events = _events.receiveAsFlow()

    fun updateSubDomain(input: String) {
        //TODO: regex for identify invalid domian
        val subDomainRegex = "[A-Za-z0-9](?:[A-Za-z0-9\\-]{0,61}[A-Za-z0-9])?".toRegex()
        var isError = false
        var error: Int? = null

        if (input.isBlank()) {
            isError = true
            error = R.string.empty_error
        } else if (!subDomainRegex.matches(input)) {
            isError = true
            error = R.string.invalid_entry_error
        }

        subDomainUiState = subDomainUiState.copy(
            text = input,
            error = isError,
            resId = error
        )

        state[SavedStateKeys.SUB_DOMAIN] = subDomainUiState
    }

    var portUiState by mutableStateOf(
        state.get<TextFieldUiState>(SavedStateKeys.PORT) ?: TextFieldUiState(
            text = DefaultValues.PORT,
            error = false,
        )
    )
        private set

    fun updatePort(input: String) {

        val port = input.trim()
        var isError = false
        var error: Int? = null

        when (val isValid = SolusServerValidator.isValidPort(port)) {
            is Result.Error -> {
                isError = true
                if (isValid.error == ValidationErrors.SolusValidationErrors.EMPTY_ENTRY)
                    error = R.string.empty_error
                else
                    error = R.string.invalid_entry_error
            }
            is Result.Success -> {}
        }

        portUiState = portUiState.copy(
            text = port,
            error = isError,
            resId = error
        )

        state[SavedStateKeys.PORT] = portUiState
    }

    var pathUiState by mutableStateOf(
        state.get<TextFieldUiState>(SavedStateKeys.PATH) ?: TextFieldUiState(
            text = DefaultValues.PATH,
            error = false,
        )
    )
        private set

    fun updatePath(input: String) {

        val path = input.trim()
        var isError = false
        var error: Int? = null

        val pathRegex = "".toRegex() //TODO

        if (path.isBlank()) {
            isError = true
            error = R.string.empty_error
        } else if (!pathRegex.matches(path)) {
            isError = true
            error = R.string.invalid_entry_error
        }

        pathUiState = pathUiState.copy(
            text = path,
            error = isError,
            resId = error
        )
    }

    var apiKeyUiState by mutableStateOf(
        state.get<TextFieldUiState>(SavedStateKeys.API_KEY) ?: TextFieldUiState(
            text = "",
            error = false
        )
    )
        private set

    fun updateApiKey(input: String) {
        var key = input.trim()
        var isError = false
        var error: Int? = null

        when (val isValid = SolusServerValidator.isValidApiKey(key)) {
            is Result.Error -> {
                isError = true
                if (isValid.error == ValidationErrors.SolusValidationErrors.EMPTY_ENTRY)
                    error = R.string.empty_error
                else
                    error = R.string.invalid_key_error
            }
            is Result.Success -> {}
        }

        apiKeyUiState = apiKeyUiState.copy(
            text = key,
            error = isError,
            resId = error
        )
    }

    var apiHashUiState by mutableStateOf(
        state.get<TextFieldUiState>(SavedStateKeys.API_HASH) ?: TextFieldUiState(
            text = "",
            error = false
        )
    )
        private set

    fun updateApiHash(input: String) {
        val hash = input.trim()
        var isError = false
        var error: Int? = null

        when (val isValid = SolusServerValidator.isValidApiHash(hash)) {
            is Result.Error -> {
                isError = true
                if (isValid.error == ValidationErrors.SolusValidationErrors.EMPTY_ENTRY)
                    error = R.string.empty_error
                else
                    error = R.string.invalid_entry_error
            }
            is Result.Success -> {}
        }

        apiHashUiState = apiHashUiState.copy(
            text = hash,
            error = isError,
            resId = error
        )

    }

    var hostUiState by mutableStateOf(
        state.get<TextFieldUiState>(SavedStateKeys.HOST) ?: TextFieldUiState(
            text = DefaultValues.HOST,
            error = false
        )
    )
        private set

    fun updateHost(input: String) {
        val host = input.trim()
        var isError = false
        var error: Int? = null
        //Todo add regex to validate domain

        if (host.isBlank()) {
            isError = true
            error = R.string.empty_error
        } else if (host.contains("")) {
            isError = true
            error = R.string.invalid_entry_error
        }

        hostUiState = hostUiState.copy(
            text = host,
            error = isError,
            resId = error
        )
    }

    fun resetValues() {
        updateHttpsFlag(true)
        updateSubDomain(DefaultValues.SUBDOMAIN)
        updateHost(DefaultValues.HOST)
        apiKeyUiState = apiKeyUiState.copy(
            text = "",
            error = false,
            resId = null
        )
        apiHashUiState = apiHashUiState.copy(
            text = "",
            error = false,
            resId = null
        )
        updatePort(DefaultValues.PORT)
        updatePath(DefaultValues.PATH)
    }

    fun saveServer() {
        viewModelScope.launch {
            val result = repository.saveServer(
                SolusServer(
                    requestUrl = pathUiState.text,
                    key = apiKeyUiState.text,
                    hash = apiHashUiState.text

                )
            )
            when (result) {
                is Result.Error -> {
                    _events.send(ServerListEvents.Error(result.error))
                }

                is Result.Success -> {
                    _events.send(ServerListEvents.Success(UiEvents.SERVER_SAVED))
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {

                return HomeViewModel(
                    extras.createSavedStateHandle(),
                    VPSAlly.appContainer.solusRepository
                ) as T
            }
        }
    }

}