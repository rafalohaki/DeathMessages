pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        maven("https://repo.auxilor.io/repository/maven-public/")
    }
}

rootProject.name = "DeathMessages"

include(
    ":core",

    "nms:abstraction",
    "nms:paper:v1_21_4",

    ":hooks:worldguard",
    ":hooks:worldguard6",
    ":hooks:worldguard7",
)
