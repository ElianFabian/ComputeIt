@file:Suppress("NOTHING_TO_INLINE")

package elianfabian.computeit.common.util

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.ktx.Firebase
import timber.log.Timber

fun setupTimber() {
	if (AppBuildConfig.IsDebug || (AppBuildConfig.Flavors.IsDevelopment && AppBuildConfig.Firebase.Environment.IsDevelopment)) {
		Timber.plant(Timber.DebugTree())
		return
	}
	Timber.plant(FirebaseCrashlyticsTree())
}

private class FirebaseCrashlyticsTree : Timber.Tree() {

	override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
		val crashlytics = Firebase.crashlytics

		crashlytics.setCustomKeys {
			if (tag != null) {
				key(TagKey, tag)
			}
			key(PriorityKey, priorityToString(priority))
		}

		if (t != null) {
			crashlytics.recordException(t)
		}
		else {
			crashlytics.recordException(NonFatalCrashlyticsException(message))
		}
	}

	override fun isLoggable(tag: String?, priority: Int): Boolean {
		return priority >= Log.WARN
	}


	companion object {
		const val PriorityKey = "priority"
		const val TagKey = "tag"
	}
}


private class NonFatalCrashlyticsException(message: String) : Exception(message)


private fun priorityToString(priority: Int) = when (priority) {
	Log.VERBOSE -> "Verbose"
	Log.DEBUG -> "Debug"
	Log.INFO -> "Info"
	Log.WARN -> "Warn"
	Log.ERROR -> "Error"
	Log.ASSERT -> "WTF"
	else -> "Unknown"
}


inline fun logVerbose(throwable: Throwable) {
	Timber.v(throwable)
}

inline fun logDebug(throwable: Throwable) {
	Timber.d(throwable)
}

inline fun logInfo(throwable: Throwable) {
	Timber.i(throwable)
}

inline fun logWarning(throwable: Throwable) {
	Timber.w(throwable)
}

inline fun logError(throwable: Throwable) {
	Timber.e(throwable)
}

inline fun logWtf(throwable: Throwable) {
	Timber.wtf(throwable)
}

inline fun logVerbose(message: String) {
	Timber.v(message)
}

inline fun logDebug(message: String) {
	Timber.d(message)
}

inline fun logInfo(message: String) {
	Timber.i(message)
}

inline fun logWarning(message: String) {
	Timber.w(message)
}

inline fun logError(message: String) {
	Timber.e(message)
}

inline fun logWtf(message: String) {
	Timber.wtf(message)
}

inline fun logVerbose(throwable: Throwable, message: String) {
	Timber.v(throwable, message)
}

inline fun logDebug(throwable: Throwable, message: String) {
	Timber.d(throwable, message)
}

inline fun logInfo(throwable: Throwable, message: String) {
	Timber.i(throwable, message)
}

inline fun logWarning(throwable: Throwable, message: String) {
	Timber.w(throwable, message)
}

inline fun logError(throwable: Throwable, message: String) {
	Timber.e(throwable, message)
}

inline fun logWtf(throwable: Throwable, message: String) {
	Timber.wtf(throwable, message)
}
