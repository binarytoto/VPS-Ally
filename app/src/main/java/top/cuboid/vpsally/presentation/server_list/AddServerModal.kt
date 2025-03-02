package top.cuboid.vpsally.presentation.server_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.cuboid.vpsally.R
import top.cuboid.vpsally.presentation.models.TextFieldUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServerModalSheet(
    onDismiss: (Boolean) -> Unit,
    isHttpsChecked: Boolean,
    onHttpsCheck: (Boolean) -> Unit,
    subDomainUiState: TextFieldUiState,
    onSubDomainUpdate: (String) -> Unit,
    hostUiState: TextFieldUiState,
    onHostUpdate: (String) -> Unit,
    apiKeyUiState: TextFieldUiState,
    onApiKeyUpdate: (String) -> Unit,
    apiHashUiState: TextFieldUiState,
    onApiHashUpdate: (String) -> Unit,
    resetValues: () -> Unit,
    updateSheetVisibility: (Boolean) -> Unit,
    saveServer: () -> Unit,
    portUiState: TextFieldUiState,
    updatePort: (String) -> Unit,
    pathUiState: TextFieldUiState,
    updatePath: (String) -> Unit,
) {


    val finalUrl =
        "${if (isHttpsChecked) "https://" else "http://"}${subDomainUiState.text}.${hostUiState.text}:${portUiState.text}/${pathUiState.text}"
    val scope = rememberCoroutineScope()
    var showAdvancedSettings by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss(false) },
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
                checked = isHttpsChecked,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.bottom_sheet_content_padding),
                    dimensionResource(R.dimen.bottom_sheet_content_padding),
                    dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                    dimensionResource(R.dimen.bottom_sheet_content_padding)
                ),
                onCheckedChange = {
                    onHttpsCheck(it)
                },
                thumbContent = if (isHttpsChecked) {
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
                    onSubDomainUpdate(it)
                },
                label = { Text(stringResource(R.string.sub_domain)) },
                singleLine = true,
                isError = (subDomainUiState.error),
                supportingText = {
                    subDomainUiState.resId?.let {
                        Text(stringResource(it))
                    }
                },
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
                    onHostUpdate(it)
                },
                label = { Text(stringResource(R.string.host)) },
                singleLine = true,
                isError = hostUiState.error,
                supportingText = {
                    hostUiState.resId?.let {
                        Text(stringResource(it))
                    }
                },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                        dimensionResource(R.dimen.bottom_sheet_content_padding)
                    ),
                value = apiKeyUiState.text,
                onValueChange = {
                    onApiKeyUpdate(it)
                },
                label = { Text(stringResource(R.string.api_key)) },
                isError = apiKeyUiState.error,
                supportingText = {
                    apiKeyUiState.resId?.let {
                        Text(stringResource(it))
                    }
                }
            )

        }

        Row(modifier = Modifier.fillMaxWidth()) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                        dimensionResource(R.dimen.bottom_sheet_content_padding),
                        dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                        dimensionResource(R.dimen.bottom_sheet_content_padding)
                    ),
                value = apiHashUiState.text,
                onValueChange = {
                    onApiHashUpdate(it)
                },
                label = { Text(stringResource(R.string.hash)) },
                isError = apiHashUiState.error,
                supportingText = {
                    apiHashUiState.resId?.let {
                        Text(stringResource(it))
                    }
                }
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            TextButton(
                onClick = {
                    resetValues()
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion { updateSheetVisibility(false) }
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
                    saveServer()
                    //TODO: close only after save is successful
                    updateSheetVisibility(false)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = true,
                    onClick = {
                        showAdvancedSettings = !showAdvancedSettings
                    }),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

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
                    modifier = Modifier
                        .padding(
                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                            dimensionResource(R.dimen.bottom_sheet_content_padding)
                        )
                        .weight(1f),
                    value = portUiState.text,
                    onValueChange = {
                        updatePort(it)
                    },
                    label = { Text(stringResource(R.string.port)) },
                    isError = portUiState.error,
                    supportingText = {
                        portUiState.resId?.let {
                            Text(
                                stringResource(it)
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(
                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                            dimensionResource(R.dimen.bottom_sheet_content_padding),
                            dimensionResource(R.dimen.bottom_sheet_content_start_padding),
                            dimensionResource(R.dimen.bottom_sheet_content_padding)
                        )
                        .weight(2f),
                    value = pathUiState.text,
                    onValueChange = {
                        updatePath(it)
                    },
                    label = { Text(stringResource(R.string.path)) },
                    singleLine = true,
                    isError = pathUiState.error,
                    supportingText = {
                        pathUiState.resId?.let {
                            Text(stringResource(it))
                        }
                    }
                )

            }
        }
    }


}

@PreviewLightDark
@Composable
private fun AddServerModalSheetPreview() {

}
