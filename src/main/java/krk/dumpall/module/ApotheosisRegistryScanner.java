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

public final class ApotheosisRegistryScanner {

    public static void collectRegistries(ScanRegistriesEvent event) {
        event.addRegistry(Apotheosis.loc("affixes"), AffixRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(Apotheosis.loc("bosses"), BossRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(Apotheosis.loc("minibosses"), MinibossRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(Apotheosis.loc("affix_loot_entries"), AffixLootRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(Apotheosis.loc("rarities"), RarityRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(Apotheosis.loc("gems"), GemRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(Apotheosis.loc("rogue_spawners"), RogueSpawnerRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(Apotheosis.loc("enchanting_stats"), EnchantingStatRegistry.INSTANCE, DynamicRegistry::getKeys);

        event.addRegistry(Apotheosis.loc("wanderer_trades"), WandererTradesRegistry.INSTANCE, DynamicRegistry::getKeys);
    }
}
