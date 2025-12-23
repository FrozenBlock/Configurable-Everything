package net.frozenblock.configurableeverything.scripting.util.conversion

import com.mojang.serialization.Dynamic
import com.mojang.serialization.DynamicOps
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path

object FileConversion {

    @JvmStatic
    fun <I : Any, O : Any> convertFile(
        inputPath: Path,
        outputDirectory: Path,
        relativePath: Path,
        inputOps: DynamicOps<I>,
        outputOps: DynamicOps<O>,
        inputExtension: String,
        outputExtension: String,
        createInputBase: (FileInputStream) -> I,
        writeOutput: (O) -> String
    ): DatapackConversion.ConversionError? {
        return try {
            // Parse input file
            val inputData: I = FileInputStream(inputPath.toFile()).use { reader ->
                createInputBase(reader)
            }

            // Convert using Dynamic
            val dynamic = Dynamic(inputOps, inputData)
            val converted: O = dynamic.convert(outputOps).value

            // Calculate output path with new extension
            val outputRelativePath = relativePath.toString().let { path ->
                if (path.endsWith(".$inputExtension")) {
                    path.dropLast(inputExtension.length + 1) + ".$outputExtension"
                } else {
                    path
                }
            }
            val outputPath = outputDirectory.resolve(outputRelativePath)

            // Write output file
            Files.createDirectories(outputPath.parent)
            val serialized = writeOutput(converted)
            Files.writeString(outputPath, serialized)

            null
        } catch (e: Exception) {
            DatapackConversion.ConversionError(inputPath, "Failed to convert: ${e.message ?: e}")
        }
    }
}
