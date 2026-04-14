package wtf.blexyel.ssu.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.function.BooleanSupplier;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.blexyel.ssu.misc.TpsStuff;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
  @Inject(method = "tickServer", at = @At("TAIL"))
  public void tickServer(
      BooleanSupplier haveTime, CallbackInfo ci, @Local(name = "tickTime") long tickTime) {
    TpsStuff.setLastTickSample(tickTime);
  }
}
