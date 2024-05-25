package elianfabian.computeit.common.util.simplestack.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.zhuinden.simplestack.ScopeKey
import com.zhuinden.simplestack.ScopeLookupMode

// Source: https://github.com/Zhuinden/simple-stack-compose-integration/blob/master/services/src/main/java/com/zhuinden/simplestackcomposeintegration/services/ComposeIntegrationServices.kt

/**
 * Helper function to remember a service looked up from the backstack.
 */
@Composable
inline fun <reified T> rememberService(serviceTag: String = T::class.java.name): T {
	val backstack = LocalBackstack.current

	return remember { backstack.lookupService(serviceTag) }
}

/**
 * Helper function to remember a service looked up from the backstack from a specific scope with the given scope lookup mode.
 */
@Composable
inline fun <reified T> rememberServiceFrom(
	scopeTag: String,
	serviceTag: String = T::class.java.name,
	scopeLookupMode: ScopeLookupMode = ScopeLookupMode.ALL,
): T {
	val backstack = LocalBackstack.current

	return remember { backstack.lookupFromScope(scopeTag, serviceTag, scopeLookupMode) }
}

/**
 * Helper function to remember a service looked up from the backstack from a specific scope with the given scope lookup mode.
 */
@Composable
inline fun <reified T> rememberServiceFrom(
	scopeKey: ScopeKey,
	serviceTag: String = T::class.java.name,
	scopeLookupMode: ScopeLookupMode = ScopeLookupMode.ALL,
): T {
	return rememberServiceFrom(
		scopeTag = scopeKey.scopeTag,
		serviceTag = serviceTag,
		scopeLookupMode = scopeLookupMode,
	)
}
