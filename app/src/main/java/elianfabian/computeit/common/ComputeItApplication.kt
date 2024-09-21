package elianfabian.computeit.common

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.zhuinden.simplestack.Backstack
import elianfabian.computeit.common.util.callback.OnConfigurationChangedCallback
import elianfabian.computeit.common.util.callback.OnMainBackstackIsInitializedCallback
import elianfabian.computeit.common.util.createLocaleContextWrapper
import elianfabian.computeit.common.util.setupTimber
import elianfabian.computeit.common.util.simplestack.forEachServiceOfType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ComputeItApplication : Application(), OnMainBackstackIsInitializedCallback {

	private val applicationScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

	private var _mainBackstack = MutableStateFlow<Backstack?>(null)
	val mainBackstack get() = _mainBackstack.value ?: throw IllegalStateException("Main Backstack is not yet initialized")


	override fun onCreate() {
		super.onCreate()

		setupTimber()
	}

	override fun attachBaseContext(base: Context?) {
		if (base == null) {
			return super.attachBaseContext(null)
		}
		val localeContext = createLocaleContextWrapper(base)
		super.attachBaseContext(localeContext)
	}

	override fun onConfigurationChanged(newConfig: Configuration) {
		super.onConfigurationChanged(newConfig)

		applicationScope.launch {
			val backstack = _mainBackstack.filterNotNull().first()

			backstack.forEachServiceOfType<OnConfigurationChangedCallback> { service ->
				service.onConfigurationChanged(newConfig)
			}
		}
	}

	override fun onMainBackstackIsInitialized(backstack: Backstack) {
		_mainBackstack.value = backstack
	}


	companion object {
		val Context.mainBackstack: Backstack get() = (applicationContext as ComputeItApplication).mainBackstack
	}
}
