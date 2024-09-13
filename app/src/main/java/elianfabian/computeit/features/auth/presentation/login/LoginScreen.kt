package elianfabian.computeit.features.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import elianfabian.computeit.R
import elianfabian.computeit.common.util.simplestack.compose.BasePreview
import elianfabian.computeit.features.auth.presentation.components.ContinueAsAGuestButton
import elianfabian.computeit.features.auth.presentation.components.EmailLinkAuthButton
import elianfabian.computeit.features.auth.presentation.components.EmailPasswordAuthButton
import elianfabian.computeit.features.auth.presentation.components.GoogleAuthButton

@Composable
fun LoginScreen(
	onAction: (action: LoginAction) -> Unit,
) {
	Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
			.padding(
				bottom = WindowInsets.safeDrawing
					.asPaddingValues()
					.calculateBottomPadding()
			)
			.verticalScroll(rememberScrollState())
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.fillMaxSize()
		) {
			EmailLinkAuthButton(
				text = stringResource(R.string.SignInWithLink),
				onClick = {
					onAction(LoginAction.ClickSignInWithEmail)
				},
			)
			EmailPasswordAuthButton(
				text = stringResource(R.string.SignInWithPassword),
				onClick = {
					onAction(LoginAction.ClickSignInWithEmail)
				},
			)
			GoogleAuthButton(
				text = stringResource(R.string.SignInWithGoogle),
				onClick = {
					onAction(LoginAction.ClickSignInWithGoogle)
				},
			)
			ContinueAsAGuestButton(
				onClick = {
					onAction(LoginAction.ClickContinueAsAGuest)
				},
				modifier = Modifier
					.fillMaxWidth()
			)
		}
	}
}


@PreviewLightDark
@Composable
private fun Preview() = BasePreview {
	LoginScreen(
		onAction = {},
	)
}
