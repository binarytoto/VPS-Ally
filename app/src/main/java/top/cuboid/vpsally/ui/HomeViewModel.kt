package top.cuboid.vpsally.ui

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import top.cuboid.vpsally.R
import top.cuboid.vpsally.data.Server
import top.cuboid.vpsally.data.SolusRepositoryImpl
import top.cuboid.vpsally.data.local.SolusServer
import top.cuboid.vpsally.di.VPSAlly
import top.cuboid.vpsally.domain.DataErrors
import top.cuboid.vpsally.domain.Result

object SavedStateKeys {
    const val PORT = "port"
    const val USE_HTTPS = "useHttps"
    const val SUB_DOMAIN = "subdomain"
    const val HOST = "host"
    const val SHOW_SHEET = "showSheet"
    const val PATH = "path"
    const val API_KEY = "api_key"
    const val API_HASH = "api_hash"
    const val HOME_UI = "home_ui"
}

object DefaultValues {
    const val SUBDOMAIN = "solusvm"
    const val HOST = "example.com"
    const val PORT = "5656"
    const val PATH = "api/client/command.php"
}

@Parcelize
data class HomeUiState(
    val servers: List<Server> = emptyList(),
    val isLoading: Boolean = false,
    @StringRes val userMessage: Int? = null
) : Parcelable

@Parcelize
data class TextFieldUiState(
    val text: String,
    val error: Boolean,
    @StringRes val resId: Int? = null
) : Parcelable

class HomeViewModel(
    private val state: SavedStateHandle,
    private val repository: SolusRepositoryImpl
) : ViewModel() {

    var httpsChecked by mutableStateOf(
        state.get<Boolean>(SavedStateKeys.USE_HTTPS) ?: true
    )
        private set

    var uiState by mutableStateOf(
        state.get<HomeUiState>(SavedStateKeys.HOME_UI) ?: HomeUiState()
    )

    fun updateHttpsFlag(useHttps: Boolean) {
        httpsChecked = useHttps
        state[SavedStateKeys.USE_HTTPS] = useHttps
    }

    val showSheet = state.getStateFlow(SavedStateKeys.SHOW_SHEET, false)

    fun updateSheetVisibility(showSheet: Boolean) {
        state[SavedStateKeys.SHOW_SHEET] = showSheet
    }

    var subDomainUiState by mutableStateOf(
        state.get<TextFieldUiState>(SavedStateKeys.SUB_DOMAIN) ?: TextFieldUiState(
            text = DefaultValues.SUBDOMAIN,
            error = false,
        )
    )
        private set

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

        if (port.isBlank()) {
            isError = true
            error = R.string.empty_error
        }else if(!port.isDigitsOnly()) {
            isError = true
            error = R.string.only_digits_error
        } else if (port.toInt() !in 1..65535){
            isError = true
            error = R.string.invalid_entry_error
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
        val key = input.trim()
        var isError = false
        var error:Int? = null
        //TODO regex to exclude Whitespaces and -

        if (key.isBlank()){
            isError = true
            error = R.string.empty_error
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
        //TODO regex to exclude Whitespaces and -

        if (hash.isBlank()) {
            isError = true
            error = R.string.empty_error
        } else if(hash.contains("")) {
            isError = true
            error = R.string.invalid_entry_error
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
        var error:Int? = null
        //Todo add regex to validate domain

        if (host.isBlank()) {
            isError = true
            error = R.string.empty_error
        } else if(host.contains("")) {
            isError = true
            error = R.string.invalid_entry_error
        }

        hostUiState = hostUiState.copy(
            text = host,
            error = isError,
            resId =  error
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
            val result =  repository.saveServer(SolusServer(
                requestUrl = pathUiState.text,
                key = apiKeyUiState.text,
                hash = apiHashUiState.text

            ))
            println("hey ")
            uiState = when (result) {
                is Result.Error -> {

                    if (result.error == DataErrors.Local.SQL_ERROR) {
                        uiState.copy(userMessage = R.string.server_save_error)
                    } else {
                        uiState.copy(userMessage = R.string.unknown_error)
                    }
                }

                is Result.Success -> {
                    uiState.copy(userMessage = R.string.server_saved)
                }
            }
        }
    }

    fun userMessageShown() {
        uiState = uiState.copy(userMessage = null)
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