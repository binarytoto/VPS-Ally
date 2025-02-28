package top.cuboid.vpsally.presentation.models

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextFieldUiState(
    val text: String,
    val error: Boolean,
    @StringRes val resId: Int? = null
) : Parcelable
