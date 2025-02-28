package top.cuboid.vpsally.presentation.server_list

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.cuboid.vpsally.R
import top.cuboid.vpsally.presentation.components.ServerListItem
import top.cuboid.vpsally.presentation.components.previewServer
import top.cuboid.vpsally.presentation.models.ServerUi
import top.cuboid.vpsally.ui.theme.VPSAllyTheme

@Composable
fun ServerListScreen(
    serverList: List<ServerUi>,
    isLoading: Boolean,
    showAddServerSheet: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val contentColor =
        if (isSystemInDarkTheme()) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        if (isLoading) {
            CircularProgressIndicator(
                modifier = modifier.align(Alignment.Center)
            )
        } else {

            if (serverList.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_servers),
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(10.dp),
                    color = contentColor
                )

            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize()
                ) {
                    items(serverList) { server ->
                        ServerListItem(
                            server = server,
                            modifier = modifier
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                showAddServerSheet(true)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        ) {
            Text(text = stringResource(R.string.add_server))
        }
    }
}

@PreviewLightDark
@Composable
private fun ServerListScreenPreview() {
    VPSAllyTheme {
        ServerListScreen(
            serverList = (1..10).map {
                previewServer.copy(id = it)
            },
            isLoading = false,
            showAddServerSheet = {},
            modifier = Modifier
        )
    }
}