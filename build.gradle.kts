import org.gradle.kotlin.dsl.libs

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.7.1" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript{
    repositories{
        google()
        mavenCentral()
    }
    dependencies{
        classpath("com.google.gms:google-services:4.4.2")

    }
}

allprojects{
    repositories{

    }
}

