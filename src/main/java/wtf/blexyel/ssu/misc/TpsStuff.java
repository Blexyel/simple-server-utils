package wtf.blexyel.ssu.misc;

import net.minecraft.server.MinecraftServer;

public class TpsStuff {
  private static long lastTick = 0;

  public static float MSPT(MinecraftServer server) {
    return (float) lastTick / 1000000;
  }

  public static float TPS(MinecraftServer server) {
    return (float) (Math.round((Math.min(20, 1000 / MSPT(server))) * 100.0) / 100.0);
  }

  public static void setLastTickSample(long tick) {
    lastTick = tick;
  }
}