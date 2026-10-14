package krk.dumpall.module;

import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.reload.DynamicRegistry;
import dev.shadowsoffire.placebo.systems.gear.GearSetRegistry;
import dev.shadowsoffire.placebo.systems.mixes.MixRegistry;
import dev.shadowsoffire.placebo.systems.wanderer.WandererTradesRegistry;
import krk.dumpall.core.ScanRegistriesEvent;

public class PlaceboRegistryScanner {
    public static void collectRegistries(ScanRegistriesEvent event) {
        event.addRegistry(Placebo.loc("gear_sets"), GearSetRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Placebo.loc("brewing_mixes"), MixRegistry.INSTANCE, DynamicRegistry::getKeys);
        event.addRegistry(Placebo.loc("wanderer_trades"), WandererTradesRegistry.INSTANCE, DynamicRegistry::getKeys);
    }
}
