package krk.dumpall.module;

import dev.shadowsoffire.apothic_enchanting.ApothicEnchanting;
import dev.shadowsoffire.apothic_enchanting.table.EnchantingStatRegistry;
import dev.shadowsoffire.placebo.reload.DynamicRegistry;
import krk.dumpall.core.ScanRegistriesEvent;

public class ApothicEnchantingRegistryScanner {
    public static void collectRegistries(ScanRegistriesEvent event) {
        event.addRegistry(ApothicEnchanting.loc("enchanting_stats"), EnchantingStatRegistry.INSTANCE, DynamicRegistry::getKeys);
    }
}
