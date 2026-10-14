package krk.dumpall.module;

import krk.dumpall.core.ScanRegistriesEvent;
import krk.dumpall.core.Utils;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.ServerFunctionManager;

import java.util.stream.StreamSupport;

public final class MinecraftRegistryScanner {
    public static void collectRegistries(ScanRegistriesEvent event) {
        var server = event.getServer();
        server.registries().compositeAccess().registries()
                .map(RegistryAccess.RegistryEntry::value)
                .forEach(event::addRegistry);
        event.addRegistry(
                Utils.mcrl("advancement"),
                server.getAdvancements(),
                m -> m.getAllAdvancements().stream().map(AdvancementHolder::id).toList()
        );
        event.addRegistry(
                Utils.mcrl("function"),
                server.getFunctions(),
                Utils.fn(ServerFunctionManager::getFunctionNames).andThen(Iterable::spliterator).andThen(s -> StreamSupport.stream(s, false).toList())
        );
        event.addRegistry(
                Utils.mcrl("tags/function"),
                server.getFunctions(),
                Utils.fn(ServerFunctionManager::getTagNames).andThen(Iterable::spliterator).andThen(s -> StreamSupport.stream(s, false).toList())
        );
        event.addRegistry(
                Utils.mcrl("recipe"),
                server.getRecipeManager(),
                m -> m.getRecipeIds().toList()
        );
        BuiltInRegistries.REGISTRY.stream()
                .forEach(event::addRegistry);
        server.reloadableRegistries().get().registries()
                .map(RegistryAccess.RegistryEntry::value)
                .forEach(event::addRegistry);
    }
}
