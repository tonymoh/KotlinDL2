import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // SLF4J (Simple Logging Facade for Java)
    implementation ("org.slf4j:slf4j-api:1.7.26")
    implementation ("org.slf4j:slf4j-simple:1.7.26")
    //
    implementation ("org.jetbrains.kotlinx:kotlin-deeplearning-onnx:0.5.0")
    implementation ("org.jetbrains.kotlinx:kotlin-deeplearning-visualization:0.5.0")
    implementation ("org.jetbrains.kotlinx:kotlin-deeplearning-api:0.3.0")
    //
    implementation(compose.desktop.currentOs)
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.31")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "KotlinDL2"
            packageVersion = "1.0.0"
        }
    }
}
