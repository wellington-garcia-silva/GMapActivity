plugins {
    alias(libs.plugins.androidLibrary)
    //id("com.android.library")
    id ("maven-publish")
}

android {
    namespace = "com.example.mylibrary"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    //implementation project(path: "mylibrary")
    //implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.guava)
    //implementation(fileTree(mapOf(
        //"dir" to "C:\\Program Files\\Android\\Android Studio\\lib",
        //"include" to listOf("*.aar", "*.jar"),
        //"exclude" to listOf()
    //)))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}