plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mdp_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mdp_project"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //ViewPager2 라이브러리
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    //화면 위치 표시 라이브러리
    implementation("com.tbuonomo:dotsindicator:4.2")
}