package com.dm.earth.cftags.mixin;

import com.dm.earth.cftags.CFTagsMerger;
import com.dm.earth.cftags.TagMergeRule;

import net.fabricmc.fabric.impl.resource.conditions.ResourceConditionsImpl;

import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
@Mixin(ResourceConditionsImpl.class)
public class ResourceConditionsImplMixin {
	@Redirect(method = "tagsPopulatedMatch", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 1))
	private static <U, V> V injected(Map<U, V> instance, Object o) {
		Map<Identifier, Tag<RegistryEntry<?>>> map = (Map<Identifier, Tag<RegistryEntry<?>>>) instance;
		Identifier id = (Identifier) o;
		if (map.containsKey(id) && !map.get(id).values().isEmpty()) return (V) map.get(id);

		if (CFTagsMerger.DEBUG) CFTagsMerger.LOGGER.info("Resolving tag for load conditions: " + id);

		ArrayList<TagMergeRule> rules = new ArrayList<>(TagMergeRule.RULES.stream().filter(TagMergeRule::isHighPriority).toList());
		rules.addAll(TagMergeRule.RULES.stream().filter(rule -> !rule.isHighPriority()).toList());

		for (TagMergeRule rule : rules) {
			Optional<Identifier> i = rule.ftc(id);
			if (i.isPresent() && CFTagsMerger.DEBUG) CFTagsMerger.LOGGER.info("Trying to resolve tag for load conditions: " + id + " with rule: " + rule + " to: " + i.get());
			if (i.isPresent() && map.containsKey(i.get()) && !map.get(i.get()).values().isEmpty())
				return (V) map.get(i.get());
			Optional<Identifier> r = rule.ctf(id);
			if (r.isPresent() && CFTagsMerger.DEBUG) CFTagsMerger.LOGGER.info("Trying to resolve tag for load conditions: " + id + " with rule: " + rule + " to: " + r.get());
			if (r.isPresent() && map.containsKey(r.get()) && !map.get(r.get()).values().isEmpty())
				return (V) map.get(r.get());
		}

		if (CFTagsMerger.DEBUG) CFTagsMerger.LOGGER.info("Resolve tag failed for load conditions: " + id);

		return (V) map.get(id);
	}
}
