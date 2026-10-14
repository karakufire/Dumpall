package krk.dumpall.core;

import krk.dumpall.Main;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.storage.loot.ItemModifierManager;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Dump {
    static Path DUMP_OUTPUT = Path.of("./dumpall");

    private Dump() {
    }

    public static void run(MinecraftServer server) {
        List<KeyedRegistry<?>> registries = registries();
        List<KeyedRegistry<?>> serverRegistries = serverRegistries(server);
        Map<ResourceLocation, Integer> counter = new LinkedHashMap<>();

        for (var registry : registries) {
            counter.put(registry.key, registry.entries().size());
            dump(registry);
        }

        for (var registry : serverRegistries) {
            counter.put(registry.key, registry.entries().size());
            dump(registry);

        }

        var summary = counter.entrySet().stream()
                .sorted((a, b) -> Objects.compare(a.getKey(), b.getKey(), ResourceLocation::compareNamespaced))
                .map(e -> String.format("%s\t%s", e.getKey().toString(), e.getValue()))
                .collect(Collectors.joining("\n"));
        dump(summary, Paths.get("summary.tsv"));
    }

    private static List<KeyedRegistry<?>> registries() {
        var rfactory = KeyedRegistry.Factory.newInstance((Registry<?> r) -> r.keySet()
                .stream()
                .sorted(ResourceLocation::compareNamespaced)
                .toList());
        var registries = Registry.REGISTRY.keySet().stream()
                .map(rl -> rfactory.build(rl, Registry.REGISTRY.get(rl))).toList();
        var builtins = BuiltinRegistries.REGISTRY.keySet().stream()
                .map(rl -> rfactory.build(rl, BuiltinRegistries.REGISTRY.get(rl))).toList();

        var ffactory = KeyedRegistry.Factory.newInstance((ForgeRegistry<?> r) -> Objects.requireNonNull(r.tags())
                .getTagNames()
                .map(TagKey::location)
                .sorted(ResourceLocation::compareNamespaced)
                .toList()
        );
        var regTags = Registry.REGISTRY.keySet().stream()
                .map(rl -> new Object() {
                    final ResourceLocation key = rl;
                    final ForgeRegistry<?> reg = RegistryManager.ACTIVE.getRegistry(rl);
                })
                .filter(entry -> Objects.nonNull(entry.reg))
                .filter(entry -> Objects.nonNull(entry.reg.tags()))
                .map(entry -> ffactory.build(entry.key.withPrefix("tags/"), entry.reg)).toList();
        var builtinTags = BuiltinRegistries.REGISTRY.keySet().stream()
                .map(rl -> new Object() {
                    final ResourceLocation key = rl;
                    final ForgeRegistry<?> reg = RegistryManager.ACTIVE.getRegistry(rl);
                })
                .filter(entry -> Objects.nonNull(entry.reg))
                .filter(entry -> Objects.nonNull(entry.reg.tags()))
                .map(entry -> ffactory.build(entry.key.withPrefix("tags/"), entry.reg)).toList();

        return Collections.unmodifiableList(new ArrayList<>() {{
            addAll(registries);
            addAll(builtins);
            addAll(regTags);
            addAll(builtinTags);
        }});
    }

    private static List<KeyedRegistry<?>> serverRegistries(MinecraftServer server) {
        return new ArrayList<>() {{
            add(KeyedRegistry.Factory.newInstance((ServerAdvancementManager manager) -> manager.getAllAdvancements().stream()
                            .map(Advancement::getId).sorted(ResourceLocation::compareNamespaced).toList())
                    .build(ResourceLocation.tryParse("minecraft:advancement"), server.getAdvancements()));

            add(KeyedRegistry.Factory.newInstance((ServerFunctionManager manager) -> StreamSupport.stream(manager.getFunctionNames().spliterator(), false)
                            .sorted(ResourceLocation::compareNamespaced).distinct().toList())
                    .build(ResourceLocation.tryParse("minecraft:function"), server.getFunctions()));

            add(KeyedRegistry.Factory.newInstance((ServerFunctionManager manager) -> StreamSupport.stream(manager.getTagNames().spliterator(), false)
                            .sorted(ResourceLocation::compareNamespaced).distinct().toList())
                    .build(ResourceLocation.tryParse("minecraft:tags/function"), server.getFunctions()));

            add(KeyedRegistry.Factory.newInstance((ItemModifierManager manager) -> manager.getKeys().stream()
                            .sorted(ResourceLocation::compareNamespaced).distinct().toList())
                    .build(ResourceLocation.tryParse("minecraft:item_modifier"), server.getItemModifierManager()));

            add(KeyedRegistry.Factory.newInstance((LootTables tables) -> tables.getIds().stream()
                            .sorted(ResourceLocation::compareNamespaced).distinct().toList())
                    .build(ResourceLocation.tryParse("minecraft:loot_table"), server.getLootTables()));

            add(KeyedRegistry.Factory.newInstance((PredicateManager manager) -> manager.getKeys().stream()
                            .sorted(ResourceLocation::compareNamespaced).distinct().toList())
                    .build(ResourceLocation.tryParse("minecraft:predicate"), server.getPredicateManager()));

            add(KeyedRegistry.Factory.newInstance((RecipeManager manager) -> manager.getRecipeIds()
                            .sorted(ResourceLocation::compareNamespaced).distinct().toList())
                    .build(ResourceLocation.tryParse("minecraft:recipe"), server.getRecipeManager()));
        }};
    }

    private static void dump(KeyedRegistry<?> registry) {
        var text = registry.entries()
                .stream()
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

    private static abstract class KeyedRegistry<R> {
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
