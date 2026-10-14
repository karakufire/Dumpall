package krk.dumpall.module;

import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.affix.AffixRegistry;
import dev.shadowsoffire.apotheosis.loot.AffixLootRegistry;
import dev.shadowsoffire.apotheosis.loot.RarityOverrideRegistry;
import dev.shadowsoffire.apotheosis.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.mobs.registries.AugmentRegistry;
import dev.shadowsoffire.apotheosis.mobs.registries.EliteRegistry;
import dev.shadowsoffire.apotheosis.mobs.registries.InvaderRegistry;
import dev.shadowsoffire.apotheosis.socket.gem.ExtraGemBonusRegistry;
import dev.shadowsoffire.apotheosis.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.socket.gem.PurityWeightsRegistry;
import dev.shadowsoffire.apotheosis.spawner.RogueSpawnerRegistry;
import dev.shadowsoffire.apotheosis.tiers.augments.TierAugmentRegistry;
import dev.shadowsoffire.placebo.reload.DynamicRegistry;
import krk.dumpall.core.ScanRegistriesEvent;

public class ApotheosisRegistryScanner {
    public static void collectRegistries(ScanRegistriesEvent event) {
        event.addRegistry(Apotheosis.loc("affixes"), AffixRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Apotheosis.loc("affix_loot_entries"), AffixLootRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Apotheosis.loc("rarity_override"), RarityOverrideRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Apotheosis.loc("rarities"), RarityRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Apotheosis.loc("apothic_augments"), AugmentRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Apotheosis.loc("apothic_elites"), EliteRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Apotheosis.loc("apothic_invaders"), InvaderRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Apotheosis.loc("extra_gem_bonuses"), ExtraGemBonusRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Apotheosis.loc("gems"), GemRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Apotheosis.loc("purity_weights"), PurityWeightsRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Apotheosis.loc("rogue_spawners"), RogueSpawnerRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Apotheosis.loc("tier_augments"), TierAugmentRegistry.INSTANCE, DynamicRegistry::getKeys);

    }
}
