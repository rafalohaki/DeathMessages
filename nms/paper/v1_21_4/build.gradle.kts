plugins {
    id("cn.dreeam.deathmessages.wrapper")
}

dependencies {
    api(project(":nms:abstraction"))
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")

    compileOnly(libs.bundles.adventure)
}
