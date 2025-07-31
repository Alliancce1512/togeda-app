plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.togeda.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.togeda.app"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    
    sourceSets {
        getByName("main") {
            kotlin {
                srcDir("${layout.buildDirectory.get()}/generated/openapi/src/main/kotlin")
            }
        }
    }
    
    lint {
        disable += "NewApi"
        ignoreWarnings = true
    }
}

tasks.register("openApiGenerate") {
    group = "openapi"
    description = "Generate API client from OpenAPI specification"
    
    doLast {
        val inputSpec = "$projectDir/src/main/assets/api/api-docs.yaml"
        val outputDir = "${layout.buildDirectory.get()}/generated/openapi"
        
        exec {
            commandLine("java", "-jar", "${layout.buildDirectory.get()}/tmp/openapi-generator-cli.jar", "generate",
                "-i", inputSpec,
                "-g", "kotlin",
                "-o", outputDir,
                "--api-package", "com.togeda.app.data.remote.generated",
                "--model-package", "com.togeda.app.data.model.generated",
                "--additional-properties", "dateLibrary=java8,serializationLibrary=jackson,useCoroutines=true"
            )
        }
    }
    
    doFirst {
        // Download OpenAPI Generator CLI if not exists
        val openApiGeneratorVersion = "7.4.0"
        val cliJar = file("${layout.buildDirectory.get()}/tmp/openapi-generator-cli.jar")
        if (!cliJar.exists()) {
            cliJar.parentFile.mkdirs()
            exec {
                commandLine("curl", "-L", "https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/$openApiGeneratorVersion/openapi-generator-cli-$openApiGeneratorVersion.jar", "-o", cliJar.absolutePath)
            }
        }
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
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.navigation.compose)
    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    // Jackson
    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.kotlin)
    // Gson
    implementation(libs.gson)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.jackson)
    implementation(libs.retrofit.gson)
    // Coil
    implementation(libs.coil.compose)
    // Core Library Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    // Security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}