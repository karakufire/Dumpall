package krk.dumpall.module;

import krk.dumpall.core.ScanRegistriesEvent;
import krk.dumpall.core.Utils;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.world.level.storage.loot.ItemModifierManager;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.PredicateManager;

import java.util.stream.StreamSupport;

public final class MinecraftRegistryScanner {
    public static void collectRegistries(ScanRegistriesEvent event) {
        event.getServer().registryAccess().registries()
                .map(RegistryAccess.RegistryEntry::value)
                .forEach(event::addRegistry);
        event.addRegistry(
                Utils.mcrl("advancement"),
                event.getServer().getAdvancements(),
                m -> m.getAllAdvancements().stream().map(Advancement::getId).toList()
        );
        event.addRegistry(
                Utils.mcrl("function"),
                event.getServer().getFunctions(),
                Utils.fn(ServerFunctionManager::getFunctionNames).andThen(Iterable::spliterator).andThen(s -> StreamSupport.stream(s, false).toList())
        );
        event.addRegistry(
                Utils.mcrl("tags/function"),
                event.getServer().getFunctions(),
                Utils.fn(ServerFunctionManager::getTagNames).andThen(Iterable::spliterator).andThen(s -> StreamSupport.stream(s, false).toList())
        );
        event.addRegistry(
                Utils.mcrl("item_modifier"),
                event.getServer().getItemModifierManager(),
                ItemModifierManager::getKeys
        );
        event.addRegistry(
                Utils.mcrl("loot_table"),
                event.getServer().getLootTables(),
                LootTables::getIds
        );
        event.addRegistry(
                Utils.mcrl("predicate"),
                event.getServer().getPredicateManager(),
                PredicateManager::getKeys
        );
        event.addRegistry(
                Utils.mcrl("recipe"),
                event.getServer().getRecipeManager(),
                m -> m.getRecipeIds().toList()
        );
        BuiltinRegistries.REGISTRY.stream()
                .forEach(event::addRegistry);

    }
}
