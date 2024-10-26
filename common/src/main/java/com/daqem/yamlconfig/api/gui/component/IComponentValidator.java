package com.daqem.yamlconfig.api.gui.component;

import net.minecraft.network.chat.Component;

import java.util.List;

@FunctionalInterface
public interface IComponentValidator {

    List<Component> validate(String value);
}
