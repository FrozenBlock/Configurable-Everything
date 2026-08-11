import com.possible_triangle.gradle.features.enableKotlin

plugins {
    id("net.frozenblock.triangle.common")
}

val frozenlib_version: String by project
val cloth_config_version: String by project

common {
    accessWidener()
}

neoForge {
    accessTransformers {} // Required for transitive AW to apply!
}

dependencies {
    compileOnly("net.frozenblock:frozenlib-common:${frozenlib_version}")?.let {
        accessTransformers(it)
        interfaceInjectionData(it)
    }

    compileOnly("net.fabricmc:sponge-mixin:0.17.3+mixin.0.8.7")
    compileOnly("io.github.llamalad7:mixinextras-common:0.5.3")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.3")

    compileOnly("me.shedaniel.cloth:cloth-config:${cloth_config_version}")

    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    compileOnly(kotlin("scripting-common"))
    compileOnly(kotlin("scripting-jvm"))
    compileOnly(kotlin("scripting-jsr223"))
    compileOnly(kotlin("scripting-jvm-host"))
    compileOnly(kotlin("scripting-compiler-embeddable"))
    compileOnly(kotlin("scripting-dependencies"))
    compileOnly(kotlin("scripting-dependencies-maven"))

    testImplementation(kotlin("test"))
}

configurations {
    create("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

upload.maven {
    name.set("configurableeverything-common")
}
repositories {
    mavenCentral()
}
