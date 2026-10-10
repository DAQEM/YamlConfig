package com.daqem.yamlconfig.networking.s2c;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import com.daqem.yamlconfig.registry.YamlConfigRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClientboundSyncConfigPacket implements CustomPacketPayload {

    public final IConfig config;
    public final Map<String, ?> data;

    private static final StreamCodec<RegistryFriendlyByteBuf, Object> SYNC_VALUE_CODEC = StreamCodec.of(
            (buf, obj) -> {
                if (obj instanceof IConfigEntry<?> entry) {
                    buf.writeIdentifier(entry.getType().getId());
                    //noinspection unchecked
                    ((IConfigEntry<Object>) entry).getType().getSerializer()
                            .valueToNetwork(buf, (IConfigEntry<Object>) entry, entry.get());
                }
            },
            buf -> {
                Identifier typeId = buf.readIdentifier();
                IConfigEntryType<?, ?> type = YamlConfigRegistry.CONFIG_ENTRY.get(typeId)
                        .map(Holder.Reference::value)
                        .orElse(null);
                return Objects.requireNonNull(type).getSerializer().valueFromNetwork(buf);
            }
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, Map<String, Object>> SYNC_DATA_CODEC =
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, SYNC_VALUE_CODEC);

    @SuppressWarnings("unchecked")
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncConfigPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            packet -> packet.config.getModId(),
            ByteBufCodecs.STRING_UTF8,
            packet -> packet.config.getName(),
            SYNC_DATA_CODEC,
            packet -> (Map<String, Object>) (Map<?, ?>) packet.config.getSyncEntries(),
            (modId, name, data) -> new ClientboundSyncConfigPacket(YamlConfig.CONFIG_MANAGER.getConfig(modId, name), data)
    );

    public ClientboundSyncConfigPacket(IConfig config) {
        this(config, null);
    }

    public ClientboundSyncConfigPacket(IConfig config, Map<String, ?> data) {
        this.config = config;
        this.data = data;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return YamlConfigNetworking.CLIENTBOUND_SYNC_CONFIG;
    }
}
