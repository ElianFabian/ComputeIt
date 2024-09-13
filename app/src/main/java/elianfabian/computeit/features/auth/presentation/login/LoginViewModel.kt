package elianfabian.computeit.features.auth.presentation.login

import com.zhuinden.simplestack.Backstack
import elianfabian.computeit.features.auth.presentation.register.RegisterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
	private val backstack: Backstack,
) {

	private val _username = MutableStateFlow("")
	val username = _username.asStateFlow()

	private val _password = MutableStateFlow("")
	val password = _password.asStateFlow()


	fun onAction(action: LoginAction) {
		when (action) {
			is LoginAction.EnterUsername -> {
				val username = action.username
				if (username.length >= 16) {
					return
				}
				_username.value = username
			}
			is LoginAction.EnterPassword -> {
				_password.value = action.password
			}
			is LoginAction.ClickLogin -> {

			}
			is LoginAction.ClickDontHaveAnAccountYet -> {
				backstack.goTo(RegisterKey)
			}
			is LoginAction.ClickSignInWithEmail -> {

			}
			is LoginAction.ClickSignInWithGoogle -> {

			}
			is LoginAction.ClickContinueAsAGuest -> {

			}
		}
	}
}
