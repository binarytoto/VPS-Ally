package top.cuboid.vpsally

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import top.cuboid.vpsally.domain.DataErrors
import top.cuboid.vpsally.presentation.server_list.AddServerModalSheet
import top.cuboid.vpsally.presentation.server_list.HomeViewModel
import top.cuboid.vpsally.presentation.server_list.ServerListEvents
import top.cuboid.vpsally.presentation.server_list.ServerListScreen
import top.cuboid.vpsally.presentation.util.ObserveAsEvents
import top.cuboid.vpsally.presentation.util.toStringId
import top.cuboid.vpsally.ui.theme.VPSAllyTheme

class HomeActivity : ComponentActivity() {

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VPSAllyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val vm: HomeViewModel by viewModels { HomeViewModel.Factory }
                    val showSheet = vm.showSheet.collectAsStateWithLifecycle()
                    val context = LocalContext.current
                    ObserveAsEvents(vm.events) { event ->
                        when (event) {
                            is ServerListEvents.Error -> {
                                when (event.error) {
                                    DataErrors.Network.NO_INTERNET -> TODO()
                                    else -> {
                                        Toast.makeText(
                                            context,
                                            context.getString(event.error.toStringId()),
                                            Toast.LENGTH_LONG
                                        ).show()

                                    }
                                }
                            }

                            is ServerListEvents.Success -> {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.server_saved),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    ServerListScreen(
                        serverList = emptyList(),
                        isLoading = vm.isLoading,
                        showAddServerSheet = vm::updateSheetVisibility,
                        modifier = Modifier.padding(innerPadding)
                    )

                    if (showSheet.value) {
                        AddServerModalSheet(
                            onDismiss = vm::updateSheetVisibility,
                            isHttpsChecked = vm.httpsChecked,
                            onHttpsCheck = vm::updateHttpsFlag,
                            subDomainUiState = vm.subDomainUiState,
                            onSubDomainUpdate = vm::updateSubDomain,
                            hostUiState = vm.hostUiState,
                            onHostUpdate = vm::updateHost,
                            apiKeyUiState = vm.apiKeyUiState,
                            onApiKeyUpdate = vm::updateApiKey,
                            apiHashUiState = vm.apiHashUiState,
                            onApiHashUpdate = vm::updateApiHash,
                            resetValues = vm::resetValues,
                            updateSheetVisibility = vm::updateSheetVisibility,
                            saveServer = vm::saveServer,
                            portUiState = vm.portUiState,
                            updatePort = vm::updatePort,
                            pathUiState = vm.pathUiState,
                            updatePath = vm::updatePath,
                        )

                    }
                }
            }

        }
    }
}
