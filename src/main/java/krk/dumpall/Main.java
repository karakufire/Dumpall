package krk.dumpall;

import com.mojang.logging.LogUtils;
import krk.dumpall.core.CommandDumpall;
import krk.dumpall.module.ApotheosisRegistryScanner;
import krk.dumpall.module.MinecraftRegistryScanner;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "dumpall";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Main(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();
        eventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
        this.loadModules();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void onRegisterCommands(RegisterCommandsEvent e) {
        CommandDumpall.register(e.getDispatcher());
    }

    public void loadModules() {
        MinecraftForge.EVENT_BUS.addListener(MinecraftRegistryScanner::collectRegistries);
        if (ModList.get().isLoaded("apotheosis")) {
            LOGGER.info("detect Apotheosis");
            MinecraftForge.EVENT_BUS.addListener(ApotheosisRegistryScanner::collectRegistries);
        }
    }
}
