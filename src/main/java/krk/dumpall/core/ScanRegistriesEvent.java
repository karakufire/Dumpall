package krk.dumpall.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.neoforged.bus.api.Event;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An event for collecting registries.
 * Note that this event handles normal and tag registry as different.
 */
public class ScanRegistriesEvent extends Event {

    private final MinecraftServer server;
    private final Map<ResourceLocation, RegistryWrapper<?>> registries;

    public ScanRegistriesEvent(MinecraftServer server) {
        this.server = server;
        this.registries = new HashMap<>();
    }

    /**
     * Context server
     * @return {@link MinecraftServer} instance
     */
    public MinecraftServer getServer() {
        return server;
    }

    /**
     * Add Registry instance for listing.<br>
     * Both of normal and tags will be added in this method. Tags' one is added as {@code example:tags/<registry_name>}
     *
     * @param registry Registry instance
     */
    public void addRegistry(Registry<?> registry) {
        this.addRegistry(
                registry.key().location(),
                registry,
                Registry::keySet
        );
        this.addRegistry(
                registry.key().location().withPrefix("tags/"),
                registry,
                r -> r.getTagNames().map(TagKey::location).collect(Collectors.toSet())
        );
    }

    /**
     * Add Registry instance for listing.
     *
     * @param registryName          Registry name. Recommend using an id mapped to datapack directory
     * @param registryLike          Registry instance. Not limited to {@link Registry} instances
     * @param keyCollectionFunction Getter of registered ids
     * @param <R>                   Registry class
     */
    public <R> void addRegistry(ResourceLocation registryName, R registryLike, Function<R, Collection<ResourceLocation>> keyCollectionFunction) {
        this.registries.put(registryName, RegistryWrapper.wrapRegistry(registryLike, keyCollectionFunction));
    }

    /**
     *
     * @param key   Registry name
     * @param clazz Registry class
     * @param <R>   Registry class
     * @return Matched in name and class. If neither matches will return empty.
     */
    public <R> Optional<R> getRegistry(ResourceLocation key, Class<R> clazz) {
        try {
            var ret = clazz.cast(this.registries.get(key));
            return Optional.ofNullable(ret);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    /**
     * Get indexed view of collected registries content.
     *
     * @return id-indexed registered content collection
     */
    public Map<ResourceLocation, Collection<ResourceLocation>> result() {
        return this.registries.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().keyCollection()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Wrapper for access the collection of all registered keys through same method whether normal or tag registries.
     *
     * @param <R> Registry class
     */
    private static abstract class RegistryWrapper<R> {
        private final R registry;

        private RegistryWrapper(R registry) {
            this.registry = registry;
        }

        public R getRegistry() {
            return registry;
        }

        public abstract Collection<ResourceLocation> keyCollection();

        public static <R> RegistryWrapper<R> wrapRegistry(R registryLike, Function<R, Collection<ResourceLocation>> keyCollectionFunction) {
            return new RegistryWrapper<R>(registryLike) {
                @Override
                public Collection<ResourceLocation> keyCollection() {
                    return keyCollectionFunction.apply(registryLike);
                }
            };
        }
    }
}
