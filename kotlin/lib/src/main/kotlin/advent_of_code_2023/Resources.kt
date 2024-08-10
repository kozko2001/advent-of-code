package advent_of_code_2023

import java.io.File
import java.net.URI

object Resources {
    fun resourceAsText(fileName: String): String =
            File(fileName.toURI()).readText()


    private fun String.toURI(): URI =
            Resources.javaClass.classLoader.getResource(this)?.toURI()
                    ?: throw IllegalArgumentException("Cannot find Resource: $this")
}