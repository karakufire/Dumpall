package krk.dumpall.core;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Utils {
    private Utils() {
    }

    public static <T, R> Function<T, R> fn(Function<T, R> function) {
        return function;
    }

    public static <T, U, R> Function<U, R> fnAp0(BiFunction<T, U, R> function, T param0) {
        return (U param1) -> function.apply(param0, param1);
    }

    public static <T, U, R> Function<T, R> fnAp1(BiFunction<T, U, R> function, U param1) {
        return (T param0) -> function.apply(param0, param1);
    }

    public static <T, U, R> Function<U, R> fnAp0S(BiFunction<T, U, R> function, Supplier<T> supplier) {
        return (U param) -> function.apply(supplier.get(), param);
    }

    public static <T, U, R> Function<T, R> fnAp1S(BiFunction<T, U, R> function, Supplier<U> supplier) {
        return (T param) -> function.apply(param, supplier.get());
    }

    public static ResourceLocation mcrl(String path0, String... paths) {
        return rl("minecraft", path0, paths);
    }

    public static ResourceLocation rl(String namespace, String path0, String... paths) {
        StringBuilder sb = new StringBuilder();
        sb.append(path0);
        for (String path : paths) {
            sb.append("/");
            sb.append(path);
        }
        return ResourceLocation.fromNamespaceAndPath(namespace, sb.toString());
    }
}
