package net.frozenblock.configurableeverything.datapack.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.datapack.util.CERepositorySource;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstantsKt;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.world.level.validation.DirectoryValidator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(ServerPacksSource.class)
public class ServerPacksSourceMixin {

	@Inject(method = "createPackRepository(Ljava/nio/file/Path;Lnet/minecraft/world/level/validation/DirectoryValidator;)Lnet/minecraft/server/packs/repository/PackRepository;", at = @At("HEAD"))
	private static void createPackRepository(Path path, DirectoryValidator directoryValidator, CallbackInfoReturnable<PackRepository> cir, @Share("validator") LocalRef<DirectoryValidator> validatorLocalRef) {
		validatorLocalRef.set(directoryValidator);
	}

	@ModifyArg(method = "createPackRepository(Ljava/nio/file/Path;Lnet/minecraft/world/level/validation/DirectoryValidator;)Lnet/minecraft/server/packs/repository/PackRepository;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;<init>([Lnet/minecraft/server/packs/repository/RepositorySource;)V"))
	private static RepositorySource[] createPackRepository(RepositorySource[] original, @Share("validator") LocalRef<DirectoryValidator> validator) {
		List<RepositorySource> newSources = new ArrayList<>(Arrays.stream(original).toList());

		var datapack = MainConfig.get().datapack;
		if (datapack != null && Boolean.TRUE.equals(datapack.applyDatapacksFolder)) {
			newSources.add(new CERepositorySource(ConfigurableEverythingSharedConstantsKt.DATAPACKS_PATH, validator.get()));
		}
		return newSources.toArray(new RepositorySource[]{});
	}
}
