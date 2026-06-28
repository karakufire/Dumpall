package krk.dumpall;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class Utils {
    private Utils() {
    }

    public static int compareResourceLocations(ResourceLocation a, ResourceLocation b) {
        int crn = Objects.compare(a.getNamespace(), b.getNamespace(), String::compareToIgnoreCase);
        if (crn != 0) {
            return crn;
        } else {
            return Objects.compare(a.getPath(), b.getPath(), String::compareToIgnoreCase);
        }
    }
}
