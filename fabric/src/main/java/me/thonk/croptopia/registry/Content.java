package me.thonk.croptopia.registry;

import me.thonk.common.MiscNames;
import me.thonk.croptopia.Croptopia;
import me.thonk.croptopia.blocks.CroptopiaCropBlock;
import me.thonk.croptopia.blocks.CroptopiaSaplingBlock;
import me.thonk.croptopia.blocks.LeafCropBlock;
import me.thonk.croptopia.generator.CroptopiaSaplingGenerator;
import me.thonk.croptopia.items.CropItem;
import me.thonk.croptopia.items.CroptopiaSaplingItem;
import me.thonk.croptopia.items.Drink;
import me.thonk.croptopia.items.SeedItem;
import me.thonk.croptopia.util.BlockConvertible;
import me.thonk.croptopia.util.PluralInfo;
import me.thonk.croptopia.util.TagConvertible;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static me.thonk.croptopia.Croptopia.createIdentifier;
import static me.thonk.croptopia.registry.FoodRegistry.*;
import static net.minecraft.world.biome.Biome.Category.*;

/**
 * Contains the items and blocks we want and need.
 */
public class Content {

    /**
     * Enum for all commonly used crop categories; always in plural form, if existent.
     */
    public enum TagCategory implements TagConvertible<Item> {

        NONE,
        CROPS,
        FRUITS,
        GRAIN,
        NUTS,
        VEGETABLES;

        String lowerCaseName;
        TagKey<Item> tag;

        TagCategory() {
            lowerCaseName = name().toLowerCase();
            /*if (!name().equals("NONE")) {
                tag = TagKey.of(Registry.ITEM_KEY, new Identifier(MiscNames.INDEPENDENT_TAG, lowerCaseName));
            }*/
        }

        public String getLowerCaseName() {
            return lowerCaseName;
        }

        @Override
        public TagKey<Item> asTag() {
            return tag;
        }
    }

    /**
     * Enum for all (Croptopia) farmland crops.
     */
    public enum Farmland implements ItemConvertible, BlockConvertible, TagConvertible<Item>, PluralInfo {
        ARTICHOKE(true, TagCategory.VEGETABLES, REG_1, SWAMP),
        ASPARAGUS(false, TagCategory.VEGETABLES, REG_3, SWAMP),
        BARLEY(false, TagCategory.GRAIN, REG_1, PLAINS, TAIGA),
        BASIL(false, TagCategory.CROPS, REG_1, JUNGLE),
        BELLPEPPER(true, TagCategory.FRUITS, REG_3, PLAINS),
        BLACKBEAN(true, TagCategory.CROPS, REG_3, FOREST),
        BLACKBERRY(true, TagCategory.FRUITS, REG_3, FOREST, TAIGA),
        BLUEBERRY(true, TagCategory.FRUITS, REG_3, FOREST, TAIGA),
        BROCCOLI(false, TagCategory.VEGETABLES, REG_3, PLAINS),
        CABBAGE(false, TagCategory.VEGETABLES, REG_1, PLAINS),
        CANTALOUPE(true, TagCategory.FRUITS, REG_3, FOREST),
        CAULIFLOWER(false, TagCategory.VEGETABLES, REG_3, FOREST),
        CELERY(false, TagCategory.VEGETABLES, REG_3, FOREST),
        CHILE_PEPPER(true, TagCategory.CROPS, REG_3, PLAINS),
        COFFEE_BEANS("coffee", true, TagCategory.CROPS, REG_3, JUNGLE),
        CORN(false, TagCategory.GRAIN, REG_3, PLAINS),
        CRANBERRY(true, TagCategory.FRUITS, REG_3, SWAMP),
        CUCUMBER(true, TagCategory.VEGETABLES, REG_3, PLAINS),
        CURRANT(true, TagCategory.FRUITS, REG_3, SWAMP),
        EGGPLANT(true, TagCategory.VEGETABLES, REG_3, JUNGLE),
        ELDERBERRY(true, TagCategory.FRUITS, REG_3, FOREST),
        GARLIC(false, TagCategory.VEGETABLES, REG_1, JUNGLE),
        GINGER(true, TagCategory.VEGETABLES, null, SAVANNA),
        GRAPE(true, TagCategory.FRUITS, REG_3, FOREST),
        GREENBEAN(true, TagCategory.VEGETABLES, REG_3, PLAINS),
        GREENONION(true, TagCategory.VEGETABLES, REG_1, JUNGLE),
        HONEYDEW(false, TagCategory.FRUITS, REG_3, JUNGLE),
        HOPS(false, TagCategory.CROPS, null, SAVANNA),
        KALE(false, TagCategory.VEGETABLES, REG_3, PLAINS),
        KIWI(true, TagCategory.FRUITS, REG_3, SAVANNA),
        LEEK(false, TagCategory.VEGETABLES, REG_3, SAVANNA),
        LETTUCE(false, TagCategory.VEGETABLES, REG_3, PLAINS),
        MUSTARD(true, TagCategory.VEGETABLES, null, PLAINS),
        OAT(false, TagCategory.GRAIN, REG_1, PLAINS),
        OLIVE(true, TagCategory.FRUITS, REG_3, SAVANNA),
        ONION(true, TagCategory.VEGETABLES, REG_3, JUNGLE),
        PEANUT(true, TagCategory.CROPS, REG_1, JUNGLE),
        PEPPER(false, TagCategory.CROPS, null, PLAINS),
        PINEAPPLE(true, TagCategory.FRUITS, REG_3, JUNGLE),
        RADISH(true, TagCategory.VEGETABLES, REG_3, FOREST),
        RASPBERRY(true, TagCategory.FRUITS, REG_3, FOREST, TAIGA),
        RHUBARB(false, TagCategory.VEGETABLES, REG_3, JUNGLE),
        RICE(false, TagCategory.GRAIN, REG_1, JUNGLE),
        RUTABAGA(true, TagCategory.VEGETABLES, REG_3, SAVANNA, TAIGA),
        SAGUARO(true, TagCategory.FRUITS, REG_3, DESERT),
        SOYBEAN(true, TagCategory.VEGETABLES, REG_1, PLAINS),
        SPINACH(false, TagCategory.VEGETABLES, REG_3, FOREST),
        SQUASH(true, TagCategory.VEGETABLES, REG_3, SAVANNA, TAIGA),
        STRAWBERRY(true, TagCategory.FRUITS, REG_3, FOREST, TAIGA),
        SWEETPOTATO(true, TagCategory.VEGETABLES, REG_3, PLAINS),
        TEA_LEAVES("tea", true, TagCategory.CROPS, null, FOREST),
        TOMATILLO(true, TagCategory.VEGETABLES, REG_3, FOREST),
        TOMATO(true, TagCategory.VEGETABLES, REG_3, FOREST),
        TURMERIC(false, TagCategory.CROPS, null, SAVANNA),
        TURNIP(true, TagCategory.VEGETABLES, REG_3, JUNGLE),
        VANILLA(false,TagCategory.CROPS, null, JUNGLE),
        YAM(true, TagCategory.VEGETABLES, REG_3, SAVANNA),
        ZUCCHINI(false, TagCategory.VEGETABLES, REG_3, SAVANNA);

        private String lowerCaseName;
        private boolean hasPlural;
        private TagCategory tagegory;
        private TagKey<Item> tag;
        private Item item;
        private Block block;
        private SeedItem seed;

        Farmland(String shortNameVariant, boolean hasPlural, TagCategory tagegory, FoodRegistry foodRegistry, Biome.Category... biomes) {
            Objects.requireNonNull(tagegory);
            lowerCaseName = name().toLowerCase();
            this.hasPlural = hasPlural;
            this.tagegory = tagegory;
            //tag = TagKey.of(Registry.ITEM_KEY, new Identifier(MiscNames.INDEPENDENT_TAG, tagegory.getLowerCaseName() + "/" + lowerCaseName));
            if (foodRegistry == null) {
                item = new CropItem(createGroup());
            }
            else {
                item = new CropItem(createGroup().food(createComponent(foodRegistry)));
            }
            Registry.register(Registry.ITEM, Croptopia.createIdentifier(lowerCaseName), item);
            block = new CroptopiaCropBlock(createCropSettings());
            Croptopia.registerBlock((shortNameVariant != null ? shortNameVariant : lowerCaseName) + "_crop", block);
            seed = new SeedItem(block, createGroup(), biomes);
            Croptopia.registerItem((shortNameVariant != null ? shortNameVariant : lowerCaseName) + "_seed", seed);
        }

        Farmland(boolean hasPlural, TagCategory tagegory, FoodRegistry foodRegistry, Biome.Category... biomes) {
            this(null, hasPlural, tagegory, foodRegistry, biomes);
        }

        public String getLowerCaseName() {
            return lowerCaseName;
        }

        @Override
        public boolean hasPlural() {
            return hasPlural;
        }

        public TagCategory getTagegory() {
            return tagegory;
        }

        @Override
        public Item asItem() {
            return item;
        }

        public Block asBlock() {
            return block;
        }

        @Override
        public TagKey<Item> asTag() {
            return tag;
        }

        public SeedItem getSeed() {
            return seed;
        }
    }

    /**
     * Enum for all (Croptopia) tree crops.
     * <p>
     * Does include {@link Items#APPLE} as {@link Tree#APPLE} (access via {@link Tree#asItem()}.
     * Does not include cinnamon.
     * </p>
     */
    public enum Tree implements ItemConvertible, BlockConvertible, TagConvertible<Item>, PluralInfo {
        ALMOND(true, Blocks.DARK_OAK_LEAVES, TagCategory.NUTS, REG_3, 4, 3, 0),
        // coding for apple requires null for food registry here, other fruits must be eatable
        APPLE(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, null, 5, 3, 0),
        APRICOT(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 5, 2, 0),
        AVOCADO(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 5, 3, 0),
        BANANA(true, Blocks.JUNGLE_LEAVES, TagCategory.FRUITS, REG_3, 4, 8, 0),
        CASHEW(true, Blocks.DARK_OAK_LEAVES, TagCategory.CROPS, REG_1, 4, 3, 0),
        CHERRY(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 5, 3, 0),
        COCONUT(true, Blocks.JUNGLE_LEAVES, TagCategory.FRUITS, REG_1, 5, 2, 3),
        DATE(true, Blocks.JUNGLE_LEAVES, TagCategory.FRUITS, REG_3, 5, 8, 0),
        DRAGONFRUIT(true, Blocks.JUNGLE_LEAVES, TagCategory.FRUITS, REG_3, 5, 7, 0),
        FIG(true, Blocks.JUNGLE_LEAVES, TagCategory.FRUITS, REG_3, 4, 8, 0),
        GRAPEFRUIT(true, Blocks.JUNGLE_LEAVES, TagCategory.FRUITS, REG_3, 4, 8, 0),
        KUMQUAT(false, Blocks.JUNGLE_LEAVES, TagCategory.FRUITS, REG_3, 4, 8, 0),
        LEMON(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 5, 3, 0),
        LIME(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 5, 2, 0),
        MANGO(true, Blocks.JUNGLE_LEAVES, TagCategory.FRUITS, REG_3, 5, 8, 0),
        NECTARINE(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 4, 4, 0),
        NUTMEG(true, Blocks.JUNGLE_LEAVES, TagCategory.CROPS, REG_1, 4, 8, 0),
        ORANGE(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 4, 4, 0),
        PEACH(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 5, 3, 0),
        PEAR(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 5, 2, 0),
        PECAN(true, Blocks.DARK_OAK_LEAVES, TagCategory.NUTS, REG_3, 4, 3, 0),
        PERSIMMON(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 5, 3, 0),
        PLUM(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 5, 3, 0),
        STARFRUIT(true, Blocks.OAK_LEAVES, TagCategory.FRUITS, REG_3, 5, 3, 0),
        WALNUT(true, Blocks.DARK_OAK_LEAVES, TagCategory.NUTS, REG_3, 4, 3, 0);

        private String lowerCaseName;
        private boolean hasPlural;
        private TagCategory tagegory;
        private TagKey<Item> tag;
        private Item item;
        private Block leaves;
        private RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> treeGen;
        private CroptopiaSaplingItem sapling;
        private CroptopiaSaplingBlock saplingBlock;

        Tree(boolean hasPlural, Block leafType, TagCategory tagegory, FoodRegistry foodRegistry, int iTreeGen, int jTreeGen, int kTreeGen) {
            Objects.requireNonNull(tagegory);
            lowerCaseName = name().toLowerCase();
            this.hasPlural = hasPlural;
            this.tagegory = tagegory;
            //tag = TagKey.of(Registry.ITEM_KEY, new Identifier(MiscNames.INDEPENDENT_TAG, tagegory.getLowerCaseName() + "/" + lowerCaseName));
            if (foodRegistry == null) {
                item = Items.APPLE;
            }
            else {
                item = new CropItem(createGroup().food(createComponent(foodRegistry)));
                Registry.register(Registry.ITEM, Croptopia.createIdentifier(lowerCaseName), item);
            }
            leaves = createLeavesBlock();
            Croptopia.registerBlock(lowerCaseName + "_crop", leaves);
            treeGen = createTreeGen(lowerCaseName + "_tree", iTreeGen, jTreeGen, kTreeGen, leafType, leaves);
            saplingBlock = new CroptopiaSaplingBlock(new CroptopiaSaplingGenerator(() -> treeGen), createSaplingSettings());
            Croptopia.registerBlock(lowerCaseName + "_sapling", saplingBlock);
            sapling = new CroptopiaSaplingItem(saplingBlock, leaves, leafType, createGroup());
            Registry.register(Registry.ITEM, Croptopia.createIdentifier(lowerCaseName + "_sapling"), sapling);
        }

        public String getLowerCaseName() {
            return lowerCaseName;
        }

        @Override
        public boolean hasPlural() {
            return hasPlural;
        }

        public TagCategory getTagegory() {
            return tagegory;
        }

        @Override
        public Item asItem() {
            return item;
        }

        @Override
        public Block asBlock() {
            return leaves;
        }

        @Override
        public TagKey<Item> asTag() {
            return tag;
        }

        public Block getLeaves() {
            return asBlock();
        }

        public RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> getTreeGen() {
            return treeGen;
        }

        public CroptopiaSaplingItem getSapling() {
            return sapling;
        }

        public CroptopiaSaplingBlock getSaplingBlock() {
            return saplingBlock;
        }

    }

    public enum Juice implements ItemConvertible {
        APPLE,
        CRANBERRY,
        GRAPE,
        MELON,
        ORANGE,
        PINEAPPLE,
        SAGUARO,
        TOMATO(false);

        private boolean sweet;
        private Item item;
        private ItemConvertible crop;

        Juice(boolean sweet) {
            this.sweet = sweet; // property not yet used, will be used in upcoming saturation overhaul
            item = new Drink(createGroup().food(createBuilder(REG_5).alwaysEdible().build()).recipeRemainder(Items.GLASS_BOTTLE));
            Registry.register(Registry.ITEM, Croptopia.createIdentifier(name().toLowerCase() + "_juice"), item);
            crop = findCrop(name());
            if (crop == null) {
                throw new IllegalStateException("Unknown crop source");
            }
        }

        Juice() {
            this(true);
        }

        @Override
        public Item asItem() {
            return item;
        }

        public boolean isSweet() {
            return sweet;
        }

        public ItemConvertible getCrop() {
            return crop;
        }
    }

    public enum Jam implements ItemConvertible {
        APRICOT,
        BLACKBERRY,
        BLUEBERRY,
        CHERRY,
        ELDERBERRY,
        GRAPE,
        PEACH,
        RASPBERRY,
        STRAWBERRY;

        private Item item;
        private ItemConvertible crop;

        Jam() {
            item = new Drink(createGroup().food(createBuilder(REG_3).alwaysEdible().build()));
            Registry.register(Registry.ITEM, Croptopia.createIdentifier(name().toLowerCase() + "_jam"), item);
            crop = findCrop(name());
            if (crop == null) {
                throw new IllegalStateException("Unknown crop source");
            }
        }

        @Override
        public Item asItem() {
            return item;
        }

        public ItemConvertible getCrop() {
            return crop;
        }
    }

    public enum Smoothie implements ItemConvertible {
        BANANA,
        STRAWBERRY;

        private boolean sweet;
        private Item item;
        private ItemConvertible crop;

        Smoothie(boolean sweet) {
            this.sweet = sweet;
            item = new Drink(createGroup().food(createBuilder(REG_7).alwaysEdible().build()));
            Registry.register(Registry.ITEM, Croptopia.createIdentifier(name().toLowerCase() + "_smoothie"), item);
            crop = findCrop(name());
            if (crop == null) {
                throw new IllegalStateException("Unknown crop source");
            }
        }

        Smoothie() {
            this(true);
        }

        public boolean isSweet() {
            return sweet;
        }

        @Override
        public Item asItem() {
            return item;
        }

        public ItemConvertible getCrop() {
            return crop;
        }
    }

    public enum IceCream implements ItemConvertible {
        MANGO,
        PECAN,
        STRAWBERRY,
        VANILLA;

        private Item item;
        private ItemConvertible crop;

        IceCream() {
            item = new Item(createGroup().food(createComponent(REG_10)));
            Registry.register(Registry.ITEM, Croptopia.createIdentifier(name().toLowerCase() + "_ice_cream"), item);
            crop = findCrop(name());
            if (crop == null) {
                throw new IllegalStateException("Unknown crop source");
            }
        }

        @Override
        public Item asItem() {
            return item;
        }

        public ItemConvertible getCrop() {
            return crop;
        }
    }

    public static Stream<Item> createCropStream() {
        return Stream.concat(
                Arrays.stream(Farmland.values()),
                Arrays.stream(Tree.values())
        ).map(ItemConvertible::asItem);
    }

    public static Stream<Block> createLeafStream() {
        return Stream.concat(
                Arrays.stream(Tree.values()).map(Tree::getLeaves),
                Stream.of(LeavesRegistry.cinnamonLeaves)
        );
    }

    public static Item.Settings createGroup() {
        return new Item.Settings().group(Croptopia.CROPTOPIA_ITEM_GROUP);
    }

    public static FabricBlockSettings createCropSettings() {
        return FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP);
    }

    public static FabricBlockSettings createSaplingSettings() {
        return FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS);
    }

    public static LeafCropBlock createLeavesBlock() {
        return new LeafCropBlock(FabricBlockSettings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(Croptopia::canSpawnOnLeaves).suffocates((a,b,c) -> false).blockVision((a,b,c) -> false));
    }

    /**
     * Returns the crop of a specified name.
     * <p>Search order is
     * <ul>
     *     <li>{@link Farmland}</li>
     *     <li>{@link Tree}</li>
     *     <li>Vanilla crops in alphabetical order save for {@link Items#APPLE} because of {@link Tree#APPLE}</li>
     * </ul>
     * </p>
     * @param name the name of the crop to find, in CAPSLOCK with _ separation
     * @return the crop with the given name or <code>null</code> if there is none.
     */
    public static ItemConvertible findCrop(String name) {
        try {
            return Farmland.valueOf(name);
        } catch (IllegalArgumentException ex) {/* try next */}
        try {
            return Tree.valueOf(name);
        } catch (IllegalArgumentException ex) {/* try next */}
        // test vanilla crops
        return switch (name) {
            // note that apple has already been tested for
            case "BEETROOT" -> Items.BEETROOT;
            case "CARROT" -> Items.CARROT;
            case "MELON" -> Items.MELON_SLICE;
            case "POTATO" -> Items.POTATO;
            case "PUMPKIN" -> Items.PUMPKIN;
            case "WHEAT" -> Items.WHEAT;
            default -> null;
        };
    }

    public static RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> createTreeGen(String name, int i, int j, int k, Block leafType, Block leafCrop) {
        return register(createIdentifier(name),
                Feature.TREE, ((new TreeFeatureConfig.Builder(
                        SimpleBlockStateProvider.of(leafType.getDefaultState()),
                        new StraightTrunkPlacer(i, j, k),
                        new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(leafType.getDefaultState(), 90).add(leafCrop.getDefaultState().with(LeafCropBlock.AGE, 3), 20).build()),
                        new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                        new TwoLayersFeatureSize(1, 0, 2))).ignoreVines().build()));
    }

    public static <FC extends FeatureConfig, F extends Feature<FC>> RegistryEntry<ConfiguredFeature<FC, ?>> register(Identifier id, F feature, FC config) {
        return badRegister(BuiltinRegistries.CONFIGURED_FEATURE, id, new ConfiguredFeature<>(feature, config));
    }

    public static <V extends T, T> RegistryEntry<V> badRegister(Registry<T> registry, Identifier id, V value) {
        return BuiltinRegistries.add((Registry<V>) registry, id, value);
    }

    /**
     * {@link Content} is a static class.
     */
    private Content() {/* No instantiation. */}

}
