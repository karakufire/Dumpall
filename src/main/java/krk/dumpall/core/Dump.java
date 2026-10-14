package krk.dumpall.core;

import krk.dumpall.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class Dump {
    static Path DUMP_OUTPUT = Path.of("./dumpall");

    private Dump() {
    }

    public static void run(MinecraftServer server) {
        var event = new ScanRegistriesEvent(server);
        MinecraftForge.EVENT_BUS.post(event);

        var registries = event.result();

        var summary = registries.entrySet().stream()
                .sorted((a, b) -> Objects.compare(a.getKey(), b.getKey(), ResourceLocation::compareNamespaced))
                .map(r -> String.format("%s\t%s", r.getKey(), r.getValue().size()))
                .collect(Collectors.joining("\n"));
        dump(summary, Paths.get("summary.tsv"));
        for (var registry : registries.entrySet()) {
            dump(registry.getKey(), registry.getValue());
        }
    }

    private static void dump(ResourceLocation location, Collection<ResourceLocation> content) {
        var text = content.stream()
                .sorted(ResourceLocation::compareNamespaced)
                .map(ResourceLocation::toString)
                .collect(Collectors.joining("\n"));
        var path = Paths.get(location.getNamespace(), String.format("%s.tsv", location.getPath()));
        dump(text, path);
    }

    private static void dump(String text, Path filePath) {
        var dist = DUMP_OUTPUT.resolve(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(
                Files.createDirectories(dist.getParent()).resolve(dist.getFileName()),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            writer.write(text);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            Main.LOGGER.error("", e);
        }
    }
}
