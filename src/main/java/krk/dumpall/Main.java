package krk.dumpall;

import com.mojang.logging.LogUtils;
import krk.dumpall.core.CommandDumpall;
import krk.dumpall.module.ApotheosisRegistryScanner;
import krk.dumpall.module.ApothicEnchantingRegistryScanner;
import krk.dumpall.module.MinecraftRegistryScanner;
import krk.dumpall.module.PlaceboRegistryScanner;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "dumpall";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
        loadModules();
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDumpall.register(event.getDispatcher());
    }

    private void loadModules() {
        LOGGER.info("Loading Dumpall Modules");
        NeoForge.EVENT_BUS.addListener(MinecraftRegistryScanner::collectRegistries);
        var modList = ModList.get();
        if (modList.isLoaded("placebo")) {
            NeoForge.EVENT_BUS.addListener(PlaceboRegistryScanner::collectRegistries);
        }
        if (modList.isLoaded("apothic_enchanting")) {
            NeoForge.EVENT_BUS.addListener(ApothicEnchantingRegistryScanner::collectRegistries);
        }
        if (modList.isLoaded("apotheosis")) {
            NeoForge.EVENT_BUS.addListener(ApotheosisRegistryScanner::collectRegistries);
        }
    }

}
