package krk.dumpall.module;

import krk.dumpall.core.ScanRegistriesEvent;
import krk.dumpall.core.Utils;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.world.level.storage.loot.LootDataType;

import java.util.stream.StreamSupport;

public final class MinecraftRegisterScanner {
    private static final String NSMC = "minecraft";

    public static void collectRegistries(ScanRegistriesEvent event) {
        event.getServer().registries().compositeAccess().registries()
                .map(RegistryAccess.RegistryEntry::value)
                .forEach(event::addRegistry);
        event.addRegistry(
                Utils.rl("advancement"),
                event.getServer().getAdvancements(),
                m -> m.getAllAdvancements().stream().map(Advancement::getId).toList()
        );
        event.addRegistry(
                Utils.rl("function"),
                event.getServer().getFunctions(),
                Utils.fn(ServerFunctionManager::getFunctionNames).andThen(Iterable::spliterator).andThen(s -> StreamSupport.stream(s, false).toList())
        );
        event.addRegistry(
                Utils.rl("tags/function"),
                event.getServer().getFunctions(),
                Utils.fn(ServerFunctionManager::getTagNames).andThen(Iterable::spliterator).andThen(s -> StreamSupport.stream(s, false).toList())
        );
        event.addRegistry(
                Utils.rl("item_modifier"),
                event.getServer().getLootData(),
                m -> m.getKeys(LootDataType.MODIFIER).stream().toList()
        );
        event.addRegistry(
                Utils.rl("loot_table"),
                event.getServer().getLootData(),
                m -> m.getKeys(LootDataType.TABLE).stream().toList()
        );
        event.addRegistry(
                Utils.rl("predicate"),
                event.getServer().getLootData(),
                m -> m.getKeys(LootDataType.PREDICATE).stream().toList()
        );
        event.addRegistry(
                Utils.rl("recipe"),
                event.getServer().getRecipeManager(),
                m -> m.getRecipeIds().toList()
        );
        BuiltInRegistries.REGISTRY.stream()
                .forEach(event::addRegistry);

    }
}
