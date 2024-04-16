plugins {
    alias(libs.plugins.androidApplication)
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    //id ("com.android.application")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.example.gmapactivity"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gmapactivity"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    //implementation(libs.play.services.ads)
    implementation(libs.play.services.location)

    implementation(libs.firebase.firestore)
    implementation(project(":mylibrary"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.firebase:firebase-firestore")
    implementation ("com.google.code.gson:gson:2.10.1")




    //implementation ("com.google.firebase:firebase-core:20.0.0")
    //<!-- Este é um comentário em XML -->
    //implementation ("com.google.android.gms:play-services-location:18.0.0")
    //implementation ("com.google.firebase:firebase-bom:32.8.0")//pode dar erro -> Removi o platform
    //implementation ("com.google.firebase:firebase-analytics")
    //implementation ("com.google.firebase:firebase-database:20.0.0")
    //implementation ("com.google.firebase:firebase-bom:32.8.0")
    //implementation ("com.google.firebase:firebase-database")
    //implementation ("com.google.firebase:firebase-firestore")

}