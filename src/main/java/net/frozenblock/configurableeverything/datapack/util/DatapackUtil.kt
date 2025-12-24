package net.frozenblock.configurableeverything.datapack.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Decoder
import com.mojang.serialization.DynamicOps
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.configurableeverything.util.logError
import net.frozenblock.lib.config.api.instance.ConfigSerialization
import net.frozenblock.lib.config.api.instance.json.JanksonOps
import net.frozenblock.lib.config.api.instance.xjs.XjsOps
import net.frozenblock.lib.shadow.xjs.data.serialization.JsonContext as XjsContext
import net.frozenblock.lib.shadow.xjs.data.Json as Xjs
import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.Identifier
import net.minecraft.resources.RegistryDataLoader
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.RegistryOps.RegistryInfoLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.level.validation.DirectoryValidator
import java.io.BufferedReader
import java.util.*
import kotlin.io.path.Path

object DatapackUtil {

    @JvmStatic
    fun addedRepositories(validator: DirectoryValidator?): List<CERepositorySource> {
        val config = MainConfig.get().datapack
        if (validator == null) return emptyList()
        if (config.applyDatapackFolders) {
            val list: MutableList<CERepositorySource> = arrayListOf()
            config.datapackFolders.forEach {
                log("Adding datapack repository at $it")
                list.add(CERepositorySource(Path(it), validator = validator))
            }
            return list
        }
        return emptyList()
    }
}
