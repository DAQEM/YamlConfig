package com.daqem.yamlconfig.impl.config.entry;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IDateTimeConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.DateTimeConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateTimeConfigEntry extends BaseConfigEntry<LocalDateTime> implements IDateTimeConfigEntry {

    private final LocalDateTime minDateTime;
    private final LocalDateTime maxDateTime;

    public DateTimeConfigEntry(String key, LocalDateTime defaultValue) {
        this(key, defaultValue, null, null);
    }

    public DateTimeConfigEntry(String key, LocalDateTime defaultValue, LocalDateTime minDateTime, LocalDateTime maxDateTime) {
        super(key, defaultValue);
        this.minDateTime = minDateTime;
        this.maxDateTime = maxDateTime;
    }

    @Override
    public void validate(LocalDateTime value) throws ConfigEntryValidationException {
        if ((minDateTime != null && value.isBefore(minDateTime)) || (maxDateTime != null && value.isAfter(maxDateTime))) {
            String minDateTime = this.minDateTime != null ? this.minDateTime.format(IDateTimeConfigEntry.DATE_TIME_FORMATTER) : null;
            String maxDateTime = this.maxDateTime != null ? this.maxDateTime.format(IDateTimeConfigEntry.DATE_TIME_FORMATTER) : null;
            throw new ConfigEntryValidationException(getKey(), "Value is out of bounds. Expected between " + minDateTime + " and " + maxDateTime);
        }
    }

    @Override
    public IConfigEntryType<IConfigEntry<LocalDateTime>, LocalDateTime> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<LocalDateTime>, LocalDateTime>) (IConfigEntryType<?, ?>) ConfigEntryTypes.DATE_TIME;
    }

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new DateTimeConfigEntryComponent(key, this);
    }

    @Override
    public LocalDateTime getMinDateTime() {
        return minDateTime;
    }

    @Override
    public LocalDateTime getMaxDateTime() {
        return maxDateTime;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            if (minDateTime != null) {
                comments.addValidationParameter("Minimum value: " + minDateTime.format(IDateTimeConfigEntry.DATE_TIME_FORMATTER));
            }
            if (maxDateTime != null) {
                comments.addValidationParameter("Maximum value: " + maxDateTime.format(IDateTimeConfigEntry.DATE_TIME_FORMATTER));
            }
        }
        if (comments.showDefaultValues()) {
            comments.addDefaultValues("'" + getDefaultValue().format(IDateTimeConfigEntry.DATE_TIME_FORMATTER) + "'");
        }
        return comments;
    }

    public static class Serializer implements IConfigEntrySerializer<IDateTimeConfigEntry, LocalDateTime> {

        @Override
        public void encodeNode(IDateTimeConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                configEntry.setValue(LocalDateTime.parse(scalarNode.getValue(), IDateTimeConfigEntry.DATE_TIME_FORMATTER));
            }
        }

        @Override
        public NodeTuple decodeNode(IDateTimeConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            ScalarNode valueNode = new ScalarNode(Tag.STR, configEntry.get().format(IDateTimeConfigEntry.DATE_TIME_FORMATTER), ScalarStyle.SINGLE_QUOTED);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IDateTimeConfigEntry configEntry, LocalDateTime value) {
            buf.writeLong(value.toEpochSecond(ZoneOffset.UTC));
        }

        @Override
        public LocalDateTime valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return LocalDateTime.ofEpochSecond(buf.readLong(), 0, ZoneOffset.UTC);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IDateTimeConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeLong(configEntry.get().toEpochSecond(ZoneOffset.UTC));
            buf.writeLong(configEntry.getMinDateTime() != null ? configEntry.getMinDateTime().toEpochSecond(ZoneOffset.UTC) : Long.MIN_VALUE);
            buf.writeLong(configEntry.getMaxDateTime() != null ? configEntry.getMaxDateTime().toEpochSecond(ZoneOffset.UTC) : Long.MAX_VALUE);
        }

        @Override
        public IDateTimeConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            LocalDateTime value = LocalDateTime.ofEpochSecond(buf.readLong(), 0, ZoneOffset.UTC);
            LocalDateTime minDateTime = buf.readLong() != Long.MIN_VALUE ? LocalDateTime.ofEpochSecond(buf.readLong(), 0, ZoneOffset.UTC) : null;
            LocalDateTime maxDateTime = buf.readLong() != Long.MAX_VALUE ? LocalDateTime.ofEpochSecond(buf.readLong(), 0, ZoneOffset.UTC) : null;
            DateTimeConfigEntry configEntry = new DateTimeConfigEntry(key, value, minDateTime, maxDateTime);
            configEntry.setValue(configEntry.getDefaultValue());
            return configEntry;
        }
    }
}
