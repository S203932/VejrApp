plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    //Only Patrick's is set up to release
    // To set up all to release keystore needs to be in git.
    /*
    signingConfigs {
        create("release") {
            storeFile = file("C:\\Users\\Bruger\\Desktop\\keystore.jks")
            storePassword = "Group24"
            keyPassword = "Group24"
            keyAlias = "keystore"
        }
    }

     */
    namespace = "com.example.vejrapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.vejrapp"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            //signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("androidx.hilt:hilt-compiler:1.1.0")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.github.MatteoBattilana:WeatherView:3.0.0")
    implementation("io.coil-kt:coil-compose:2.5.0")

}

//Allow references to generated code
kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}