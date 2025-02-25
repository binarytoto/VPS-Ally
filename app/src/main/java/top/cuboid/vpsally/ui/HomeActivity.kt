package top.cuboid.vpsally.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.cuboid.vpsally.ui.theme.VPSAllyTheme
import top.cuboid.vpsally.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class HomeActivity : ComponentActivity() {

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
            val scope = rememberCoroutineScope()
            var showAdvancedSettings by rememberSaveable { mutableStateOf(false) }
            val snackBarHostState = remember { SnackbarHostState() }
            val vm: HomeViewModel by viewModels { HomeViewModel.Factory }
            val showSheet = vm.showSheet.collectAsStateWithLifecycle()
            val httpsChecked = vm.httpsChecked
            val subDomainUiState = vm.subDomainUiState
            val portUiState = vm.portUiState
            val pathUiState = vm.pathUiState
            val apiKeyUiState = vm.apiKeyUiState
            val apiHashUiState = vm.apiHashUiState
            val hostUiState = vm.hostUiState
            val finalUrl ="${if (httpsChecked) "https://" else "http://"}${subDomainUiState.text}.${hostUiState.text}:${portUiState.text}/${pathUiState.text}"
            val uiState = vm.uiState

            val cont = LocalContext.current
            uiState.userMessage?.let {
                Toast.makeText(cont, stringResource(it),Toast.LENGTH_SHORT).show()
                vm.userMessageShown()
            }

            VPSAllyTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    },
                    ) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {

                        Text(
                            text = stringResource(R.string.no_servers),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(10.dp)
                        )

                        Button(
                            onClick = {
                                vm.updateSheetVisibility(true)
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(10.dp)
                        ) {
                            Text(text = stringResource(R.string.add_server))
                        }
                    }

                    if (showSheet.value) {

                        ModalBottomSheet(
                            onDismissRequest = {  vm.updateSheetVisibility(false) },
                            sheetState = sheetState
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {

                                Text(
                                    text = stringResource(R.string.server_details),
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(
                                        dimensionResource(R.dimen.bottom_sheet_content_padding)
                                    )
                                )

                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {

                                OutlinedTextField(
                                    value = finalUrl,
                                    onValueChange = {},
                                    label = { Text(stringResource(R.string.final_url)) },
                                    enabled = false,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding)
                                        )
                                )

                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = stringResource(R.string.use_https),
                                    modifier = Modifier.padding(
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding)
                                    )
                                )

                                Switch(
                                    checked = httpsChecked,
                                    modifier = Modifier.padding(
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding)
                                    ),
                                    onCheckedChange = {
                                        vm.updateHttpsFlag(it)
                                    },
                                    thumbContent = if (httpsChecked) {
                                        {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(SwitchDefaults.IconSize)
                                            )
                                        }
                                    } else {
                                        null
                                    }
                                )

                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Bottom
                            ) {

                                OutlinedTextField(
                                    value = subDomainUiState.text,
                                    onValueChange = {
                                        vm.updateSubDomain(it)

                                    },
                                    label = { Text(stringResource(R.string.sub_domain)) },
                                    singleLine = true,
                                    isError = (subDomainUiState.error),
                                    supportingText =  { subDomainUiState.resId?.let {
                                        Text(stringResource(it))
                                    } },
                                    modifier = Modifier
                                        .padding(
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding)
                                        )
                                        .weight(1f)
                                )

                                Text(
                                    text = ".",
                                    fontSize = 40.sp,
                                    modifier = Modifier.padding(
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding)
                                    )
                                )

                                OutlinedTextField(
                                    value = hostUiState.text,
                                    onValueChange = {
                                       vm.updateHost(it)
                                    },
                                    label = { Text(stringResource(R.string.host)) },
                                    singleLine = true,
                                    isError = hostUiState.error,
                                    supportingText = { hostUiState.resId?.let{
                                        Text(stringResource(it))
                                    } },
                                    modifier = Modifier
                                        .padding(
                                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding)
                                        )
                                        .weight(2f)
                                )

                            }

                            Row(modifier = Modifier.fillMaxWidth()) {

                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth().padding(
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding)
                                    ),
                                    value = apiKeyUiState.text,
                                    onValueChange = {
                                        vm.updateApiKey(it)
                                    },
                                    label = { Text(stringResource(R.string.api_key))},
                                    isError = apiKeyUiState.error,
                                    supportingText = { apiKeyUiState.resId?.let{
                                        Text(stringResource(it))
                                    } }
                                )

                            }

                            Row(modifier = Modifier.fillMaxWidth()) {

                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth().padding(
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding)
                                    ),
                                    value = apiHashUiState.text,
                                    onValueChange = {
                                        vm.updateApiHash(it)
                                    },
                                    label = { Text(stringResource(R.string.hash))},
                                    isError = apiHashUiState.error,
                                    supportingText = { apiHashUiState.resId?.let{
                                        Text(stringResource(it))
                                    } }
                                )

                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {

                                TextButton(
                                    onClick = {
                                        vm.resetValues()
                                        scope.launch {
                                            sheetState.hide()
                                        }.invokeOnCompletion { vm.updateSheetVisibility(false) }
                                    },
                                    modifier = Modifier.padding(
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding)
                                    )
                                ) {
                                    Text(text = stringResource(R.string.cancel))
                                }

                                OutlinedButton(
                                    onClick = {
                                        vm.saveServer()
                                    },
                                    modifier = Modifier.padding(
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_padding)
                                    )
                                ) {
                                    Text(text = stringResource(R.string.add_server))
                                }

                            }

                            Row(modifier = Modifier.fillMaxWidth().clickable(
                                enabled = true,
                                onClick = {
                                    showAdvancedSettings = !showAdvancedSettings
                                }),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Text(
                                    modifier = Modifier.padding(
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                        dimensionResource(R.dimen.bottom_sheet_content_start_padding)
                                    ),
                                    text = stringResource(R.string.advance_settings),

                                )

                                if (showAdvancedSettings) {

                                    Icon(
                                        modifier = Modifier.padding(
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding)
                                        ),
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = null
                                    )

                                } else {
                                    Icon(
                                        modifier = Modifier.padding(
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding)
                                        ),
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null
                                    )
                                }


                            }

                            if (showAdvancedSettings) {

                                Row(modifier = Modifier.fillMaxWidth()) {

                                    OutlinedTextField(
                                        modifier = Modifier.padding(
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding)
                                        ).weight(1f),
                                        value = portUiState.text,
                                        onValueChange = {
                                            vm.updatePort(it)
                                        },
                                        label = { Text(stringResource(R.string.port)) },
                                        isError = vm.portUiState.error,
                                        supportingText = { vm.portUiState.resId?.let {
                                            Text(stringResource(it)
                                        ) }},
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )

                                    OutlinedTextField(
                                        modifier = Modifier.padding(
                                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                                            dimensionResource(R.dimen.bottom_sheet_content_padding)
                                        ).weight(2f),
                                        value = pathUiState.text,
                                        onValueChange = {
                                            vm.updatePath(it)
                                        },
                                        label = { Text(stringResource(R.string.path)) },
                                        singleLine = true,
                                        isError = pathUiState.error,
                                        supportingText = { pathUiState.resId?.let {
                                            Text(stringResource(it))
                                        } }
                                    )

                                }
                            }


                        }
                    }
                }
            }

        }
    }
}
