plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    //id ("kotlin-android-extensions")
    id ("androidx.navigation.safeargs")
    id ("com.google.gms.google-services")
    id ("com.google.firebase.crashlytics"    )
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }

    // Ajout de la dimension "version" pour les flavors
    flavorDimensions("version") // Corrected syntax

    productFlavors {
        create("dev") {  // Corrected syntax to explicitly create the flavor
            dimension = "version"
            versionNameSuffix = "-dev"
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:3000\"")
            buildConfigField("String", "API_KEY", "\"dev_key\"")
        }
        create("prod") {  // Corrected syntax to explicitly create the flavor
            dimension = "version"
            versionNameSuffix = "-prod"
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:3000\"")
            buildConfigField("String", "API_KEY", "\"prod_key\"")
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.google.firebase:firebase-crashlytics:19.0.3")
    // Dependency to include Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.maps.android:android-maps-utils:2.2.0")
    // Maps SDK for Android KTX Library
    implementation("com.google.maps.android:maps-ktx:3.3.0")
    // Maps SDK for Android Utility Library KTX Library
    implementation("com.google.maps.android:maps-utils-ktx:3.0.0")
    // Lifecycle Runtime KTX Library
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("com.google.android.material:material:1.8.0")
    // Retrofit
    implementation("com.squareup.okhttp3:logging-interceptor:4.4.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.5.3")
    androidTestImplementation("androidx.navigation:navigation-testing:2.5.3")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("com.jaredrummler:material-spinner:1.3.1")
    implementation("androidx.databinding:databinding-runtime:7.0.0")
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")


}