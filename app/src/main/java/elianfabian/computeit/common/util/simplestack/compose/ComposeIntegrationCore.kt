package elianfabian.computeit.common.util.simplestack.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.zhuinden.simplestack.Backstack

// Source: https://github.com/Zhuinden/simple-stack-compose-integration/blob/master/core/src/main/java/com/zhuinden/simplestackcomposeintegration/core/ComposeIntegrationCore.kt

/**
 * Composition local to access the Backstack within screens.
 */
val LocalBackstack =
	staticCompositionLocalOf<Backstack> {
		throw IllegalStateException("You must ensure that the BackstackProvider provides the backstack, but it currently doesn't exist.")
	}

/**
 * Provider for the backstack composition local.
 */
@Composable
fun BackstackProvider(backstack: Backstack, content: @Composable () -> Unit) {
	CompositionLocalProvider(LocalBackstack provides (backstack)) {
		content()
	}
}
