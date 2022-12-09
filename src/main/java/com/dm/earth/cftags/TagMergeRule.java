package com.dm.earth.cftags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

public interface TagMergeRule {
    static final ArrayList<TagMergeRule> RULES = new ArrayList<>();

    static <T extends TagMergeRule> T register(T rule) {
        RULES.add(rule);
        return rule;
    }

    @Nullable
    String ftc(String forgeTag);

    @Nullable
    String ctf(String commonTag);

    default Optional<Identifier> ctf(Identifier commonTag) {
        String f = this.ctf(commonTag.getPath());
        return f == null || f.equals(commonTag.getPath()) ? Optional.empty() : Optional.of(new Identifier("c", f));
    }

    @Nullable
    default Optional<Identifier> ftc(Identifier forgeTag) {
        String c = this.ftc(forgeTag.getPath());
        return c == null || c.equals(forgeTag.getPath()) ? Optional.empty() : Optional.of(new Identifier("c", c));
    }

    static class Common implements TagMergeRule {
        protected final String matchingPartForge;
        protected final String matchingPartCommon;

        public Common(String cPart, String fPart) {
            this.matchingPartForge = fPart;
            this.matchingPartCommon = cPart;
        }

        @Override
        public @Nullable String ctf(String commonTag) {
            ArrayList<String> parts = new ArrayList<>();
            parts.addAll(List.of(commonTag.split("_")));
            if (parts.contains(matchingPartCommon) && parts.size() >= 2) {
                Collections.reverse(parts);
                return matchingPartForge + "/" + commonTag.replaceAll("_" + matchingPartCommon, "");
            }
            return null;
        }

        @Override
        public @Nullable String ftc(String forgeTag) {
            ArrayList<String> parts = new ArrayList<>();
            parts.addAll(List.of(forgeTag.split("/")));
            if (parts.contains(matchingPartForge) && parts.size() >= 2) {
                Collections.reverse(parts);
                parts.replaceAll(in -> in.equals(matchingPartForge) ? matchingPartCommon : in);
                return String.join("_", parts.toArray(new String[0]));
            }
            return null;
        }
    }

    static class Simple extends Common {
        public Simple(String part) {
            super(part, part);
        }
    }

    static class Custom implements TagMergeRule {
        protected final SingleRule ftcRule;
        protected final SingleRule ctfRule;

        public Custom(SingleRule ftc, SingleRule ctf) {
            this.ftcRule = ftc;
            this.ctfRule = ctf;
        }

        @Override
        public @Nullable String ftc(String forgeTag) {
            return ftcRule.convert(forgeTag);
        }

        @Override
        public @Nullable String ctf(String commonTag) {
            return ctfRule.convert(commonTag);
        }

        @FunctionalInterface
        public static interface SingleRule {
            @Nullable
            String convert(String tag);
        }
    }

    static class Direct implements TagMergeRule {
        protected final String forge;
        protected final String common;

        public Direct(String common, String forge) {
            this.common = common;
            this.forge = forge;
        }

        @Override
        public @Nullable String ftc(String forgeTag) {
            return forgeTag.equals(forge) ? common : null;
        }

        @Override
        public @Nullable String ctf(String commonTag) {
            return commonTag.equals(common) ? forge : common;
        }

    }
}
