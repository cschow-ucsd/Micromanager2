@file:Suppress("PropertyName")

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val graphql_kotlin_version: String by project

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
    }
}

plugins {
    kotlin("jvm") version "1.4.21"
    application
}

group = "project.ucsd"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { setUrl("https://kotlin.bintray.com/ktor") }
    maven { setUrl("https://kotlin.bintray.com/kotlin-js-wrappers") }
    maven { setUrl("https://kotlin.bintray.com/kotlinx") }
    maven { setUrl("https://s3-us-west-2.amazonaws.com/dynamodb-local/release") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.31-kotlin-1.2.41")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-gson:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-logging-jvm:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")

    // graphql-kotlin
    implementation("com.expediagroup:graphql-kotlin-schema-generator:$graphql_kotlin_version")
    implementation("com.graphql-java:graphql-java:11.0")

    // dynamodb
    implementation("com.amazonaws:aws-java-sdk-dynamodb:1.11.822")
    implementation("com.amazonaws:DynamoDBLocal:[1.12,2.0)")

    // sqlite4java for DynamoDB Local
//    implementation("com.almworks.sqlite4java:sqlite4java:1.0.392")
//    implementation("com.almworks.sqlite4java:sqlite4java-win32-x86:1.0.392")
//    implementation("com.almworks.sqlite4java:sqlite4java-win32-x64:1.0.392")
//    implementation("com.almworks.sqlite4java:libsqlite4java-osx:1.0.392")
//    implementation("com.almworks.sqlite4java:libsqlite4java-linux-i386:1.0.392")
//    implementation("com.almworks.sqlite4java:libsqlite4java-linux-amd64:1.0.392")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

val copyNativeDeps by tasks.creating(Copy::class) {
    from(configurations.runtimeClasspath) {
        include("*.dll")
        include("*.dylib")
        include("*.so")
    }
    into("$buildDir/libs")
}

tasks.withType<JavaExec> {
    dependsOn.add(copyNativeDeps)
    doFirst { systemProperty("java.library.path", "$buildDir/libs") }
}