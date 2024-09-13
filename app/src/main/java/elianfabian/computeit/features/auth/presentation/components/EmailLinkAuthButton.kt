package elianfabian.computeit.features.auth.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import elianfabian.computeit.R
import elianfabian.computeit.common.util.simplestack.compose.BasePreview


@Composable
fun EmailLinkAuthButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	BaseAuthButton(
		text = text,
		onClick = onClick,
		iconStartResId = R.drawable.ic_email_outlined_w300,
		modifier = modifier,
	)
}


@PreviewLightDark
@Composable
private fun Preview() = BasePreview {
	EmailLinkAuthButton(
		text = stringResource(R.string.SignUpWithPassword),
		onClick = {},
	)
}
