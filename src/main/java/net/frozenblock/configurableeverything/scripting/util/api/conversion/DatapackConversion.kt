package net.frozenblock.configurableeverything.scripting.util.api.conversion

import com.mojang.serialization.DynamicOps
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.lib.config.api.instance.ConfigSerialization
import net.frozenblock.lib.config.api.instance.json.JanksonOps
import net.frozenblock.lib.config.api.instance.xjs.XjsOps
import net.frozenblock.lib.shadow.xjs.data.Json as Xjs
import net.frozenblock.lib.shadow.xjs.data.serialization.JsonContext as XjsContext
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import net.frozenblock.lib.shadow.xjs.data.JsonValue as XjsValue
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.relativeTo
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonElement as JanksonElement

object DatapackConversion {

    private val GSON = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    private val JANKSON = ConfigSerialization.createJankson("")

    @JvmStatic
    fun <I : Any, O : Any> convertDatapack(
        inputDatapackPath: Path,
        outputDirectory: Path,
        inputOps: DynamicOps<I>,
        outputOps: DynamicOps<O>,
        inputExtension: String,
        outputExtension: String,
        parseInput: (FileInputStream) -> I,
        writeOutput: (O) -> String
    ): ConversionResult {
        val errors = mutableListOf<ConversionError>()
        var filesConverted = 0
        var filesCopied = 0
        var filesSkipped = 0

        val inputDir = inputDatapackPath.toFile()
        if (!inputDir.exists() || !inputDir.isDirectory) {
            return ConversionResult(
                success = false,
                filesConverted = 0,
                filesCopied = 0,
                filesSkipped = 0,
                errors = listOf(ConversionError(inputDatapackPath, "Input datapack path does not exist or is not a directory"))
            )
        }

        // Ensure output directory exists
        Files.createDirectories(outputDirectory)

        // Process all files in the datapack
        Files.walk(inputDatapackPath).use { stream ->
            stream.forEach { inputPath ->
                if (inputPath.isDirectory()) return@forEach

                val relativePath = inputPath.relativeTo(inputDatapackPath)
                val relativePathStr = relativePath.toString()

                try {
                    when {
                        // Copy pack.mcmeta and pack.png as-is (unless they match input extension)
                        relativePathStr == "pack.mcmeta" || relativePathStr == "pack.png" -> {
                            val outputPath = outputDirectory.resolve(relativePath)
                            Files.createDirectories(outputPath.parent)
                            Files.copy(inputPath, outputPath, StandardCopyOption.REPLACE_EXISTING)
                            filesCopied++
                        }

                        // Convert data files with matching extension
                        relativePathStr.startsWith("data${File.separator}") && inputPath.extension == inputExtension -> {
                            val result = FileConversion.convertFile(
                                inputPath = inputPath,
                                outputDirectory = outputDirectory,
                                relativePath = relativePath,
                                inputOps = inputOps,
                                outputOps = outputOps,
                                inputExtension = inputExtension,
                                outputExtension = outputExtension,
                                createInputBase = parseInput,
                                writeOutput = writeOutput
                            )
                            if (result != null) {
                                errors.add(result)
                                filesSkipped++
                            } else {
                                filesConverted++
                            }
                        }

                        // Copy other files in data/ that don't match input extension (e.g., different format files, textures)
                        relativePathStr.startsWith("data${File.separator}") -> {
                            val outputPath = outputDirectory.resolve(relativePath)
                            Files.createDirectories(outputPath.parent)
                            Files.copy(inputPath, outputPath, StandardCopyOption.REPLACE_EXISTING)
                            filesCopied++
                        }

                        // Copy any other top-level files
                        else -> {
                            val outputPath = outputDirectory.resolve(relativePath)
                            Files.createDirectories(outputPath.parent)
                            Files.copy(inputPath, outputPath, StandardCopyOption.REPLACE_EXISTING)
                            filesCopied++
                        }
                    }
                } catch (e: Exception) {
                    errors.add(ConversionError(inputPath, "Unexpected error: ${e.message}"))
                    filesSkipped++
                }
            }
        }

        log("Datapack conversion complete: $filesConverted files converted, $filesCopied files copied, $filesSkipped files skipped")

        return ConversionResult(
            success = errors.isEmpty(),
            filesConverted = filesConverted,
            filesCopied = filesCopied,
            filesSkipped = filesSkipped,
            errors = errors
        )
    }

    @JvmField
    val jsonParser: (FileInputStream) -> JsonElement =
        { file -> JsonParser.parseReader(InputStreamReader(file)) }

    @JvmField
    val json5Parser: (FileInputStream) -> JanksonElement =
        { file -> JANKSON.load(file) }

    @JvmField
    val djsParser: (FileInputStream) -> XjsValue =
        { file -> Xjs.parse(file) }

    @JvmField
    val jsoncParser: (FileInputStream) -> XjsValue =
        { file -> XjsContext.getParser("jsonc").parse(file) }

    @JvmField
    val hjsonParser: (FileInputStream) -> XjsValue =
        { file -> XjsContext.getParser("hjson").parse(file) }

    @JvmField
    val txtParser: (FileInputStream) -> XjsValue =
        { file -> XjsContext.getParser("txt").parse(file) }

    @JvmField
    val ubjsonParser: (FileInputStream) -> XjsValue =
        { file -> XjsContext.getParser("ubjson").parse(file) }

    @JvmField
    val jsonWriter: (JsonElement) -> String = { value -> GSON.toJson(value) }

    @JvmField
    val json5Writer: (JanksonElement) -> String =
        { value -> value.toJson(true, true) }

    @JvmField
    val djsWriter: (XjsValue) -> String =
        { value -> value.toString("djs", XjsContext.getDefaultFormatting()) }

    @JvmField
    val jsoncWriter: (XjsValue) -> String =
        { value -> value.toString("jsonc", XjsContext.getDefaultFormatting()) }

    @JvmField
    val hjsonWriter: (XjsValue) -> String =
        { value -> value.toString("hjson", XjsContext.getDefaultFormatting()) }

    @JvmField
    val txtWriter: (XjsValue) -> String =
        { value -> value.toString("txt", XjsContext.getDefaultFormatting()) }

    @JvmField
    val ubjsonWriter: (XjsValue) -> String = { value -> value.toString("ubjson", XjsContext.getDefaultFormatting()) }

    @JvmStatic
    fun convertJsonToJson5(inputDatapackPath: Path, outputDirectory: Path): ConversionResult {
        return convertDatapack(
            inputDatapackPath = inputDatapackPath,
            outputDirectory = outputDirectory,
            inputOps = JsonOps.INSTANCE,
            outputOps = JanksonOps.INSTANCE,
            inputExtension = "json",
            outputExtension = "json5",
            parseInput = jsonParser,
            writeOutput = json5Writer
        )
    }

    @JvmStatic
    fun convertJson5ToJson(inputDatapackPath: Path, outputDirectory: Path): ConversionResult {
        return convertDatapack(
            inputDatapackPath = inputDatapackPath,
            outputDirectory = outputDirectory,
            inputOps = JanksonOps.INSTANCE,
            outputOps = JsonOps.INSTANCE,
            inputExtension = "json5",
            outputExtension = "json",
            parseInput = json5Parser,
            writeOutput = jsonWriter
        )
    }

    @JvmStatic
    fun convertJsonToDjs(inputDatapackPath: Path, outputDirectory: Path): ConversionResult {
        return convertDatapack(
            inputDatapackPath = inputDatapackPath,
            outputDirectory = outputDirectory,
            inputOps = JsonOps.INSTANCE,
            outputOps = XjsOps.INSTANCE,
            inputExtension = "json",
            outputExtension = "djs",
            parseInput = jsonParser,
            writeOutput = djsWriter
        )
    }

    @JvmStatic
    fun convertDjsToJson(inputDatapackPath: Path, outputDirectory: Path): ConversionResult {
        return convertDatapack(
            inputDatapackPath = inputDatapackPath,
            outputDirectory = outputDirectory,
            inputOps = XjsOps.INSTANCE,
            outputOps = JsonOps.INSTANCE,
            inputExtension = "djs",
            outputExtension = "json",
            parseInput = djsParser,
            writeOutput = jsonWriter
        )
    }

    /**
     * Result of a datapack conversion operation.
     */
    data class ConversionResult(
        val success: Boolean,
        val filesConverted: Int,
        val filesCopied: Int,
        val filesSkipped: Int,
        val errors: List<ConversionError>
    )

    /**
     * Represents an error that occurred during conversion.
     */
    data class ConversionError(
        val filePath: Path,
        val message: String
    )
}
