package top.cuboid.vpsally.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import top.cuboid.vpsally.R
import top.cuboid.vpsally.presentation.models.toDisplayableMemoryNum
import top.cuboid.vpsally.ui.theme.VPSAllyTheme

@Composable
fun ServerValueDetail(
    @StringRes redId: Int,
    value: String
) {
    Row(

    ) {
        Text(
            text = stringResource(redId)
        )
        Text(
            text = value,
            modifier = Modifier.padding(end = 5.dp)
        )
    }
}

@PreviewLightDark
@Composable
fun ServerValueDetailPreview() {
    VPSAllyTheme {
        ServerValueDetail(
            R.string.bandwidth,
            2500000L.toDisplayableMemoryNum().formatted,
        )
    }
}