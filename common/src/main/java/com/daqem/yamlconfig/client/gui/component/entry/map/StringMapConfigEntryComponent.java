package com.daqem.yamlconfig.client.gui.component.entry.map;

import com.daqem.uilib.util.ValidationErrors;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.map.IStringMapConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.map.StringMapConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StringMapConfigEntryComponent extends BaseMapConfigEntryComponent<IStringMapConfigEntry>{

    public StringMapConfigEntryComponent(String key, IStringMapConfigEntry configEntry) {
        super(key, configEntry, input -> {
            List<Component> list = new ArrayList<>();
            if (configEntry.getPattern() != null && !input.matches(configEntry.getPattern())) {
                list.add(ValidationErrors.pattern(configEntry.getPattern()));
            }
            if (!configEntry.getValidValues().isEmpty() && !configEntry.getValidValues().contains(input)) {
                list.add(ValidationErrors.validValues(configEntry.getValidValues()));
            }
            return list;
        });
    }

    @Override
    public void applyValue() {
        if (hasInputValidationErrors()) return;

        Map<String, String> map = this.editBoxWidgets.keySet().stream()
            .collect(Collectors.toMap(
                    entry -> entry.keyWidget().getValue(),
                    entry -> entry.valueWidget().getValue()
            ));

        this.getConfigEntry().set(map);
    }
}
