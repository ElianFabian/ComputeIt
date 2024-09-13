package elianfabian.computeit.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Dp.toPixel() = with(LocalDensity.current) { this@toPixel.toPx() }

@Composable
fun pixelsToDp(pixels: Float): Dp {
	return (pixels / LocalContext.current.resources.displayMetrics.density).dp
}
