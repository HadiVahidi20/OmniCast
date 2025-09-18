pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "OmniCast"
include(":app")
include(":core-common")
include(":core-ui")
include(":core-navigation")
include(":core-data")
include(":core-domain")

// ماژول‌های features
include(":features:home") // مسیر صحیح ماژول home
include(":features:settings")
include(":features:profile")
include(":features:zodiac")