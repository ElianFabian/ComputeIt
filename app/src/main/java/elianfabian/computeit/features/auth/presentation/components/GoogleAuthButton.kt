package elianfabian.computeit.features.auth.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import elianfabian.computeit.R
import elianfabian.computeit.common.util.simplestack.compose.BasePreview

@Composable
fun GoogleAuthButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	BaseAuthButton(
		text = text,
		onClick = onClick,
		iconStartResId = R.drawable.ic_google,
		iconTint = Color.Unspecified,
		modifier = modifier
	)
}

@PreviewLightDark
@Composable
private fun Preview() = BasePreview {
	GoogleAuthButton(
		text = stringResource(R.string.SignUpWithGoogle),
		onClick = {},
	)
}
