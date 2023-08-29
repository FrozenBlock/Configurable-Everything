package net.frozenblock.configurableeverything.config

import blue.endless.jankson.Comment
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.instance.json.JsonType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.world.phys.Vec3

class GameConfig {
    companion object {
        private val TEST: TypedEntryType<List<Vec3>> = ConfigRegistry.register(
            TypedEntryType(
                MOD_ID,
                Vec3.CODEC.listOf()
            )
        )

        private val INSTANCE: Config<GameConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                GameConfig::class.java,
                makeConfigPath("game"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): GameConfig = INSTANCE.config()

        @JvmStatic
        fun getConfigInstance(): Config<GameConfig> = INSTANCE
    }

    @JvmField
	@Comment("Changes the game window title. Change this from an empty string to apply.")
    var windowTitle: String? = ""

    @JvmField
	@Comment(
        """
		Changes the version series. Change this from an empty string to apply.
		The default version series is "main".

		"""
    )
    var versionSeries: String? = ""

    @JvmField
    @Comment("Im just testing Cloth Config typed entries")
    var testing: TypedEntry<List<Vec3>> = TypedEntry(
        TEST,
        listOf(
            Vec3(1.0, 1.0, 1.0),
            Vec3(2.0, 2.0, 2.0),
            Vec3(69.0, 420.0, 5.0)
        )
    )
}
