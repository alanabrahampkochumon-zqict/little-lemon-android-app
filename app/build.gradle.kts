plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.littlelemon.application"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.littlelemon"
        minSdk = 24
        targetSdk = 36
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
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    buildFeatures {
        compose = true
    }

    testFixtures {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.navigation)

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.rules)
    ksp(libs.androidx.room.compiler)

    implementation(libs.io.ktor.client)
    implementation(libs.io.ktor.cio)

    implementation(platform(libs.supbase.bom))
    implementation(libs.supbase.auth)
    implementation(libs.supbase.postgres)

    implementation(libs.kotlinx.datetime)

    implementation(libs.play.services.location)
    implementation(libs.kotlinx.coroutines.play.services) // For .await for cb based methods of google play services like location


    coreLibraryDesugaring(libs.desugar.jdk)

    testImplementation(kotlin("test"))
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit5)
    testImplementation(libs.io.mockk)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.kotlin.faker)
    testImplementation(libs.turbine)
    testImplementation(testFixtures(project(":app")))
    androidTestImplementation(testFixtures(project(":app")))

    testRuntimeOnly(libs.junit.engine)
    testRuntimeOnly(libs.junit.platform)


    testFixturesImplementation(platform(libs.supbase.bom))
    testFixturesImplementation(platform(libs.androidx.compose.bom))
    testFixturesImplementation(libs.androidx.ui)
    testFixturesImplementation(libs.androidx.material3)
    testFixturesImplementation(libs.supbase.auth)
    testFixturesImplementation(libs.supbase.postgres)
    testFixturesImplementation(libs.io.ktor.mock)
    testFixturesImplementation(libs.io.ktor.client)
    testFixturesImplementation(libs.kotlin.faker)

    testImplementation(libs.io.ktor.mock)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.kotlinx.coroutines.play.services)
    androidTestImplementation(libs.kotlin.faker)
    androidTestImplementation(kotlin("test"))


    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testFixturesCompileOnly("org.jetbrains.kotlin:kotlin-stdlib:$kotlin")
}