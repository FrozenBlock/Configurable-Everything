package net.frozenblock.configurableeverything.util

import net.minecraft.FileUtil
import java.io.*
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun File.recreateDir() {
    this.deleteRecursively()
    FileUtil.createDirectoriesSafe(this.toPath())
}

fun File.addToJar(jar: File) {
    JarOutputStream(BufferedOutputStream(FileOutputStream(jar))).use {
        zipFiles(it, this, "")
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
