package elianfabian.computeit.features.auth.presentation.login

sealed interface LoginAction {
	data class EnterUsername(val username: String) : LoginAction
	data class EnterPassword(val password: String) : LoginAction
	data object ClickLogin : LoginAction
	data object ClickDontHaveAnAccountYet : LoginAction
	data object ClickSignInWithEmail : LoginAction
	data object ClickSignInWithGoogle : LoginAction
	data object ClickContinueAsAGuest : LoginAction
}
