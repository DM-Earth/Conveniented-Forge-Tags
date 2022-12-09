package com.dm.earth.cftags;

import static com.dm.earth.cftags.TagMergeRule.register;

public class TagMergeRules {
    public static final TagMergeRule INGOTS = register(new TagMergeRule.Simple("ingots"));
    public static final TagMergeRule PLATES = register(new TagMergeRule.Simple("plates"));
    public static final TagMergeRule SMALL_DUSTS = register(new TagMergeRule.Simple("small_dusts"));
    public static final TagMergeRule DUSTS = register(new TagMergeRule.Simple("dusts"));
    public static final TagMergeRule NUGGETS = register(new TagMergeRule.Simple("nuggets"));
    public static final TagMergeRule RODS = register(new TagMergeRule.Simple("rods"));

    public static void load() {
    }
}
