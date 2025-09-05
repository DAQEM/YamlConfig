package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.gui.component.text.TruncatedTextComponent;
import com.daqem.yamlconfig.YamlConfig;

public class TruncatedKeyTextComponent extends TruncatedTextComponent {

    public TruncatedKeyTextComponent(String key, int maxWidth) {
        super(0, 5, maxWidth, YamlConfig.translatable(key));
    }
}
