package net.frozenblock.configurableeverything.datapack.util

import com.mojang.serialization.Dynamic
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
import java.io.BufferedReader
import java.io.File
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
        parseInput: (BufferedReader) -> I,
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
                            val result = convertFile(
                                inputPath = inputPath,
                                outputDirectory = outputDirectory,
                                relativePath = relativePath,
                                inputOps = inputOps,
                                outputOps = outputOps,
                                inputExtension = inputExtension,
                                outputExtension = outputExtension,
                                createInputBase = parseInput,
                                serializeOutput = writeOutput
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

    /**
     * Converts a single data file from one format to another.
     *
     * @return A [ConversionError] if conversion failed, null otherwise
     */
    private fun <I : Any, O : Any> convertFile(
        inputPath: Path,
        outputDirectory: Path,
        relativePath: Path,
        inputOps: DynamicOps<I>,
        outputOps: DynamicOps<O>,
        inputExtension: String,
        outputExtension: String,
        createInputBase: (BufferedReader) -> I,
        serializeOutput: (O) -> String
    ): ConversionError? {
        return try {
            // Parse input file
            val inputData: I = Files.newBufferedReader(inputPath).use { reader ->
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
            val serialized = serializeOutput(converted)
            Files.writeString(outputPath, serialized)

            null
        } catch (e: Exception) {
            ConversionError(inputPath, "Failed to convert: ${e.message}")
        }
    }

    @JvmField
    val jsonParser: (BufferedReader) -> JsonElement =
        { reader -> JsonParser.parseReader(reader) }

    @JvmField
    val json5Parser: (BufferedReader) -> JanksonElement =
        { reader -> JANKSON.load(reader.readText()) }

    @JvmField
    val djsParser: (BufferedReader) -> XjsValue =
        { reader -> Xjs.parse(reader.readText()) }

    @JvmField
    val jsoncParser: (BufferedReader) -> XjsValue =
        { reader -> XjsContext.getParser("jsonc").parse(reader) }

    @JvmField
    val hjsonParser: (BufferedReader) -> XjsValue =
        { reader -> XjsContext.getParser("hjson").parse(reader) }

    @JvmField
    val txtParser: (BufferedReader) -> XjsValue =
        { reader -> XjsContext.getParser("txt").parse(reader) }

    @JvmField
    val ubjsonParser: (BufferedReader) -> XjsValue =
        { reader -> XjsContext.getParser("ubjson").parse(reader) }

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
