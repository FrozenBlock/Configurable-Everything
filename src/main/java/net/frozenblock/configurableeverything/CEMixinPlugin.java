package net.frozenblock.configurableeverything;

import net.frozenblock.configurableeverything.config.gui.MixinsConfig;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import java.util.List;
import java.util.Set;

public class CEMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		var config = MixinsConfig.get();
		if (mixinClassName.contains("biome_placement.mixin"))
			return config.biome_placement == true;

		if (mixinClassName.contains("datafixer.mixin"))
			return config.datafixer == true;

		if (mixinClassName.contains("datapack.mixin"))
			return config.datapack == true;

		if (mixinClassName.contains("entity.mixin.zombie"))
			return config.entity_zombie == true;

		if (mixinClassName.contains("entity.mixin"))
			return config.entity == true;

		if (mixinClassName.contains("fluid.mixin"))
			return config.fluid == true;

		if (mixinClassName.contains("game.mixin.client"))
			return config.game_client == true;

		if (mixinClassName.contains("game.mixin"))
			return config.game == true;

		if (mixinClassName.contains("screenshake.mixin.client"))
			return config.screenshake_client;

		if (mixinClassName.contains("screenshake.mixin"))
			return config.screenshake;

		if (mixinClassName.contains("splash_text.mixin"))
			return config.splash_text;

		if (mixinClassName.contains("world.mixin.client"))
			return config.world_client;

		if (mixinClassName.contains("world.mixin"))
			return config.world;
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
