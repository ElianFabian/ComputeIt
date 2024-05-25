@file:Suppress("DEPRECATION")

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.util.Locale
import java.util.Properties

plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.jetbrainsKotlinAndroid)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.kotlinParcelize)
	alias(libs.plugins.googleServices)
	alias(libs.plugins.crashlytics)
}

android {
	namespace = "elianfabian.computeit"
	compileSdk = 34

	signingConfigs {
		create("production-release") {
			val keystoreProperties = Properties().apply {
				load(rootProject.file("keystore.properties").inputStream())
			}
			storeFile = file("../ComputeIt-keystore-2024-05-18.jks")
			storePassword = keystoreProperties["StorePassword"] as String
			keyAlias = keystoreProperties["KeyAlias"] as String
			keyPassword = keystoreProperties["KeyPassword"] as String
		}
		getByName("debug") {
			val keystoreProperties = Properties().apply {
				load(rootProject.file("keystore.properties").inputStream())
			}
			storeFile = file("../debug-2024-06-23.keystore")
			storePassword = keystoreProperties["DebugStorePassword"] as String
			keyAlias = keystoreProperties["DebugKeyAlias"] as String
			keyPassword = keystoreProperties["DebugKeyPassword"] as String
		}
	}
	defaultConfig {
		applicationId = "elianfabian.computeit"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "0.0.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}
	productFlavors {
		val environmentDimension = "environment"

		flavorDimensions += listOf(
			environmentDimension,
		)

		create("development") {
			dimension = environmentDimension
			isDefault = true

			applicationIdSuffix = ".dev"
		}
		create("production") {
			dimension = environmentDimension
		}
	}
	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

			productFlavors.getByName("development").signingConfig = signingConfigs.getByName("debug")
			productFlavors.getByName("production").signingConfig = signingConfigs.getByName("production-release")
		}
		debug {
			applicationIdSuffix = ".debug"
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = JavaVersion.VERSION_1_8.toString()
	}
	buildFeatures {
		compose = true
		buildConfig = true
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	lint {
		disable += "ConstPropertyName"
	}

	setupBuildFields()
}

dependencies {

	implementation(libs.firebaseAuthKtx)
	implementation(libs.firebaseFirestoreKtx)
	implementation(libs.firebaseCrashlyticsKtx)
	implementation(platform(libs.firebaseBom))

	implementation(libs.timber)
	implementation(libs.coilCompose)
	implementation(libs.simpleStackExtensions)
	implementation(libs.simpleStack)

	implementation(libs.androidx.dataStore)
	implementation(libs.androidx.appCompat)
	implementation(libs.androidx.coreKtx)
	implementation(libs.androidx.lifecycleRuntimeCompose)
	implementation(libs.androidx.lifecycleRuntimeKtx)
	implementation(libs.androidx.activityCompose)
	implementation(platform(libs.androidx.composeBom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.toolingPreview)
	implementation(libs.androidx.material3)


	testImplementation(libs.junit)

	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espressoCore)
	androidTestImplementation(platform(libs.androidx.composeBom))
	androidTestImplementation(libs.androidx.ui.testJunit4)

	//debugImplementation(libs.leakCanary)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.testManifest)
}


fun BaseAppModuleExtension.setupBuildFields() {
	val firebaseDevProperties = Properties().apply {
		load(rootProject.file("firebase-dev.properties").inputStream())
	}
	val environmentProperty = firebaseDevProperties["FirebaseDevelopmentEnvironment"] as String
	val emulatorHostProperty = firebaseDevProperties["FirebaseEmulatorHost"] as String
	val useEmulatorProperty = (firebaseDevProperties["UseFirebaseEmulator"] as String).toBoolean()

	defaultConfig {
		val defaultLocale = Locale("en")
		val appLocales = getAppLocales(defaultLocale)
		val appLanguages = appLocales.joinToString(",") { locale ->
			"\"${locale}\""
		}

		buildConfigField("String", "DEFAULT_LANGUAGE", "\"$defaultLocale\"")
		buildConfigField("String[]", "SUPPORTED_LANGUAGES", "new String[]{$appLanguages}")

		buildConfigField("String", "FIREBASE_DEVELOPMENT_ENVIRONMENT", "\"$environmentProperty\"")
		buildConfigField("String", "FIREBASE_EMULATOR_HOST", "\"$emulatorHostProperty\"")
		buildConfigField("boolean", "USE_FIREBASE_EMULATOR", "$useEmulatorProperty")
	}
	buildTypes {
		release {
			buildConfigField("boolean", "IS_DEBUG", "false")
		}
		debug {
			applicationIdSuffix = ".debug"

			buildConfigField("boolean", "IS_DEBUG", "true")
		}
	}
	applicationVariants.all {
		val appName = "ComputeIt"
		val flavor = productFlavors[0].name
		val buildType = buildType.name

		fun BaseVariant.appNameResForCurrentEnv(suffix: String = "") {

			val buildTypeSuffix = if (buildType == "debug") ".debug" else ""
			resValue("string", "env__AppName", "$appName$suffix$buildTypeSuffix")
		}

		resValue("string", "const__AppName", appName)

		if (flavor == "production") {
			appNameResForCurrentEnv()
		}
		if (flavor == "development") {
			appNameResForCurrentEnv("-$environmentProperty")
		}
	}
}


fun getAppLocales(defaultLocale: Locale): List<Locale> {
	val foundLocales = mutableListOf(defaultLocale)

	project.android.sourceSets.getByName("main").res.srcDirs.forEach { resFolder ->
		resFolder.listFiles().orEmpty()
			.filter { folder ->
				folder.name.startsWith("values-") && !folder.listFiles().isNullOrEmpty()
			}
			.forEach innerForEach@{ valuesFolder ->
				val hasStringsXmlFile = valuesFolder.listFiles().orEmpty().filterNotNull().any { file ->
					file.name == "strings.xml"
				}
				if (!hasStringsXmlFile) {
					return@innerForEach
				}

				var config = valuesFolder.name.replaceFirst("values-", "").replaceFirst("values", "").let { config ->
					if (config.startsWith("mcc") || config.startsWith("mnc")) {
						val splitPosition = config.indexOf('-')
						if (splitPosition >= 0) {
							return@let config.substring(splitPosition + 1)
						}
					}
					return@let config
				}

				runCatching {
					val locale = if (config.startsWith("b+")) {
						val splitPosition = config.indexOf('-')
						if (splitPosition >= 0) {
							config = config.substring(0, splitPosition)
						}
						Locale.forLanguageTag(config.substring(2))
					}
					else {
						val parts = config.split('-')
						if (parts.size == 1 || !parts[1].startsWith('r')) {
							Locale(parts[0])
						}
						else Locale(parts[0], parts[1].substring(1))
					}

					if (locale.isO3Language != null) {
						foundLocales.add(locale)
					}
				}
			}
	}
	return foundLocales
}
