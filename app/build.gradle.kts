plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.martysh12.legacystuff"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.martysh12.legacystuff"
        minSdk = 1
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
}