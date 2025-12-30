package net.frozenblock.configurableeverything.datapack.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.log
import net.minecraft.world.level.validation.DirectoryValidator
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
