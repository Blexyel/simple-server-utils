package wtf.blexyel.ssu.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public class ThorCommand {
  public static int command(CommandContext<CommandSourceStack> context) {
    try {
      Entity target = EntityArgument.getEntity(context, "target");
      Vec3 targetpos = target.position();
      ServerLevel level = context.getSource().getLevel();

      CompoundTag tag = new CompoundTag();
      tag.putString("id", "minecraft:lightning_bolt");

      Entity entity = EntityType.loadEntityRecursive(tag, level, EntitySpawnReason.COMMAND, (e) -> {
        e.setPos(targetpos.x, targetpos.y, targetpos.z);
        return e;
      });
      if (entity != null)
        level.addFreshEntity(entity);

      context.getSource().sendSuccess(() -> Component.literal(target.getName().getString() + " has been struck by Thor"), false);
      return 1;
    } catch (CommandSyntaxException e) {
      context.getSource().sendFailure(Component.literal("You must specify a target!"));
      return 0;
    }
  }
}
