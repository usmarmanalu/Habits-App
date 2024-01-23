plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id ("com.google.devtools.ksp")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.dicoding.habitapp"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.dicoding.habitapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "APIKEY", "\"f649f0060f494374993c6c9e98b91997\"")
        testInstrumentationRunnerArguments += mapOf(
            "clearPackageData" to "true"
        )
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    extra.apply{
        set("core_version", "1.12.0")
        set("appcompat_version", "1.6.1")
        set("material_version", "1.10.0")
        set("constraint_version", "2.1.4")

        set("junit_version", "4.13.2")
        set("ext_junit_version", "1.1.5")
        set("espresso_version", "3.5.1")
        set("runner_version", "1.2.0")

        set("room_version", "2.6.0")
        set("arch_lifecycle_version", "2.6.2")
        set("lifecycle_version", "2.6.1")
        set("work_version", "2.8.1")
        set("preference_version", "1.2.1")
        set("paging_version", "2.1.2")
        set("viewpager2_version", "1.0.0")
    }

    implementation("androidx.core:core-ktx:${extra["core_version"]}")
    implementation("androidx.appcompat:appcompat:${extra["appcompat_version"]}")
    implementation("com.google.android.material:material:${extra["material_version"]}")
    implementation("androidx.constraintlayout:constraintlayout:${extra["constraint_version"]}")

    testImplementation("junit:junit:${extra["junit_version"]}")
    androidTestImplementation("androidx.test.ext:junit:${extra["ext_junit_version"]}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${extra["espresso_version"]}")
    androidTestImplementation("androidx.test.espresso:espresso-intents:${extra["espresso_version"]}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${extra["espresso_version"]}")
    androidTestImplementation("com.kaspersky.android-components:kaspresso:1.5.3")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestUtil("androidx.test:orchestrator:1.4.2")

    implementation("androidx.room:room-runtime:${extra["room_version"]}")
    ksp("androidx.room:room-compiler:${extra["room_version"]}")
    implementation("androidx.room:room-ktx:${extra["room_version"]}")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${extra["arch_lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${extra["arch_lifecycle_version"]}")

    implementation("androidx.work:work-runtime-ktx:${extra["work_version"]}")
    implementation("androidx.preference:preference-ktx:${extra["preference_version"]}")

    implementation("androidx.paging:paging-runtime-ktx:${extra["paging_version"]}")
    implementation("androidx.viewpager2:viewpager2:${extra["viewpager2_version"]}")

    implementation ("io.insert-koin:koin-core:3.3.2")
    implementation ("io.insert-koin:koin-android:3.3.2")
    testImplementation ("junit:junit:4.13.2")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    androidTestImplementation ("io.insert-koin:koin-test-junit4:3.3.2")

    testImplementation ("org.mockito:mockito-core:5.9.0")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

    testImplementation ("org.powermock:powermock-api-mockito2:2.0.9")
    testImplementation ("org.powermock:powermock-module-junit4:2.0.9")
    testImplementation ("org.powermock:powermock-module-junit4-rule-agent:2.0.9")

    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:1.1.5")


}