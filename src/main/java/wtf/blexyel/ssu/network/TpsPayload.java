package wtf.blexyel.ssu.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import wtf.blexyel.ssu.SimpleServerUtils;

public record TpsPayload(double tps) implements CustomPacketPayload {
  public static final Identifier TPS_PAYLOAD_ID =
      Identifier.fromNamespaceAndPath(SimpleServerUtils.MOD_ID, "tps_payload");
  public static final Type<TpsPayload> TYPE = new Type<>(TPS_PAYLOAD_ID);
  public static final StreamCodec<RegistryFriendlyByteBuf, TpsPayload> CODEC =
      StreamCodec.composite(ByteBufCodecs.DOUBLE, TpsPayload::tps, TpsPayload::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
