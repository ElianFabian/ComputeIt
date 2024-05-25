package elianfabian.computeit.common.util

import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import java.util.Locale

fun Application.createLocaleContextWrapper(baseContext: Context): ContextWrapper {
	return LocaleContextWrapper(
		application = this,
		baseContext = baseContext,
	)
}


class LocaleContextWrapper(
	application: Application,
	baseContext: Context,
) : ContextWrapper(
	baseContext.createContextWithFirstSupportedUserLocaleOrForceDefault(baseContext.resources.configuration)
) {
	init {
		application.registerComponentCallbacks(object : ComponentCallbacks {
			override fun onConfigurationChanged(newConfig: Configuration) {
				_baseContext = _baseContext.createContextWithFirstSupportedUserLocaleOrForceDefault(newConfig)
			}

			override fun onLowMemory() = Unit
		})
	}


	private var _baseContext = super.getBaseContext()


	override fun getResources(): Resources {
		return _baseContext.resources
	}

	override fun getTheme(): Resources.Theme {
		return _baseContext.theme
	}

	override fun getBaseContext(): Context {
		return _baseContext
	}

	override fun setTheme(resid: Int) {
		_baseContext.setTheme(resid)
	}
}

private fun Context.createContextWithFirstSupportedUserLocaleOrForceDefault(configuration: Configuration): Context {

	val userSupportedLocales = ConfigurationCompat
		.getLocales(configuration)
		.getMatches(AppBuildConfig.SupportedLanguages)

	val newLocales = userSupportedLocales.ifEmpty {
		listOf(Locale(AppBuildConfig.DefaultLanguage))
	}

	val newLocaleList = LocaleList(*newLocales.toTypedArray())
	LocaleList.setDefault(newLocaleList)
	configuration.setLocales(newLocaleList)

	return createConfigurationContext(configuration)
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
