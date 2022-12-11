# Conveniented Forge Tags

This is a simple Minecraft mod for **Fabric/Quilt 1.18.2** that makes [ForgeTags](https://github.com/AlphaMode/ForgeTags) and [Conventional Tags](https://github.com/FabricMC/fabric/tree/1.18.2) sync with each other.

## Why?

If you're playing Create Fabric (or other mods that use [ForgeTags](https://github.com/AlphaMode/ForgeTags)) with other 'big' native Fabric mods, you may find that they are using two different tags systems and they are not compatible with each other so you can't use a mod's item in another mod's recipe. This mod is designed to solve this problem.

## Builtin Merge Rules

```
Ingots
Double Ingots
Plates
Dusts
Small Dusts
Tiny Dusts
Nuggets
Rods
Wires
Coins
Gears
Dyes
Seeds
Ores
Storage Blocks
Raw Ores
```

## Example

For example, zinc ingots. In [ForgeTags](https://github.com/AlphaMode/ForgeTags) it's `#c:ingots/zinc` and in conventional tags it's `#c:zinc_ingots`.

### Before

```json5
// #c:zinc_ingots
{
    "techreborn:zinc_ingot",
    "another_tech_mod:zinc_ingot"
}
```

```json5
// #c:ingots/zinc
{
    "create:zinc_ingot",
    "alphamode:zinc_ingot"
}
```

### After

```json5
// #c:zinc_ingots
{
    "techreborn:zinc_ingot",
    "another_tech_mod:zinc_ingot",
    "create:zinc_ingot",
    "alphamode:zinc_ingot"
}
```

```json5
// #c:ingots/zinc
{
    "techreborn:zinc_ingot",
    "another_tech_mod:zinc_ingot",
    "create:zinc_ingot",
    "alphamode:zinc_ingot"
}
```

## Api Usage

### Register a `TagMergeRule`

You can use static method `register` in the `TagMergeRule` class to register your custom `TagMergeRule`.

```java
TagMergeRule.register(new TagMergeRule.Simple("double_ingots"));
```
