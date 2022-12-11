package com.dm.earth.cftags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
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

    @NotNull
    default Optional<Identifier> ctf(Identifier commonTag) {
        String f = this.ctf(commonTag.getPath());
        return f == null || f.equals(commonTag.getPath()) ? Optional.empty() : Optional.of(new Identifier("c", f));
    }

    @NotNull
    default Optional<Identifier> ftc(Identifier forgeTag) {
        String c = this.ftc(forgeTag.getPath());
        return c == null || c.equals(forgeTag.getPath()) ? Optional.empty() : Optional.of(new Identifier("c", c));
    }

    boolean isHighPriority();

    static class Common implements TagMergeRule {
        protected final String matchingPartForge;
        protected final String matchingPartCommon;

        public Common(String cPart, String fPart) {
            this.matchingPartForge = fPart;
            this.matchingPartCommon = cPart;
        }

        @Override
        public @Nullable String ctf(String commonTag) {
            ArrayList<String> parts = new ArrayList<>(List.of(commonTag.split("_")));
            if (commonTag.contains(matchingPartCommon) && parts.size() >= 2 + getSubCount(matchingPartCommon, "_")) {
                Collections.reverse(parts);
                return matchingPartForge + "/" + commonTag.replaceAll("_" + matchingPartCommon, "");
            }
            return null;
        }

        @Override
        public boolean isHighPriority() {
            return this.matchingPartCommon.contains("_");
        }

        @Override
        public @Nullable String ftc(String forgeTag) {
            ArrayList<String> parts = new ArrayList<>();
            parts.addAll(List.of(forgeTag.split("/")));
            if (forgeTag.contains(matchingPartForge) && parts.size() >= 2) {
                Collections.reverse(parts);
                parts.replaceAll(in -> in.equals(matchingPartForge) ? matchingPartCommon : in);
                return String.join("_", parts.toArray(new String[0]));
            }
            return null;
        }

        private static int getSubCount(String main, String sub) {
            return (main.length() - main.replaceAll(sub, "").length()) / sub.length();
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

        @Override
        public boolean isHighPriority() {
            return false;
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

        @Override
        public boolean isHighPriority() {
            return true;
        }

    }
}
