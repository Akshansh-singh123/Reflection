package com.akshansh.app

import java.io.File

fun main(args: Array<String>) {
    args.getOrNull(0)?.let { module ->
        val baseDir = File("./${module}/build/libs/${module}.jar")

        val viewer = Viewer()
        viewer.view(baseDir = baseDir)
    }
}
