package krk.dumpall.module;

import dev.shadowsoffire.placebo.json.GearSetRegistry;
import dev.shadowsoffire.placebo.reload.DynamicRegistry;
import krk.dumpall.core.ScanRegistriesEvent;
import krk.dumpall.core.Utils;

public class PlaceboRegistryScanner {
    public static void collectRegistries(ScanRegistriesEvent event) {
        event.addRegistry(Utils.rl("placebo", "gear_sets"), GearSetRegistry.INSTANCE, DynamicRegistry::getKeys);
    }
}
