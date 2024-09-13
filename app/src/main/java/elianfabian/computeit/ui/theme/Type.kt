package elianfabian.computeit.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import elianfabian.computeit.R
import elianfabian.computeit.common.util.simplestack.compose.BasePreview

private val redditMonoFontFamily = FontFamily(
	Font(R.font.redditmono_extralight, FontWeight.ExtraLight),
	Font(R.font.redditmono_light, FontWeight.Light),
	Font(R.font.redditmono_regular, FontWeight.Normal),
	Font(R.font.redditmono_medium, FontWeight.Medium),
	Font(R.font.redditmono_semibold, FontWeight.SemiBold),
	Font(R.font.redditmono_bold, FontWeight.Bold),
	Font(R.font.redditmono_extrabold, FontWeight.ExtraBold),
	Font(R.font.redditmono_black, FontWeight.Black),
)

private val nunitoFontFamily = FontFamily(
	Font(R.font.nunito_extralight, FontWeight.ExtraLight),
	Font(R.font.nunito_extralight_italic, FontWeight.ExtraLight, FontStyle.Italic),
	Font(R.font.nunito_light, FontWeight.Light),
	Font(R.font.nunito_light_italic, FontWeight.Light, FontStyle.Italic),
	Font(R.font.nunito_regular, FontWeight.Normal),
	Font(R.font.nunito_regular_italic, FontWeight.Normal, FontStyle.Italic),
	Font(R.font.nunito_medium, FontWeight.Medium),
	Font(R.font.nunito_medium_italic, FontWeight.Medium, FontStyle.Italic),
	Font(R.font.nunito_bold, FontWeight.Bold),
	Font(R.font.nunito_bold_italic, FontWeight.Bold, FontStyle.Italic),
	Font(R.font.nunito_extrabold, FontWeight.ExtraBold),
	Font(R.font.nunito_extrabold_italic, FontWeight.ExtraBold, FontStyle.Italic),
	Font(R.font.nunito_black, FontWeight.Black),
	Font(R.font.nunito_black_italic, FontWeight.Black, FontStyle.Italic),
)

private val baseline = Typography()

val Typography = baseline.run {
	val fontFamily = nunitoFontFamily

	Typography(
		displayLarge = displayLarge.copy(fontFamily = fontFamily),
		displayMedium = displayMedium.copy(fontFamily = fontFamily),
		displaySmall = displaySmall.copy(fontFamily = fontFamily),
		headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
		headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
		headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
		titleLarge = titleLarge.copy(fontFamily = fontFamily),
		titleMedium = titleMedium.copy(fontFamily = fontFamily),
		titleSmall = titleSmall.copy(fontFamily = fontFamily),
		bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
		bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
		bodySmall = bodySmall.copy(fontFamily = fontFamily),
		labelLarge = labelLarge.copy(fontFamily = fontFamily),
		labelMedium = labelMedium.copy(fontFamily = fontFamily),
		labelSmall = labelSmall.copy(fontFamily = fontFamily),
	)
}

val TypographyMono = baseline.run {
	val fontFamily = redditMonoFontFamily

	Typography(
		displayLarge = displayLarge.copy(fontFamily = fontFamily),
		displayMedium = displayMedium.copy(fontFamily = fontFamily),
		displaySmall = displaySmall.copy(fontFamily = fontFamily),
		headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
		headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
		headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
		titleLarge = titleLarge.copy(fontFamily = fontFamily),
		titleMedium = titleMedium.copy(fontFamily = fontFamily),
		titleSmall = titleSmall.copy(fontFamily = fontFamily),
		bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
		bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
		bodySmall = bodySmall.copy(fontFamily = fontFamily),
		labelLarge = labelLarge.copy(fontFamily = fontFamily),
		labelMedium = labelMedium.copy(fontFamily = fontFamily),
		labelSmall = labelSmall.copy(fontFamily = fontFamily),
	)
}


@Preview(showBackground = true)
@Composable
private fun Preview() = BasePreview {
	val appName = stringResource(R.string.const__AppName)
	Column(Modifier.fillMaxSize()) {
		Text("1. $appName", style = Typography.displayLarge)
		Text("2. $appName", style = Typography.displayMedium)
		Text("3. $appName", style = Typography.displaySmall)
		Text("4. $appName", style = Typography.headlineLarge)
		Text("5. $appName", style = Typography.headlineMedium)
		Text("6. $appName", style = Typography.headlineSmall)
		Text("7. $appName", style = Typography.titleLarge)
		Text("8. $appName", style = Typography.titleMedium)
		Text("9. $appName", style = Typography.titleSmall)
		Text("10. $appName", style = Typography.bodyLarge)
		Text("11. $appName", style = Typography.bodyMedium)
		Text("12. $appName", style = Typography.bodySmall)
		Text("13. $appName", style = Typography.labelLarge)
		Text("14. $appName", style = Typography.labelMedium)
		Text("15. $appName", style = Typography.labelSmall)
		Spacer(Modifier.height(32.dp))
		Text("+1234567890", style = TypographyMono.displaySmall.copy(fontWeight = FontWeight.Medium))
		Text("-0987654321", style = TypographyMono.displaySmall.copy(fontWeight = FontWeight.Medium))
	}
}
