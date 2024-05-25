package elianfabian.computeit.common

import android.app.Application
import android.content.Context
import com.zhuinden.simplestack.Backstack
import elianfabian.computeit.common.util.callback.OnMainBackstackIsInitializedCallback
import elianfabian.computeit.common.util.createLocaleContextWrapper
import elianfabian.computeit.common.util.setupTimber

class ComputeItApplication : Application(), OnMainBackstackIsInitializedCallback {

	private var _mainBackstack: Backstack? = null
	val mainBackstack get() = _mainBackstack ?: throw IllegalStateException("Main Backstack is not yet initialized")


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

	override fun onMainBackstackIsInitialized(backstack: Backstack) {
		_mainBackstack = backstack
	}


	companion object {
		val Context.mainBackstack: Backstack get() = (applicationContext as ComputeItApplication).mainBackstack
	}
}
