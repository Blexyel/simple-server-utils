package wtf.blexyel.ssu.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import wtf.blexyel.ssu.misc.TpsStuff;

public class TpsCommand {
  public static int command(CommandContext<CommandSourceStack> context) {
    MinecraftServer server = context.getSource().getServer();
    context
        .getSource()
        .sendSuccess(
            () ->
                Component.literal(
                    "TPS: " + TpsStuff.TPS(server) + " MSPT: " + TpsStuff.MSPT(server)),
            false);
    return 1;
  }
}
