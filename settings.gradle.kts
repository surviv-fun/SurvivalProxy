rootProject.name = "SurvivalProxy"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

include(":plugin")

project(":plugin").name = "SurvivalProxy-Velocity"
