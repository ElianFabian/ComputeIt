package elianfabian.computeit.features.auth.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import elianfabian.computeit.R
import elianfabian.computeit.common.util.pixelsToDp
import elianfabian.computeit.common.util.simplestack.compose.BasePreview
import elianfabian.computeit.ui.theme.ComputeItTheme

@Composable
fun BaseAuthButton(
	text: String,
	@DrawableRes
	iconStartResId: Int,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	textColor: Color = ComputeItTheme.colorScheme.onSurface,
	colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
	border: BorderStroke? = ButtonDefaults.outlinedButtonBorder,
	iconTint: Color? = ComputeItTheme.colorScheme.onSurface,
) {
	OutlinedButton(
		onClick = onClick,
		colors = colors,
		border = border,
		modifier = modifier
	) {
		Row(
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically,
		) {
			val icon = painterResource(iconStartResId)
			Icon(
				painter = icon,
				tint = iconTint ?: LocalContentColor.current,
				contentDescription = null,
			)

			val iconWidth = pixelsToDp(icon.intrinsicSize.width)
			Text(
				text = text,
				textAlign = TextAlign.Center,
				color = textColor,
				modifier = Modifier
					.weight(1F)
					.offset(x = -iconWidth / 2)
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun Preview() = BasePreview {
	BaseAuthButton(
		text = "Base button",
		iconStartResId = R.drawable.ic_android,
		onClick = {},
	)
}
