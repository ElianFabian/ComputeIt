package elianfabian.computeit.common.util.simplestack.compose

import androidx.compose.runtime.Composable
import com.zhuinden.simplestack.Backstack
import elianfabian.computeit.ui.theme.ComputeItTheme

@Composable
fun BasePreview(
	content: @Composable () -> Unit,
) {
	BackstackProvider(backstack = Backstack()) {
		ComputeItTheme {
			content()
		}
	}
}
