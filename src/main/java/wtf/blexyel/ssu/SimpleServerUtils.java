package wtf.blexyel.ssu;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wtf.blexyel.ssu.commands.ModListCommand;

public class SimpleServerUtils implements ModInitializer {
  public static final String MOD_ID = "ssu";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  public static final GameRule<Boolean> TIMESYNC_ENABLED =
      GameRuleBuilder.forBoolean(false)
          .category(GameRuleCategory.MISC)
          .buildAndRegister(
              Identifier.fromNamespaceAndPath(SimpleServerUtils.MOD_ID, "timesync_enabled"));

  public static final GameRule<Integer> TIMESYNC_OFFSET =
      GameRuleBuilder.forInteger(0)
          .category(GameRuleCategory.MISC)
          .buildAndRegister(
              Identifier.fromNamespaceAndPath(SimpleServerUtils.MOD_ID, "timesync_offset"));

  @Override
  public void onInitialize() {
    // ### Command Registration ### //
    CommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess, environment) -> {
          dispatcher.register(
              Commands.literal("ml")
                  .requires(
                      source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                  .executes(ModListCommand::command));
          dispatcher.register(
              Commands.literal("modlist")
                  .requires(
                      source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                  .executes(ModListCommand::command));
        });
    // ### Command Registration ### //

    // ### Time Syncing ### //
    var ref = new Object() {
      ZonedDateTime now =
          ZonedDateTime.now(ZoneId.of("UTC"));
    };
    ServerTickEvents.END_LEVEL_TICK.register(
        level -> {
          GameRules gamerules = level.getGameRules();
          boolean isTimeSyncEnabled = gamerules.get(SimpleServerUtils.TIMESYNC_ENABLED);
          if (level.getServer().overworld() == level && isTimeSyncEnabled) {
            if (gamerules.get(GameRules.ADVANCE_TIME)) {
              gamerules.set(GameRules.ADVANCE_TIME, false, level.getServer());
            }
            // long time = server.getLevelData().getGameTime();
            Holder<@NotNull DimensionType> dimensionType = level.dimensionTypeRegistration();
            Optional<Holder<@NotNull WorldClock>> clock = dimensionType.value().defaultClock();
            ServerClockManager clockmanager = level.clockManager();
            if (clock.isEmpty()) return;
            ref.now = ref.now
                    .plusHours(
                            Math.clamp(
                                level.getGameRules().get(SimpleServerUtils.TIMESYNC_OFFSET),
                                -12,
                                12));
            int totalSeconds = ((ref.now.getHour() * 60) + ref.now.getMinute()) * 60 + ref.now.getSecond();
            int ticks = (((totalSeconds * 24000) / 86400) + 18000) % 24000;
            long time = clockmanager.getTotalTicks(clock.get());
            long dayCounter = (long) (Math.floor((double) time / 24000)) * 24000;
            clockmanager.setTotalTicks(clock.get(), dayCounter + ticks);
          }
        }
        // ### Time Syncing ### //
        );
  }
}
