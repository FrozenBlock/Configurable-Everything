package net.frozenblock.configurableeverything.datapack.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.frozenblock.configurableeverything.datapack.util.DatapackUtil;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.world.level.validation.DirectoryValidator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPacksSource.class)
public class ServerPacksSourceMixin {

	@Inject(method = "createPackRepository(Ljava/nio/file/Path;Lnet/minecraft/world/level/validation/DirectoryValidator;)Lnet/minecraft/server/packs/repository/PackRepository;", at = @At("HEAD"))
	private static void createPackRepository(Path path, DirectoryValidator directoryValidator, CallbackInfoReturnable<PackRepository> cir, @Share("validator") LocalRef<DirectoryValidator> validatorLocalRef) {
		validatorLocalRef.set(directoryValidator);
	}

	@ModifyArg(method = "createPackRepository(Ljava/nio/file/Path;Lnet/minecraft/world/level/validation/DirectoryValidator;)Lnet/minecraft/server/packs/repository/PackRepository;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;<init>([Lnet/minecraft/server/packs/repository/RepositorySource;)V"))
	private static RepositorySource[] createPackRepository(RepositorySource[] original, @Share("validator") LocalRef<DirectoryValidator> validator) {
		List<RepositorySource> newSources = new ArrayList<>(Arrays.stream(original).toList());

		newSources.addAll(DatapackUtil.addedRepositories(validator.get()));
		return newSources.toArray(new RepositorySource[]{});
	}
}
