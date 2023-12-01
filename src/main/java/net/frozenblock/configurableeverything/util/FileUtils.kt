package net.frozenblock.configurableeverything.util

import net.minecraft.FileUtil
import java.io.*
import java.net.URL
import java.nio.file.Path
import java.util.jar.JarEntry
import java.util.jar.JarInputStream
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun File.recreateDir() {
    this.deleteRecursively()
    FileUtil.createDirectoriesSafe(this.toPath())
}

val Path.asDir: Array<File>?
    get() = this.toFile().listFiles()

val Path.asFileList: List<File>?
    get() = this.asDir?.toList()

val File.isJar: Boolean
    get() = this.extension == "jar"

fun File.addToJar(jar: File) {
    JarOutputStream(BufferedOutputStream(FileOutputStream(jar))).use {
        addToJar(it, this, "")
    }
}

fun File.removeFromJar(transform: (JarEntry) -> Boolean) {
    val tempFile = File.createTempFile("temp", ".jar")
    JarOutputStream(FileOutputStream(tempFile).buffered()).use { jarOut ->
        JarInputStream(FileInputStream(this).buffered()).use { jarIn ->
            while (true) {
                val entry = jarIn.nextJarEntry ?: break
                if (transform(entry)) {
                    continue
                }
                jarOut.putNextEntry(entry)
                jarIn.copyTo(jarOut)
            }
        }
    }
    tempFile.copyRecursively(this, true)
}

val File.asJarInput: JarInputStream
    get() {
        return JarInputStream(BufferedInputStream(FileInputStream(this)))
    }

val File.asJarOutput: JarOutputStream
    get() {
        return JarOutputStream(BufferedOutputStream(FileOutputStream(this)))
    }

const val UTF8_BOM = 0xfeff.toChar().toString()

fun File.readTextSkipUtf8Bom(): String = readText().removePrefix(UTF8_BOM)

fun URL.readTextSkipUtf8Bom(): String = readText().removePrefix(UTF8_BOM)

private fun addToJar(jarOut: JarOutputStream, sourceFile: File, parentDirPath: String) {
    val data = ByteArray(2048)
    (sourceFile.listFiles() ?: arrayOf(sourceFile)).forEach { f ->
        if (f.isDirectory) {
            val path = if (parentDirPath == "") {
                f.name
            } else {
                parentDirPath + File.separator + f.name
            }
            val entry = JarEntry(path + File.separator)
            entry.time = f.lastModified()
            entry.isDirectory
            entry.size = f.length()
            jarOut.putNextEntry(entry)
            //Call recursively to add files within this directory
            addToJar(jarOut, f, path)
        } else {
            BufferedInputStream(FileInputStream(f)).use { origin ->
                val path = parentDirPath + File.separator + f.name
                val entry = JarEntry(path)
                entry.time = f.lastModified()
                entry.isDirectory
                entry.size = f.length()
                jarOut.putNextEntry(entry)
                while (true) {
                    val readBytes = origin.read(data)
                    if (readBytes == -1) {
                        break
                    }
                    jarOut.write(data, 0, readBytes)
                }
            }
        }
    }
}

fun File.zipAllTo(zipFile: File) {
    ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use {
        zipFiles(it, this, "")
    }
}

private fun zipFiles(zipOut: ZipOutputStream, sourceFile: File, parentDirPath: String) {
    val data = ByteArray(2048)
    sourceFile.listFiles()?.forEach { f ->
        if (f.isDirectory) {
            val path = if (parentDirPath == "") {
                f.name
            } else {
                parentDirPath + File.separator + f.name
            }
            val entry = ZipEntry(path + File.separator)
            entry.time = f.lastModified()
            entry.isDirectory
            entry.size = f.length()
            zipOut.putNextEntry(entry)
            //Call recursively to add files within this directory
            zipFiles(zipOut, f, path)
        } else {
            BufferedInputStream(FileInputStream(f)).use { origin ->
                val path = parentDirPath + File.separator + f.name
                val entry = ZipEntry(path)
                entry.time = f.lastModified()
                entry.isDirectory
                entry.size = f.length()
                zipOut.putNextEntry(entry)
                while (true) {
                    val readBytes = origin.read(data)
                    if (readBytes == -1) {
                        break
                    }
                    zipOut.write(data, 0, readBytes)
                }
            }
        }
    }
}
