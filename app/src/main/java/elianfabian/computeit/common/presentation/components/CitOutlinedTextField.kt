package elianfabian.computeit.common.presentation.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import elianfabian.computeit.R

@Composable
fun CitOutlinedTextField(
	value: String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	hint: String? = null,
	trailingIcon: (@Composable () -> Unit)? = null,
	isError: Boolean = false,
	visualTransformation: VisualTransformation = VisualTransformation.None,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	minLines: Int = 1,
	maxLines: Int = Int.MAX_VALUE,
) {
	OutlinedTextField(
		value = value,
		onValueChange = onValueChange,
		trailingIcon = trailingIcon,
		minLines = minLines,
		maxLines = maxLines,
		isError = isError,
		label = createTextComposableOrNull(hint),
		shape = CircleShape,
		keyboardOptions = keyboardOptions,
		keyboardActions = keyboardActions,
		visualTransformation = visualTransformation,
		modifier = modifier
	)
}

@Composable
fun CitOutlinedPasswordTextField(
	value: String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	hint: String? = null,
	isError: Boolean = false,
) {
	var isPasswordVisible by remember {
		mutableStateOf(false)
	}

	val visibilityIcon = painterResource(
		if (isPasswordVisible) {
			R.drawable.ic_visibility_off
		}
		else R.drawable.ic_visibility
	)

	CitOutlinedTextField(
		value = value,
		onValueChange = onValueChange,
		hint = hint,
		isError = isError,
		maxLines = 1,
		keyboardOptions = KeyboardOptions(
			keyboardType = KeyboardType.Password,
		),
		trailingIcon = {
			IconButton(
				onClick = {
					isPasswordVisible = !isPasswordVisible
				},
			) {
				Icon(
					painter = visibilityIcon,
					contentDescription = stringResource(R.string.TogglePasswordVisibility),
				)
			}
		},
		visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
		modifier = modifier
	)
}


private fun createTextComposableOrNull(text: String?): (@Composable () -> Unit)? {
	return if (text == null) null
	else {
		{ Text(text) }
	}
}
