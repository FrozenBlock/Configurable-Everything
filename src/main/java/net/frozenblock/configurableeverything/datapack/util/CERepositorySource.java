package net.frozenblock.configurableeverything.datapack.util;

import com.mojang.logging.LogUtils;
import net.minecraft.FileUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.slf4j.Logger;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public class CERepositorySource extends FolderRepositorySource {
	private static final Logger LOGGER = LogUtils.getLogger();

	private final Path folder;
	private final PackType packType;
	private final PackSource packSource;

	public CERepositorySource(Path folder) {
		super(folder, PackType.SERVER_DATA, PackSource.WORLD);
		this.folder = folder;
		this.packType = PackType.SERVER_DATA;
		this.packSource = PackSource.WORLD;
	}

	@Override
	public void loadPacks(Consumer<Pack> onLoad) {
		try {
			FileUtil.createDirectoriesSafe(this.folder);
			discoverPacks(this.folder, false, (packPath, packFactory) -> {
				String string = nameFromPath(packPath);
				Pack pack = Pack.readMetaAndCreate("file/" + string, Component.literal(string), true, packFactory, this.packType, Pack.Position.TOP, this.packSource);
				if (pack != null) {
					onLoad.accept(pack);
				}
			});
		} catch (IOException var3) {
			LOGGER.warn("Failed to list packs in {}", this.folder, var3);
		}
	}
}
