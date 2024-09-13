package elianfabian.computeit.features.auth.presentation.login

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import elianfabian.computeit.common.util.simplestack.FragmentKey
import elianfabian.computeit.common.util.simplestack.compose.ComposeKeyedFragment
import elianfabian.computeit.common.util.simplestack.compose.rememberService
import elianfabian.computeit.features.auth.di.LoginModule
import kotlinx.parcelize.Parcelize

@Parcelize
data object LoginKey : FragmentKey(
	serviceModule = LoginModule,
) {
	override fun instantiateFragment() = LoginFragment()


	class LoginFragment : ComposeKeyedFragment() {

		@Composable
		override fun Content(innerPadding: PaddingValues) {

			val viewModel: LoginViewModel = rememberService()

			val username by viewModel.username.collectAsStateWithLifecycle()
			val password by viewModel.password.collectAsStateWithLifecycle()

			LoginScreen(
				onAction = viewModel::onAction,
			)
		}
	}
}
