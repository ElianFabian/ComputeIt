package elianfabian.computeit.features.auth.presentation.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import elianfabian.computeit.R
import elianfabian.computeit.common.util.simplestack.compose.BasePreview
import elianfabian.computeit.ui.theme.ComputeItTheme

@Composable
fun ContinueAsAGuestButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	BaseAuthButton(
		text = stringResource(R.string.ContinueAsAGuest),
		iconStartResId = R.drawable.ic_incognito,
		onClick = onClick,
		border = null,
		colors = ButtonDefaults.buttonColors().copy(
			containerColor = ComputeItTheme.colorScheme.tertiary,
		),
		iconTint = ComputeItTheme.colorScheme.onTertiary,
		textColor = ComputeItTheme.colorScheme.onTertiary,
		modifier = modifier
	)
}


@PreviewLightDark
@Composable
private fun Preview() = BasePreview {
	ContinueAsAGuestButton(onClick = { })
}
