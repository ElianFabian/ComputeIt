@file:JvmName("EmulatorDetector")

package elianfabian.computeit.common.util

import android.opengl.GLES20
import android.os.Build
import android.os.Environment
import java.io.File

// From: https://github.com/gingo/android-emulator-detector/blob/master/EmulatorDetectorProject/EmulatorDetector/src/main/java/net/skoumal/emulatordetector/EmulatorDetector.java

private var rating = -1

/**
 * Detects if app is currently running on emulator, or real device.
 *
 * @return true for emulator, false for real devices
 */
fun isEmulator(): Boolean {
	var newRating = 0
	if (rating >= 0) {
		return rating > 3
	}

	if (Build.PRODUCT.contains("sdk") ||
		Build.PRODUCT.contains("Andy") ||
		Build.PRODUCT.contains("ttVM_Hdragon") ||
		Build.PRODUCT.contains("google_sdk") ||
		Build.PRODUCT.contains("Droid4X") ||
		Build.PRODUCT.contains("nox") ||
		Build.PRODUCT.contains("sdk_x86") ||
		Build.PRODUCT.contains("sdk_google") ||
		Build.PRODUCT.contains("vbox86p")
	) {
		newRating++
	}

	if (Build.MANUFACTURER == "unknown" || Build.MANUFACTURER == "Genymotion" ||
		Build.MANUFACTURER.contains("Andy") ||
		Build.MANUFACTURER.contains("MIT") ||
		Build.MANUFACTURER.contains("nox") ||
		Build.MANUFACTURER.contains("TiantianVM")
	) {
		newRating++
	}

	if (Build.BRAND == "generic" || Build.BRAND == "generic_x86" || Build.BRAND == "TTVM" ||
		Build.BRAND.contains("Andy")
	) {
		newRating++
	}

	if (Build.DEVICE.contains("generic") ||
		Build.DEVICE.contains("generic_x86") ||
		Build.DEVICE.contains("Andy") ||
		Build.DEVICE.contains("ttVM_Hdragon") ||
		Build.DEVICE.contains("Droid4X") ||
		Build.DEVICE.contains("nox") ||
		Build.DEVICE.contains("generic_x86_64") ||
		Build.DEVICE.contains("vbox86p")
	) {
		newRating++
	}

	if (Build.MODEL == "sdk" || Build.MODEL == "google_sdk" ||
		Build.MODEL.contains("Droid4X") ||
		Build.MODEL.contains("TiantianVM") ||
		Build.MODEL.contains("Andy") || Build.MODEL == "Android SDK built for x86_64" || Build.MODEL == "Android SDK built for x86"
	) {
		newRating++
	}

	if (Build.HARDWARE == "goldfish" || Build.HARDWARE == "vbox86" ||
		Build.HARDWARE.contains("nox") ||
		Build.HARDWARE.contains("ttVM_x86")
	) {
		newRating++
	}

	if (Build.FINGERPRINT.contains("generic/sdk/generic") ||
		Build.FINGERPRINT.contains("generic_x86/sdk_x86/generic_x86") ||
		Build.FINGERPRINT.contains("Andy") ||
		Build.FINGERPRINT.contains("ttVM_Hdragon") ||
		Build.FINGERPRINT.contains("generic_x86_64") ||
		Build.FINGERPRINT.contains("generic/google_sdk/generic") ||
		Build.FINGERPRINT.contains("vbox86p") ||
		Build.FINGERPRINT.contains("generic/vbox86p/vbox86p")
	) {
		newRating++
	}

	try {
		val opengl = GLES20.glGetString(GLES20.GL_RENDERER)
		if (opengl != null) {
			if (opengl.contains("Bluestacks") ||
				opengl.contains("Translator")
			) newRating += 10
		}
	}
	catch (e: Exception) {
		e.printStackTrace()
	}

	try {
		val sharedFolder = File(
			Environment
				.getExternalStorageDirectory().toString()
				+ File.separatorChar
				+ "windows"
				+ File.separatorChar
				+ "BstSharedFolder"
		)

		if (sharedFolder.exists()) {
			newRating += 10
		}
	}
	catch (e: Exception) {
		e.printStackTrace()
	}
	rating = newRating

	return rating > 3
}

fun isRealDevice() = !isEmulator()
