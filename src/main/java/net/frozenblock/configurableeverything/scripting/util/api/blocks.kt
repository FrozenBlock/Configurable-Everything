package net.frozenblock.configurableeverything.scripting.util.api

import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.AttachedStemBlock
import net.minecraft.world.level.block.AzaleaBlock
import net.minecraft.world.level.block.BarrierBlock
import net.minecraft.world.level.block.BaseCoralFanBlock
import net.minecraft.world.level.block.BaseCoralPlantBlock
import net.minecraft.world.level.block.BaseCoralWallFanBlock
import net.minecraft.world.level.block.BigDripleafBlock
import net.minecraft.world.level.block.BigDripleafStemBlock
import net.minecraft.world.level.block.BlastFurnaceBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.BushBlock
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.CactusBlock
import net.minecraft.world.level.block.CakeBlock
import net.minecraft.world.level.block.CandleCakeBlock
import net.minecraft.world.level.block.CartographyTableBlock
import net.minecraft.world.level.block.CarvedPumpkinBlock
import net.minecraft.world.level.block.ChestBlock
import net.minecraft.world.level.block.ChorusFlowerBlock
import net.minecraft.world.level.block.ChorusPlantBlock
import net.minecraft.world.level.block.CoralFanBlock
import net.minecraft.world.level.block.CoralPlantBlock
import net.minecraft.world.level.block.CoralWallFanBlock
import net.minecraft.world.level.block.CraftingTableBlock
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.DecoratedPotBlock
import net.minecraft.world.level.block.DirtPathBlock
import net.minecraft.world.level.block.DispenserBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.EnchantingTableBlock
import net.minecraft.world.level.block.EndGatewayBlock
import net.minecraft.world.level.block.EndPortalBlock
import net.minecraft.world.level.block.EndRodBlock
import net.minecraft.world.level.block.EnderChestBlock
import net.minecraft.world.level.block.FarmBlock
import net.minecraft.world.level.block.FungusBlock
import net.minecraft.world.level.block.FurnaceBlock
import net.minecraft.world.level.block.GrindstoneBlock
import net.minecraft.world.level.block.HalfTransparentBlock
import net.minecraft.world.level.block.HangingRootsBlock
import net.minecraft.world.level.block.IronBarsBlock
import net.minecraft.world.level.block.JigsawBlock
import net.minecraft.world.level.block.JukeboxBlock
import net.minecraft.world.level.block.KelpBlock
import net.minecraft.world.level.block.KelpPlantBlock
import net.minecraft.world.level.block.LadderBlock
import net.minecraft.world.level.block.LecternBlock
import net.minecraft.world.level.block.LeverBlock
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.LoomBlock
import net.minecraft.world.level.block.MangroveRootsBlock
import net.minecraft.world.level.block.NetherWartBlock
import net.minecraft.world.level.block.NyliumBlock
import net.minecraft.world.level.block.PlayerHeadBlock
import net.minecraft.world.level.block.PlayerWallHeadBlock
import net.minecraft.world.level.block.PoweredRailBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.PumpkinBlock
import net.minecraft.world.level.block.RailBlock
import net.minecraft.world.level.block.RedstoneTorchBlock
import net.minecraft.world.level.block.RedstoneWallTorchBlock
import net.minecraft.world.level.block.RepeaterBlock
import net.minecraft.world.level.block.RootsBlock
import net.minecraft.world.level.block.SaplingBlock
import net.minecraft.world.level.block.ScaffoldingBlock
import net.minecraft.world.level.block.SeaPickleBlock
import net.minecraft.world.level.block.SeagrassBlock
import net.minecraft.world.level.block.SkullBlock
import net.minecraft.world.level.block.SmithingTableBlock
import net.minecraft.world.level.block.SmokerBlock
import net.minecraft.world.level.block.SnowLayerBlock
import net.minecraft.world.level.block.SnowyDirtBlock
import net.minecraft.world.level.block.SpawnerBlock
import net.minecraft.world.level.block.SpongeBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.StemBlock
import net.minecraft.world.level.block.StructureBlock
import net.minecraft.world.level.block.StructureVoidBlock
import net.minecraft.world.level.block.SugarCaneBlock
import net.minecraft.world.level.block.TallGrassBlock
import net.minecraft.world.level.block.TorchBlock
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallSkullBlock
import net.minecraft.world.level.block.WallTorchBlock
import net.minecraft.world.level.block.WaterlilyBlock
import net.minecraft.world.level.block.WaterloggedTransparentBlock
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.WeatheringCopperDoorBlock
import net.minecraft.world.level.block.WeatheringCopperGrateBlock
import net.minecraft.world.level.block.WeatheringCopperTrapDoorBlock
import net.minecraft.world.level.block.WeightedPressurePlateBlock
import net.minecraft.world.level.block.WetSpongeBlock
import net.minecraft.world.level.block.WitherWallSkullBlock
import net.minecraft.world.level.block.WoolCarpetBlock
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.ChestBlockEntity
import net.minecraft.world.level.block.grower.TreeGrower
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.material.FlowingFluid

fun AttachedStemBlock(
    stem: ResourceKey<Block>,
    fruit: ResourceKey<Block>,
    seed: ResourceKey<Item>,
    properties: BlockBehaviour.Properties,
): AttachedStemBlock = AttachedStemBlock(stem, fruit, seed, properties)

fun AzaleaBlock(
    properties: BlockBehaviour.Properties,
): AzaleaBlock = AzaleaBlock(properties)

fun BarrierBlock(
    properties: BlockBehaviour.Properties,
): BarrierBlock = BarrierBlock(properties)

fun BaseCoralFanBlock(
    properties: BlockBehaviour.Properties,
): BaseCoralFanBlock = BaseCoralFanBlock(properties)

fun BaseCoralPlantBlock(
    properties: BlockBehaviour.Properties,
): BaseCoralPlantBlock = BaseCoralPlantBlock(properties)

fun BaseCoralWallFanBlock(
    properties: BlockBehaviour.Properties,
): BaseCoralWallFanBlock = BaseCoralWallFanBlock(properties)

fun BigDripleafBlock(
    properties: BlockBehaviour.Properties,
): BigDripleafBlock = BigDripleafBlock(properties)

fun BigDripleafStemBlock(
    properties: BlockBehaviour.Properties,
): BigDripleafStemBlock = BigDripleafStemBlock(properties)

fun BlastFurnaceBlock(
    properties: BlockBehaviour.Properties,
): BlastFurnaceBlock = BlastFurnaceBlock(properties)

fun ButtonBlock(
    setType: BlockSetType,
    pressTicks: Int,
    properties: BlockBehaviour.Properties,
): ButtonBlock = ButtonBlock(setType, pressTicks, properties)

fun CactusBlock(
    properties: BlockBehaviour.Properties,
): CactusBlock = CactusBlock(properties)

fun CakeBlock(
    properties: BlockBehaviour.Properties,
): CakeBlock = CakeBlock(properties)

fun CandleCakeBlock(
    candle: Block,
    properties: BlockBehaviour.Properties,
): CandleCakeBlock = CandleCakeBlock(candle, properties)

fun CartographyTableBlock(
    properties: BlockBehaviour.Properties,
): CartographyTableBlock = CartographyTableBlock(properties)

fun CarvedPumpkinBlock(
    properties: BlockBehaviour.Properties,
): CarvedPumpkinBlock = CarvedPumpkinBlock(properties)

fun ChestBlock(
    properties: BlockBehaviour.Properties,
    blockEntityTypeSupplier: () -> BlockEntityType<out ChestBlockEntity>,
): ChestBlock = ChestBlock(properties, blockEntityTypeSupplier)

fun ChorusFlowerBlock(
    plantBlock: Block,
    properties: BlockBehaviour.Properties,
): ChorusFlowerBlock = ChorusFlowerBlock(plantBlock, properties)

fun ChorusPlantBlock(
    properties: BlockBehaviour.Properties,
): ChorusPlantBlock = ChorusPlantBlock(properties)

fun CoralFanBlock(
    deadCoralBlock: Block,
    properties: BlockBehaviour.Properties,
): CoralFanBlock = CoralFanBlock(deadCoralBlock, properties)

fun CoralPlantBlock(
    deadCoralBlock: Block,
    properties: BlockBehaviour.Properties,
): CoralPlantBlock = CoralPlantBlock(deadCoralBlock, properties)

fun CoralWallFanBlock(
    deadCoralBlock: Block,
    properties: BlockBehaviour.Properties,
): CoralWallFanBlock = CoralWallFanBlock(deadCoralBlock, properties)

fun CraftingTableBlock(
    properties: BlockBehaviour.Properties,
): CraftingTableBlock = CraftingTableBlock(properties)

fun CropBlock(
    properties: BlockBehaviour.Properties,
): CropBlock = CropBlock(properties)

fun DecoratedPotBlock(
    properties: BlockBehaviour.Properties,
): DecoratedPotBlock = DecoratedPotBlock(properties)

fun DirtPathBlock(
    properties: BlockBehaviour.Properties,
): DirtPathBlock = DirtPathBlock(properties)

fun DispenserBlock(
    properties: BlockBehaviour.Properties,
): DispenserBlock = DispenserBlock(properties)

fun DoorBlock(
    blockSetType: BlockSetType,
    properties: BlockBehaviour.Properties,
): DoorBlock = DoorBlock(blockSetType, properties)

fun EnchantingTableBlock(
    properties: BlockBehaviour.Properties,
): EnchantingTableBlock = EnchantingTableBlock(properties)

fun EndGatewayBlock(
    properties: BlockBehaviour.Properties,
): EndGatewayBlock = EndGatewayBlock(properties)

fun EndPortalBlock(
    properties: BlockBehaviour.Properties,
): EndPortalBlock = EndPortalBlock(properties)

fun EndRodBlock(
    properties: BlockBehaviour.Properties,
): EndRodBlock = EndRodBlock(properties)

fun EnderChestBlock(
    properties: BlockBehaviour.Properties,
): EnderChestBlock = EnderChestBlock(properties)

fun FarmBlock(
    properties: BlockBehaviour.Properties,
): FarmBlock = FarmBlock(properties)

fun FungusBlock(
    featureKey: ResourceKey<ConfiguredFeature<*, *>>,
    nyliumBlock: Block,
    properties: BlockBehaviour.Properties,
): FungusBlock = FungusBlock(featureKey, nyliumBlock, properties)

fun FurnaceBlock(
    properties: BlockBehaviour.Properties,
): FurnaceBlock = FurnaceBlock(properties)

fun GrindstoneBlock(
    properties: BlockBehaviour.Properties,
): GrindstoneBlock = GrindstoneBlock(properties)

fun HalfTransparentBlock(
    properties: BlockBehaviour.Properties,
): HalfTransparentBlock = HalfTransparentBlock(properties)

fun HangingRootsBlock(
    properties: BlockBehaviour.Properties,
): HangingRootsBlock = HangingRootsBlock(properties)

fun IronBarsBlock(
    properties: BlockBehaviour.Properties,
): IronBarsBlock = IronBarsBlock(properties)

fun JigsawBlock(
    properties: BlockBehaviour.Properties,
): JigsawBlock = JigsawBlock(properties)

fun JukeboxBlock(
    properties: BlockBehaviour.Properties,
): JukeboxBlock = JukeboxBlock(properties)

fun KelpBlock(
    properties: BlockBehaviour.Properties,
): KelpBlock = KelpBlock(properties)

fun KelpPlantBlock(
    properties: BlockBehaviour.Properties,
): KelpPlantBlock = KelpPlantBlock(properties)

fun LadderBlock(
    properties: BlockBehaviour.Properties,
): LadderBlock = LadderBlock(properties)

fun LecternBlock(
    properties: BlockBehaviour.Properties,
): LecternBlock = LecternBlock(properties)

fun LeverBlock(
    properties: BlockBehaviour.Properties,
): LeverBlock = LeverBlock(properties)

fun LiquidBlock(
    fluid: FlowingFluid,
    properties: BlockBehaviour.Properties,
): LiquidBlock = LiquidBlock(fluid, properties)

fun LoomBlock(
    properties: BlockBehaviour.Properties,
): LoomBlock = LoomBlock(properties)

fun MangroveRootsBlock(
    properties: BlockBehaviour.Properties,
): MangroveRootsBlock = MangroveRootsBlock(properties)

fun NetherWartBlock(
    properties: BlockBehaviour.Properties,
): NetherWartBlock = NetherWartBlock(properties)

fun NyliumBlock(
    properties: BlockBehaviour.Properties,
): NyliumBlock = NyliumBlock(properties)

fun PlayerHeadBlock(
    properties: BlockBehaviour.Properties,
): PlayerHeadBlock = PlayerHeadBlock(properties)

fun PlayerWallHeadBlock(
    properties: BlockBehaviour.Properties,
): PlayerWallHeadBlock = PlayerWallHeadBlock(properties)

fun PoweredRailBlock(
    properties: BlockBehaviour.Properties,
): PoweredRailBlock = PoweredRailBlock(properties)

fun PressurePlateBlock(
    blockSetType: BlockSetType,
    properties: BlockBehaviour.Properties,
): PressurePlateBlock = PressurePlateBlock(blockSetType, properties)

fun PumpkinBlock(
    properties: BlockBehaviour.Properties,
): PumpkinBlock = PumpkinBlock(properties)

fun RailBlock(
    properties: BlockBehaviour.Properties,
): RailBlock = RailBlock(properties)

fun RedstoneTorchBlock(
    properties: BlockBehaviour.Properties,
): RedstoneTorchBlock = RedstoneTorchBlock(properties)

fun RedstoneWallTorchBlock(
    properties: BlockBehaviour.Properties,
): RedstoneWallTorchBlock = RedstoneWallTorchBlock(properties)

fun RepeaterBlock(
    properties: BlockBehaviour.Properties,
): RepeaterBlock = RepeaterBlock(properties)

fun RootsBlock(
    properties: BlockBehaviour.Properties,
): RootsBlock = RootsBlock(properties)

fun SaplingBlock(
    treeGenerator: TreeGrower,
    properties: BlockBehaviour.Properties,
): SaplingBlock = SaplingBlock(treeGenerator, properties)

fun ScaffoldingBlock(
    properties: BlockBehaviour.Properties,
): ScaffoldingBlock = ScaffoldingBlock(properties)

fun SeaPickleBlock(
    properties: BlockBehaviour.Properties,
): SeaPickleBlock = SeaPickleBlock(properties)

fun SeagrassBlock(
    properties: BlockBehaviour.Properties,
): SeagrassBlock = SeagrassBlock(properties)

fun SkullBlock(
    skullBlockType: SkullBlock.Type,
    properties: BlockBehaviour.Properties,
): SkullBlock = SkullBlock(skullBlockType, properties)

fun SmithingTableBlock(
    properties: BlockBehaviour.Properties,
): SmithingTableBlock = SmithingTableBlock(properties)

fun SmokerBlock(
    properties: BlockBehaviour.Properties,
): SmokerBlock = SmokerBlock(properties)

fun SnowLayerBlock(
    properties: BlockBehaviour.Properties,
): SnowLayerBlock = SnowLayerBlock(properties)

fun SnowyDirtBlock(
    properties: BlockBehaviour.Properties,
): SnowyDirtBlock = SnowyDirtBlock(properties)

fun SpawnerBlock(
    properties: BlockBehaviour.Properties,
): SpawnerBlock = SpawnerBlock(properties)

fun SpongeBlock(
    properties: BlockBehaviour.Properties,
): SpongeBlock = SpongeBlock(properties)

fun StairBlock(
    baseBlock: Block,
    properties: BlockBehaviour.Properties
): StairBlock = StairBlock(baseBlock.defaultBlockState(), properties)

fun StemBlock(
    gourdBlock: ResourceKey<Block>,
    attachedStemBlock: ResourceKey<Block>,
    pickBlockItem: ResourceKey<Item>,
    properties: BlockBehaviour.Properties,
): StemBlock = StemBlock(gourdBlock, attachedStemBlock, pickBlockItem, properties)

fun StructureBlock(
    properties: BlockBehaviour.Properties,
): StructureBlock = StructureBlock(properties)

fun StructureVoidBlock(
    properties: BlockBehaviour.Properties,
): StructureVoidBlock = StructureVoidBlock(properties)

fun SugarCaneBlock(
    properties: BlockBehaviour.Properties,
): SugarCaneBlock = SugarCaneBlock(properties)

fun TallGrassBlock(
    properties: BlockBehaviour.Properties,
): TallGrassBlock = TallGrassBlock(properties)

fun TorchBlock(
    particle: SimpleParticleType,
    properties: BlockBehaviour.Properties,
): TorchBlock = TorchBlock(particle, properties)

fun TransparentBlock(
    properties: BlockBehaviour.Properties,
): TransparentBlock = TransparentBlock(properties)

fun TrapDoorBlock(
    blockSetType: BlockSetType,
    properties: BlockBehaviour.Properties,
): TrapDoorBlock = TrapDoorBlock(blockSetType, properties)

fun WallSkullBlock(
    skullBlockType: SkullBlock.Type,
    properties: BlockBehaviour.Properties,
): WallSkullBlock = WallSkullBlock(skullBlockType, properties)

fun WallTorchBlock(
    particle: SimpleParticleType,
    properties: BlockBehaviour.Properties,
): WallTorchBlock = WallTorchBlock(particle, properties)

fun WaterlilyBlock(
    properties: BlockBehaviour.Properties,
): WaterlilyBlock = WaterlilyBlock(properties)

fun WaterloggedTransparentBlock(
    properties: BlockBehaviour.Properties,
): WaterloggedTransparentBlock = WaterloggedTransparentBlock(properties)

fun WeatheringCopperDoorBlock(
    blockSetType: BlockSetType,
    oxidizationLevel: WeatheringCopper.WeatherState,
    properties: BlockBehaviour.Properties,
): WeatheringCopperDoorBlock = WeatheringCopperDoorBlock(blockSetType, oxidizationLevel, properties)

fun WeatheringCopperGrateBlock(
    oxidizationLevel: WeatheringCopper.WeatherState,
    properties: BlockBehaviour.Properties,
): WeatheringCopperGrateBlock = WeatheringCopperGrateBlock(oxidizationLevel, properties)

fun WeatheringCopperTrapDoorBlock(
    blockSetType: BlockSetType,
    oxidizationLevel: WeatheringCopper.WeatherState,
    properties: BlockBehaviour.Properties,
): WeatheringCopperTrapDoorBlock = WeatheringCopperTrapDoorBlock(blockSetType, oxidizationLevel, properties)

fun WeightedPressurePlateBlock(
    weight: Int,
    blockSetType: BlockSetType,
    properties: BlockBehaviour.Properties,
): WeightedPressurePlateBlock = WeightedPressurePlateBlock(weight, blockSetType, properties)

fun WetSpongeBlock(
    properties: BlockBehaviour.Properties,
): WetSpongeBlock = WetSpongeBlock(properties)

fun WitherWallSkullBlock(
    properties: BlockBehaviour.Properties,
): WitherWallSkullBlock = WitherWallSkullBlock(properties)

fun WoolCarpetBlock(
    dyeColor: DyeColor,
    properties: BlockBehaviour.Properties,
): WoolCarpetBlock = WoolCarpetBlock(dyeColor, properties)
