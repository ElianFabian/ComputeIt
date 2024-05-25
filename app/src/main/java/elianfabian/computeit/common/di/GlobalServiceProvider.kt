package elianfabian.computeit.common.di

import android.app.Application
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.GlobalServices
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import elianfabian.computeit.common.data.AppSettingsImpl
import elianfabian.computeit.common.data.dataStore
import elianfabian.computeit.common.domain.AppSettings
import elianfabian.computeit.common.util.AppBuildConfig
import elianfabian.computeit.common.util.isRealDevice

class GlobalServiceProvider(
	private val application: Application,
) : GlobalServices.Factory {

	override fun create(backstack: Backstack): GlobalServices {

		val applicationContext: Context = application
		val firestore = provideFirestore()

		val dataStore = applicationContext.dataStore

		val appSettings: AppSettings = AppSettingsImpl(dataStore)

		val globalServices = GlobalServices.builder()
			.add(applicationContext, ApplicationContextTag)
			.add(firestore)
			.add(appSettings)
			.build()

		return globalServices
	}


	private fun provideFirestore(): FirebaseFirestore {
		return Firebase.firestore.apply {
			firestoreSettings = firestoreSettings {
				setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
			}

			if (!AppBuildConfig.Firebase.UseEmulator) {
				return@apply
			}

			val host = if (isRealDevice()) {
				AppBuildConfig.Firebase.EmulatorHost
			}
			else "10.0.2.2"

			useEmulator(host, 8080)
		}
	}
}

private const val TagPrefix = "Tag.GlobalServices"

private const val ApplicationContextTag = "$TagPrefix.ApplicationContext"

fun ServiceBinder.lookupApplicationContext(): Context {
	return lookup(ApplicationContextTag)
}
