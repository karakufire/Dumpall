package krk.dumpall.module;

import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import dev.shadowsoffire.apotheosis.adventure.boss.BossRegistry;
import dev.shadowsoffire.apotheosis.adventure.boss.MinibossRegistry;
import dev.shadowsoffire.apotheosis.adventure.loot.AffixLootRegistry;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.adventure.spawner.RogueSpawnerRegistry;
import dev.shadowsoffire.apotheosis.ench.table.EnchantingStatRegistry;
import dev.shadowsoffire.apotheosis.village.wanderer.WandererTradesRegistry;
import dev.shadowsoffire.placebo.reload.DynamicRegistry;
import krk.dumpall.core.ScanRegistriesEvent;
import krk.dumpall.core.Utils;
import net.minecraft.resources.ResourceLocation;

public final class ApotheosisRegisterScanner {

    public static void collectRegistries(ScanRegistriesEvent event) {
        event.addRegistry(rlApoth("affixes"), AffixRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(rlApoth("bosses"), BossRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(rlApoth("minibosses"), MinibossRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(rlApoth("affix_loot_entries"), AffixLootRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(rlApoth("rarities"), RarityRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(rlApoth("gems"), GemRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(rlApoth("rogue_spawners"), RogueSpawnerRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(rlApoth("enchanting_stats"), EnchantingStatRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(rlApoth("wanderer_trades"), WandererTradesRegistry.INSTANCE, DynamicRegistry::getKeys);
    }

    private static ResourceLocation rlApoth(String path, String... paths) {
        return Utils.rl(Apotheosis.MODID, path, paths);
    }
}
