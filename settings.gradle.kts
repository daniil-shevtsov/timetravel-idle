pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "TimeTravelGame"

include(":app")
include(":app-desktop")
include(":app-web")
include(":common")
