plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = properties["pluginGroup"]!!
version = properties["pluginVersion"]!!

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.add("-Xjdk-release=17")
    }
}

configurations.all {
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
}

intellij {
    version.set(properties["platformVersion"].toString())
    type.set(properties["platformType"].toString())
    plugins.set(listOf("java", "maven"))
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation("junit:junit:4.13.2")
}

tasks {
    patchPluginXml {
        sinceBuild.set(project.properties["pluginSinceBuild"].toString())
        untilBuild.set(project.properties["pluginUntilBuild"].toString())
    }
    
    buildSearchableOptions {
        enabled = false
    }
    
    instrumentCode {
        enabled = false
    }
    
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}
