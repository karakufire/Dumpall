package krk.dumpall.core;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CommandDumpall {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("dumpall").executes(context -> {
            Dump.run(context.getSource().getServer());
            context.getSource().sendSystemMessage(Component.literal("DumpAll finished"));
            return Command.SINGLE_SUCCESS;
        });

        dispatcher.register(command);
    }
}
