package com.dm.earth.cftags;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dm.earth.tags_binder.api.LoadTagsCallback;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

public class CFTagsMerger implements ModInitializer {
	public static final String MODID = "cftags";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		LoadTagsCallback.ITEM.register(handler -> {
			LOGGER.info("Syncing Forge tags and Convenient tags!");
			TagMergeRules.load();

			for (TagKey<Item> key : handler.getKeys())
				for (TagMergeRule rule : TagMergeRule.RULES) {
					Identifier id = key.id();

					Optional<Identifier> ftc = rule.ftc(id);
					if (ftc.isPresent()) {
						Identifier commonId = ftc.get();
						handler.register(commonId, handler.get(id).toArray(new Item[0]));
						if (handler.getKeys().stream().anyMatch(tk -> tk.id().equals(commonId)))
							handler.register(id, handler.get(commonId).toArray(new Item[0]));
						break;
					}

					Optional<Identifier> ctf = rule.ctf(id);
					if (ctf.isPresent()) {
						Identifier forgeId = ctf.get();
						handler.register(ctf.get(), handler.get(id).toArray(new Item[0]));
						if (handler.getKeys().stream().anyMatch(tk -> tk.id().equals(forgeId))) {
							Item[] items = handler.get(forgeId).toArray(new Item[0]);
							if (forgeId.getPath().contains("/")) {
								List<String> l = List.of(forgeId.getPath().split("/"));
								StringBuilder builder = new StringBuilder();
								for (int i = 0; i < l.size() - 1; i++) {
									if (i > 0)
										builder.append("/");
									builder.append(l.get(i));
									handler.register(new Identifier("c", builder.toString()), items);
								}
							}
							handler.register(id, items);
						}
						break;
					}
				}
		});
	}

	public static Identifier asIdentifier(String id) {
		return new Identifier(MODID, id);
	}
}
