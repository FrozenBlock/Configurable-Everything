package net.frozenblock.configurableeverything.datapack.mixin;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.datapack.util.CERepositorySource;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {

	@ModifyArg(method = "openFresh", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;<init>([Lnet/minecraft/server/packs/repository/RepositorySource;)V"), index = 0)
	private static RepositorySource[] openFresh(RepositorySource[] original) {
		List<RepositorySource> newSources = new ArrayList<>(Arrays.stream(original).toList());

		if (MainConfig.get().datapack.applyDatapacksFolder) {
			newSources.add(new CERepositorySource(ConfigurableEverythingSharedConstants.DATAPACKS_PATH));
		}
		return newSources.toArray(new RepositorySource[]{});
	}
}