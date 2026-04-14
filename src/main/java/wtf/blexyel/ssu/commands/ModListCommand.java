package wtf.blexyel.ssu.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import wtf.blexyel.ssu.SimpleServerUtils;

public class ModListCommand {
  public static int command(CommandContext<CommandSourceStack> context) {
    FabricLoader instance = FabricLoader.getInstance();
    StringBuilder mods = new StringBuilder();
    for (ModContainer mod : instance.getAllMods()) {
      ModMetadata metadata = mod.getMetadata();
      String mod_id = metadata.getId();
      if ((mod_id.startsWith("fabric") && !mod_id.equals("fabric-api"))
          || mod_id.startsWith("minecraft")
          || mod_id.startsWith("java")
          || mod_id.startsWith("mixinextras")) continue;

      SimpleServerUtils.LOGGER.debug("{}", metadata.getName());
      mods.append(metadata.getName()).append(", ");
    }
    String modsFixed =
        mods.toString().endsWith(", ") ? mods.substring(0, mods.length() - 2) : mods.toString();
    context.getSource().sendSuccess(() -> Component.literal("§a[mods]: §r" + modsFixed), false);
    return 1;
  }
}
