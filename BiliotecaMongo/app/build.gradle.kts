buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.2")
        classpath("io.realm:realm-gradle-plugin:10.17.0")
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
    }
}

plugins {
    id("com.android.application")
    id("realm-android")
}

android {
    compileSdkVersion(34)

    defaultConfig {
        applicationId = "com.example.bibliotecamongo"
        minSdkVersion(24)
        targetSdkVersion(34)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("io.realm:realm-android-library:10.17.0")
    implementation("org.mongodb:mongodb-driver-sync:4.11.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

realm {
    syncEnabled = true
}