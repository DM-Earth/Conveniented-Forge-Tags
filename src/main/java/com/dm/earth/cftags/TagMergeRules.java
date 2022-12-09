package com.dm.earth.cftags;

import static com.dm.earth.cftags.TagMergeRule.register;

public class TagMergeRules {
    public static final TagMergeRule INGOTS = register(new TagMergeRule.Simple("ingots"));
    public static final TagMergeRule PLATES = register(new TagMergeRule.Simple("plates"));
    public static final TagMergeRule TINY_DUSTS = register(new TagMergeRule.Simple("tiny_dusts"));
    public static final TagMergeRule SMALL_DUSTS = register(new TagMergeRule.Simple("small_dusts"));
    public static final TagMergeRule DUSTS = register(new TagMergeRule.Simple("dusts"));
    public static final TagMergeRule NUGGETS = register(new TagMergeRule.Simple("nuggets"));
    public static final TagMergeRule RODS = register(new TagMergeRule.Simple("rods"));

    public static final TagMergeRule ORES = register(new TagMergeRule.Simple("ores"));
    public static final TagMergeRule RAW_ORE_BLOCKS = register(new TagMergeRule.Common("blocks", "storage_blocks"));

    public static void load() {
    }
}
