package krk.dumpall.module;

import krk.dumpall.core.ScanRegistriesEvent;
import shadows.apotheosis.Apotheosis;
import shadows.apotheosis.adventure.affix.AffixManager;
import shadows.apotheosis.adventure.affix.socket.gem.GemManager;
import shadows.apotheosis.adventure.boss.BossArmorManager;
import shadows.apotheosis.adventure.boss.BossItemManager;
import shadows.apotheosis.adventure.boss.MinibossManager;
import shadows.apotheosis.adventure.loot.AffixLootManager;
import shadows.apotheosis.adventure.loot.LootRarityManager;
import shadows.apotheosis.adventure.spawner.RandomSpawnerManager;
import shadows.apotheosis.ench.table.EnchantingStatManager;
import shadows.apotheosis.village.wanderer.WandererTradeManager;
import shadows.placebo.json.PlaceboJsonReloadListener;

public class ApotheosisRegistryScanner {
    public static void collectRegistries(ScanRegistriesEvent event) {
        event.addRegistry(Apotheosis.loc("affixes"), AffixManager.INSTANCE, PlaceboJsonReloadListener::getKeys);
        event.addRegistry(Apotheosis.loc("gems"), GemManager.INSTANCE, PlaceboJsonReloadListener::getKeys);
        event.addRegistry(Apotheosis.loc("bosses"), BossItemManager.INSTANCE, PlaceboJsonReloadListener::getKeys);
        event.addRegistry(Apotheosis.loc("boss_gear"), BossArmorManager.INSTANCE, PlaceboJsonReloadListener::getKeys);
        event.addRegistry(Apotheosis.loc("minibosses"), MinibossManager.INSTANCE, PlaceboJsonReloadListener::getKeys);
        event.addRegistry(Apotheosis.loc("affix_loot_entries"), AffixLootManager.INSTANCE, PlaceboJsonReloadListener::getKeys);
        event.addRegistry(Apotheosis.loc("rarities"), LootRarityManager.INSTANCE, PlaceboJsonReloadListener::getKeys);
        event.addRegistry(Apotheosis.loc("random_spawners"), RandomSpawnerManager.INSTANCE, PlaceboJsonReloadListener::getKeys);
        event.addRegistry(Apotheosis.loc("enchanting_stats"), EnchantingStatManager.INSTANCE, PlaceboJsonReloadListener::getKeys);
        event.addRegistry(Apotheosis.loc("wanderer_trades"), WandererTradeManager.INSTANCE, PlaceboJsonReloadListener::getKeys);
    }
}
