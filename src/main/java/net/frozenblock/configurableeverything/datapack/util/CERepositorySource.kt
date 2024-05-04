package net.frozenblock.configurableeverything.datapack.util

import com.mojang.logging.LogUtils
import net.minecraft.FileUtil
import net.minecraft.network.chat.Component
import net.minecraft.server.packs.PackLocationInfo
import net.minecraft.server.packs.PackSelectionConfig
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.repository.FolderRepositorySource
import net.minecraft.server.packs.repository.Pack
import net.minecraft.server.packs.repository.Pack.ResourcesSupplier
import net.minecraft.server.packs.repository.PackSource
import net.minecraft.world.level.validation.DirectoryValidator
import org.slf4j.Logger
import java.io.IOException
import java.nio.file.Path
import java.util.*
import java.util.function.Consumer

class CERepositorySource(
    private val folder: Path,
    private val packType: PackType = PackType.SERVER_DATA,
    private val packSource: PackSource = PackSource.WORLD,
    validator: DirectoryValidator
) : FolderRepositorySource(folder, packType, PackSource.WORLD, validator) {

    override fun loadPacks(onLoad: Consumer<Pack>) {
        try {
            FileUtil.createDirectoriesSafe(this.folder)
            discoverPacks(this.folder, this.validator) { packPath: Path?, packFactory: ResourcesSupplier? ->
                if (packPath == null || packFactory == null) {
                    return@discoverPacks
                }
                val string = nameFromPath(packPath)
                val pack = Pack.readMetaAndCreate(
                    PackLocationInfo(
                        "file/$string",
                        Component.literal(string),
                        this.packSource,
                        Optional.empty()
                    ),
                    packFactory,
                    this.packType,
                    PackSelectionConfig(
                        true,
                        Pack.Position.TOP,
                        true
                    )
                )
                if (pack != null) {
                    onLoad.accept(pack)
                }
            }
        } catch (var3: IOException) {
            LOGGER.warn("Failed to list packs in {}", this.folder, var3)
        }
    }

    companion object {
        private val LOGGER: Logger = LogUtils.getLogger()
    }
}
