package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.gui.component.text.TruncatedTextComponent;
import com.daqem.yamlconfig.YamlConfig;
import net.minecraft.network.chat.Style;

public class TruncatedKeyTextComponent extends TruncatedTextComponent {

    public TruncatedKeyTextComponent(String key, int maxWidth, boolean bold) {
        super(0, 5, maxWidth, YamlConfig.translatable(key).withStyle(Style.EMPTY.withBold(bold)));
    }
}
