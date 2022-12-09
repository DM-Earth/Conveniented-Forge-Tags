package com.dm.earth.cftags;

import static com.dm.earth.cftags.TagMergeRule.register;

public class TagMergeRules {
    public static final TagMergeRule INGOTS = register(new TagMergeRule.Simple("ingots"));
    public static final TagMergeRule PLATES = register(new TagMergeRule.Simple("plates"));
    public static final TagMergeRule TINY_DUSTS = register(new TagMergeRule.Simple("tiny_dusts"));
    public static final TagMergeRule SMALL_DUSTS = register(new TagMergeRule.Simple("small_dusts"));
    public static final TagMergeRule DUSTS = new TagMergeRule.Simple("dusts");
    public static final TagMergeRule NUGGETS = register(new TagMergeRule.Simple("nuggets"));
    public static final TagMergeRule RODS = register(new TagMergeRule.Simple("rods"));

    public static final TagMergeRule DYES = register(new TagMergeRule.Simple("dyes"));
    public static final TagMergeRule SEEDS = register(new TagMergeRule.Simple("seeds"));

    public static final TagMergeRule ORES = register(new TagMergeRule.Simple("ores"));
    public static final TagMergeRule RAW_ORE_BLOCKS = register(new TagMergeRule.Common("blocks", "storage_blocks"));
    public static final TagMergeRule RAW_ORES = register(new TagMergeRule.Custom(
            f -> f.startsWith("raw_ores") || f.startsWith("raw_materials/")
                    ? "raw_" + f.replaceAll("raw_ores/", "").replaceAll("raw_materials/", "") + "_ores"
                    : null,
            c -> c.startsWith("raw_") && c.endsWith("_ores")
                    ? "raw_ores/" + c.replaceAll("raw_", "").replaceAll("_ores", "")
                    : null));

    public static void load() {
        register(DUSTS);
    }
}
