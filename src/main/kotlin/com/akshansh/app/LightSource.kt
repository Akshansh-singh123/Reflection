package com.akshansh.app

import java.io.File

fun main(args: Array<String>) {
    val baseDir = File("./feature/build/libs/feature.jar")

    val viewer = Viewer()
    viewer.view(baseDir = baseDir)
}
