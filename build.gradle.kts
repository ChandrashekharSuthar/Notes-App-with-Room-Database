// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    // Kotlin Ksp Symbolic Processing Plugin for Gradle alternate to kapt
    alias(libs.plugins.kotlinKsp) apply false
}