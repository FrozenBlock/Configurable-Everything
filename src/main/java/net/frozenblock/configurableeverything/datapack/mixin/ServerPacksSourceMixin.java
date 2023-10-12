package net.frozenblock.configurableeverything.datapack.mixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.datapack.util.CERepositorySource;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstantsKt;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.repository.ServerPacksSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerPacksSource.class)
public class ServerPacksSourceMixin {

	@ModifyArg(method = "createPackRepository(Ljava/nio/file/Path;)Lnet/minecraft/server/packs/repository/PackRepository;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;<init>([Lnet/minecraft/server/packs/repository/RepositorySource;)V"))
	private static RepositorySource[] createPackRepository(RepositorySource[] original) {
		List<RepositorySource> newSources = new ArrayList<>(Arrays.stream(original).toList());

		var datapack = MainConfig.get().datapack;
		if (datapack != null && Boolean.TRUE.equals(datapack.applyDatapacksFolder)) {
			newSources.add(new CERepositorySource(ConfigurableEverythingSharedConstantsKt.DATAPACKS_PATH));
		}
		return newSources.toArray(new RepositorySource[]{});
	}
}
