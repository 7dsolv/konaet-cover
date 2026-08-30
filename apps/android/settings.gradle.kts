pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "konaet-cover"

include(":app")
include(":core:designsystem")
include(":core:model")
include(":core:network")
include(":feature:auth")
include(":feature:home")
include(":feature:onboarding")
