plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.hami.fresheatables"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hami.fresheatables"
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
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains" && requested.name == "annotations") {
            useVersion("23.0.0")
        }
    }
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")

    // Your other dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Room dependencies
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version") // Use annotationProcessor instead of kapt
    implementation("androidx.room:room-ktx:$room_version")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1") {
        exclude(group = "com.intellij", module = "annotations") // Exclude old annotations
    }
}
