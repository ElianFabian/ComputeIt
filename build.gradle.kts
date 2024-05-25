buildscript {
	repositories {
		google()
		mavenCentral()
		maven { setUrl("https://jitpack.io") }
	}
}

plugins {
	alias(libs.plugins.androidApplication) apply false
	alias(libs.plugins.jetbrainsKotlinAndroid) apply false
	alias(libs.plugins.composeCompiler) apply false
	alias(libs.plugins.kotlinParcelize) apply false
	alias(libs.plugins.jetbrainsKotlinJvm) apply false
	alias(libs.plugins.googleServices) apply false
	alias(libs.plugins.crashlytics) apply false
}
