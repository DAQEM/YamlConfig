package com.daqem.yamlconfig.networking.s2c;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import com.daqem.yamlconfig.registry.YamlConfigRegistry;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ClientboundSyncConfigPacket implements CustomPacketPayload {

    private final IConfig config;
    private final Map<String, ?> data;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncConfigPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeUtf(packet.config.getModId());
                buf.writeUtf(packet.config.getName());

                buf.writeMap(packet.config.getSyncEntries(),
                        FriendlyByteBuf::writeUtf,
                        (entryBuf, entry) -> {
                            entryBuf.writeResourceLocation(entry.getType().getId());
                            //noinspection unchecked
                            ((IConfigEntry<Object>) entry).getType().getSerializer()
                                    .toNetwork(
                                            (RegistryFriendlyByteBuf) entryBuf,
                                            ((IConfigEntry<Object>) entry),
                                            entry.get());
                        });

            },
            buf -> {
                String modId = buf.readUtf();
                String name = buf.readUtf();

                Map<String, ?> data = buf.readMap(FriendlyByteBuf::readUtf,
                        entryBuf -> {
                    IConfigEntryType<? extends IConfigEntry<?>, ?> type = YamlConfigRegistry.CONFIG_ENTRY.get(buf.readResourceLocation());
                    return Objects.requireNonNull(type).getSerializer().fromNetwork((RegistryFriendlyByteBuf) entryBuf);
                });

                return new ClientboundSyncConfigPacket(YamlConfig.CONFIG_MANAGER.getConfig(modId, name), data);
            }
    );

    public ClientboundSyncConfigPacket(IConfig config) {
        this.config = config;
        this.data = null;
    }

    public ClientboundSyncConfigPacket(IConfig config, Map<String, ?> data) {
        this.config = config;
        this.data = data;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return YamlConfigNetworking.CLIENTBOUND_SYNC_CONFIG;
    }

    @Environment(EnvType.CLIENT)
    public static void handleClientSide(ClientboundSyncConfigPacket packet, NetworkManager.PacketContext context) {
        packet.config.sync(packet.data);
    }
}
