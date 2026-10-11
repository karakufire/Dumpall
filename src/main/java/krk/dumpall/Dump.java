package krk.dumpall;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeManager;
//import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Dump {
    static Path DUMP_OUTPUT = Path.of("./dumpall");

    private Dump() {
    }

    public static void run(MinecraftServer server) {
        var registryFactory = KeyedRegistry.Factory.newInstance((Registry<?> registry) -> registry.keySet().stream().toList());
        var tagRegsFactory = KeyedRegistry.Factory.newInstance((Collection<? extends TagKey<?>> tagKeys) -> tagKeys.stream().map(TagKey::location).toList());

        var registries = new TreeSet<KeyedRegistry<?>>() {{
            // Server Registries
            addAll(server.registries().compositeAccess().registries()
                    .sorted((a, b) -> Objects.compare(a.key().location(), b.key().location(), ResourceLocation::compareNamespaced))
                    .map(r -> registryFactory.build(r.key().location(), r.value()))
                    .toList());
            addAll(server.registries().compositeAccess().registries()
                    .sorted((a, b) -> Objects.compare(a.key().location(), b.key().location(), ResourceLocation::compareNamespaced))
                    .map(r -> tagRegsFactory.build(r.key().location().withPrefix("tags/"), r.value().getTagNames().toList()))
                    .toList());
            // Builtin Registries
            addAll(BuiltInRegistries.REGISTRY.stream()
                    .sorted((a, b) -> Objects.compare(a.key().location(), b.key().location(), ResourceLocation::compareNamespaced))
                    .map(r -> registryFactory.build(r.key().location(), r))
                    .toList());
            addAll(BuiltInRegistries.REGISTRY.stream()
                    .sorted((a, b) -> Objects.compare(a.key().location(), b.key().location(), ResourceLocation::compareNamespaced))
                    .map(r -> tagRegsFactory.build(r.key().location().withPrefix("tags/"), r.getTagNames().toList()))
                    .toList());
            // Reloadable Registries
            addAll(server.reloadableRegistries().get().registries()
                    .sorted((a, b) -> a.key().location().compareNamespaced(b.key().location()))
                    .map(r -> registryFactory.build(r.key().location(), r.value()))
                    .toList());
            addAll(server.reloadableRegistries().get().registries()
                    .sorted((a, b) -> a.key().location().compareNamespaced(b.key().location()))
                    .map(r -> tagRegsFactory.build(r.key().location().withPrefix("tags/"), r.value().getTagNames().toList()))
                    .toList());
        }};

        var summary = registries.stream().map(r -> String.format("%s\t%s", r.key, r.entries().size())).collect(Collectors.joining("\n"));
        dump(summary, Paths.get("summary.tsv"));
        for (var registry : registries) {
            dump(registry);
        }
    }

    private static void dump(KeyedRegistry<?> registry) {
        var text = registry.entries().stream()
                .sorted(ResourceLocation::compareNamespaced)
                .map(ResourceLocation::toString)
                .collect(Collectors.joining("\n"));
        var path = Paths.get(registry.key.getNamespace(), registry.key.getPath() + ".tsv");
        dump(text, path);
    }

    private static void dump(String text, Path filePath) {
        var dist = DUMP_OUTPUT.resolve(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(
                Files.createDirectories(dist.getParent()).resolve(dist.getFileName()),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            writer.write(text);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            Main.LOGGER.error("", e);
        }
    }

    private static abstract class KeyedRegistry<R> implements Comparable<KeyedRegistry<R>> {
        public final ResourceLocation key;
        public final R registry;

        private KeyedRegistry(ResourceLocation key, R registry) {
            this.key = key;
            this.registry = registry;
        }

        public abstract List<ResourceLocation> entries();

        @Override
        public String toString() {
            return String.format("KeyedRegistry[%s]:%s", key, registry.hashCode());
        }

        @Override
        public int compareTo(KeyedRegistry<R> o) {
            return this.key.compareNamespaced(o.key);
        }

        public static class Factory<R> {
            private final Function<R, List<ResourceLocation>> enumerator;

            private Factory(Function<R, List<ResourceLocation>> enumerator) {
                this.enumerator = enumerator;
            }

            public KeyedRegistry<R> build(ResourceLocation key, R registry) {
                return new KeyedRegistry<>(key, registry) {
                    @Override
                    public List<ResourceLocation> entries() {
                        return enumerator.apply(registry);
                    }
                };
            }

            public static <R> Factory<R> newInstance(Function<R, List<ResourceLocation>> enumerator) {
                return new Factory<>(enumerator);
            }
        }
    }
}
