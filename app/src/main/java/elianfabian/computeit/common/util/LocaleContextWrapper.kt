package elianfabian.computeit.common.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import java.util.Locale

fun Application.createLocaleContextWrapper(baseContext: Context): ContextWrapper {
	return LocaleContextWrapper(
		application = this,
		baseContext = baseContext,
		createUpdatedLocaleContext = { oldContext, newConfig ->
			createContextWithFirstSupportedUserLocaleOrForceDefault(
				context = oldContext,
				configuration = newConfig,
				defaultLanguage = AppBuildConfig.DefaultLanguage,
				supportedLanguages = AppBuildConfig.SupportedLanguages,
			)
		},
	)
}


class LocaleContextWrapper(
	application: Application,
	baseContext: Context,
	private val createUpdatedLocaleContext: (oldContext: Context, newConfig: Configuration) -> Context,
) : ContextWrapper(
	createUpdatedLocaleContext(baseContext, baseContext.resources.configuration)
) {
	init {
		application.registerComponentCallbacks(object : ComponentCallbacks {
			override fun onConfigurationChanged(newConfig: Configuration) {
				_baseContext = createUpdatedLocaleContext(_baseContext, newConfig)
			}

			override fun onLowMemory() = Unit
		})
	}


	private var _baseContext = super.getBaseContext()


	override fun getResources(): Resources = _baseContext.resources

	override fun getTheme(): Resources.Theme = _baseContext.theme

	override fun getBaseContext(): Context = _baseContext

	override fun setTheme(resid: Int) {
		_baseContext.setTheme(resid)
	}
}

@Suppress("DEPRECATION")
@SuppressLint("ObsoleteSdkInt")
private fun createContextWithFirstSupportedUserLocaleOrForceDefault(
	context: Context,
	configuration: Configuration,
	defaultLanguage: String,
	supportedLanguages: List<String>,
): Context {
	val userSupportedLocales = ConfigurationCompat
		.getLocales(configuration)
		.getMatches(supportedLanguages)

	val newLocales = userSupportedLocales.ifEmpty {
		listOf(Locale(defaultLanguage))
	}

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
		val newLocaleList = LocaleList(*newLocales.toTypedArray())
		LocaleList.setDefault(newLocaleList)
		configuration.setLocales(newLocaleList)
	}
	else {
		val newLocale = newLocales.first()
		Locale.setDefault(newLocale)
		configuration.locale = newLocale
	}

	return context.createConfigurationContext(configuration)
}

private fun LocaleListCompat.getMatches(supportedLocales: List<String>): List<Locale> {
	val currentSupportedLocales = supportedLocales.toMutableList()

	return buildList {
		while (currentSupportedLocales.isNotEmpty()) {
			val match = getFirstMatch(currentSupportedLocales.toTypedArray()) ?: continue
			add(match)
			currentSupportedLocales.removeAll { supportedLocale ->
				match.toString().startsWith(supportedLocale)
			}
		}
	}
}
