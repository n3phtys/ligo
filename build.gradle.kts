plugins {
    java
    application
    kotlin("jvm") version "1.3.72"
}

group = "com.github.ckaag.liferay"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.github.ckaag.liferay.ligo.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("commons-net:commons-net:3.7")
    implementation("net.sf.expectit:expectit-core:0.9.0")
    implementation("com.github.ajalt.clikt:clikt:3.0.0-rc")
}
