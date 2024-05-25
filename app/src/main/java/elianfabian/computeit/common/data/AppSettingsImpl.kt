package elianfabian.computeit.common.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import elianfabian.computeit.common.domain.AppSettings
import elianfabian.computeit.common.util.AppBuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

class AppSettingsImpl(
	private val dataStore: DataStore<Preferences>,
) : AppSettings {

	init {
		if (AppBuildConfig.IsDebug || AppBuildConfig.Flavors.IsDevelopment) {
			runBlocking {
				saveCurrentFirebaseEnvironment(AppBuildConfig.Firebase.Environment.Current)
			}
		}
	}


	override suspend fun getCurrentFirebaseEnvironment() = get(firebaseEnvironmentKey) ?: throw IllegalStateException("Firebase environment was not set")

	override suspend fun saveCurrentFirebaseEnvironment(value: String) = save(firebaseEnvironmentKey, value)

	override fun firebaseEnvironmentFlow() = getFlow(firebaseEnvironmentKey).filterNotNull()


	private suspend fun <T> save(key: Preferences.Key<T>, value: T?) {
		dataStore.edit { preferences ->
			if (value == null) {
				preferences.remove(key)
				return@edit
			}
			preferences[key] = value
		}
	}

	private suspend fun <T> get(key: Preferences.Key<T>): T? {
		return getFlow(key).first()
	}

	private fun <T> getFlow(key: Preferences.Key<T>): Flow<T?> {
		return dataStore.data
			.catch { e ->
				if (e is IOException) {
					e.printStackTrace()
					emit(emptyPreferences())
				}
				else throw e
			}
			.map { preferences ->
				preferences[key]
			}
	}


	companion object {
		private val firebaseEnvironmentKey = stringPreferencesKey("firebaseEnvironment")
	}
}
