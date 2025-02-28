package top.cuboid.vpsally.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.cuboid.vpsally.R
import top.cuboid.vpsally.presentation.models.ServerStatus
import top.cuboid.vpsally.presentation.models.ServerUi
import top.cuboid.vpsally.presentation.models.toDisplayableMemoryNum
import top.cuboid.vpsally.ui.theme.VPSAllyTheme

@Composable
fun ServerListItem(
    server: ServerUi,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(start = 5.dp)
        ) {

            Text(
                text = server.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row {

                ServerValueDetail(
                    redId = R.string.bandwidth,
                    value = server.usedBw.formatted
                )

                ServerValueDetail(
                    redId = R.string.storage,
                    value = server.usedStorage.formatted,
                )
            }

            ServerValueDetail(
                redId = R.string.memory,
                value = server.usedMem.formatted,
            )
        }
        //TODO find icon for status
        Icon(
            modifier = Modifier.padding(10.dp),
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = if (server.serverStatus.equals(ServerStatus.ONLINE)) Color.Green else Color.Red,
        )
    }

}

@PreviewLightDark
@Composable
private fun ServerListItemPreview() {

    VPSAllyTheme {
        ServerListItem(
            server = previewServer,
            modifier = Modifier
        )
    }

}

internal val previewServer = ServerUi(
    name = "Gullo 1",
    id = 1,
    ip = "127.0.0.1",
    serverStatus = ServerStatus.ONLINE,
    totalStorage = 2500L.toDisplayableMemoryNum(),
    usedStorage = 2500L.toDisplayableMemoryNum(),
    totalMem = 2500L.toDisplayableMemoryNum(),
    usedMem = 2500L.toDisplayableMemoryNum(),
    usedBw = 2500L.toDisplayableMemoryNum(),
    totalBw = 2500L.toDisplayableMemoryNum(),
)