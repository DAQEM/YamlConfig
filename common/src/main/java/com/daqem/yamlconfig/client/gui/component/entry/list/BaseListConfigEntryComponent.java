package com.daqem.yamlconfig.client.gui.component.entry.list;

import com.daqem.uilib.gui.widget.ButtonWidget;
import com.daqem.uilib.gui.widget.EditBoxWidget;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.list.IListConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IComponentValidator;
import com.daqem.yamlconfig.client.gui.component.CrossButtonComponent;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.*;

public abstract class BaseListConfigEntryComponent<C extends IListConfigEntry<?>> extends BaseConfigEntryComponent<C> {

    private static final int WIDTH = KEY_WIDTH + GAP_WIDTH + VALUE_WIDTH;

    protected final Map<EditBoxWidget, CrossButtonComponent> editBoxWidgets;
    protected final ButtonWidget addEntryButton;

    private final IComponentValidator validator; // TODO

    public BaseListConfigEntryComponent(String key, C configEntry, IComponentValidator validator) {
        super(key, configEntry, 0, 0, calculateInitialHeight(configEntry), WIDTH);
        this.validator = validator;
        this.editBoxWidgets = createEditBoxWidgets();
        this.addEntryButton = createAddEntryButton();
    }

    private static int calculateInitialHeight(IListConfigEntry<?> configEntry) {
        int entryCount = configEntry.get().size();
        return entryCount * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT * 3 + GAP_WIDTH;
    }

    private ButtonWidget createAddEntryButton() {
        return new ButtonWidget(
                0,
                editBoxWidgets.size() * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH,
                WIDTH,
                DEFAULT_HEIGHT,
                YamlConfig.translatable("gui.add_entry"),
                this::handleAddEntryButtonClick
        );
    }

    private void handleAddEntryButtonClick(Button button) {
        EditBoxWidget newTextBox = createEditBoxWidget(editBoxWidgets.size());
        CrossButtonComponent newCrossButton = createCrossButtonComponent(newTextBox);

        editBoxWidgets.put(newTextBox, newCrossButton);
        this.addWidget(newTextBox);
        this.addWidget(newCrossButton);

        adjustLayoutForNewEntry(button);
    }

    private EditBoxWidget createEditBoxWidget(int index) {
        return new EditBoxWidget(
                Minecraft.getInstance().font,
                0,
                index * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH,
                WIDTH,
                DEFAULT_HEIGHT,
                Component.empty()
        ) {
//            @Override
//            public List<Component> validateInput(String input) {
//                return validator.validate(input);
//            }
            //TODO
        };
    }

    private CrossButtonComponent createCrossButtonComponent(EditBoxWidget EditBoxWidget) {
        return new CrossButtonComponent(
                WIDTH + GAP_WIDTH + 3,
                EditBoxWidget.getY() + 3,
                button -> handleRemoveEntryButtonClick(EditBoxWidget, (CrossButtonComponent) button)
        );
    }

    private void handleRemoveEntryButtonClick(EditBoxWidget EditBoxWidget, CrossButtonComponent crossButtonComponent) {
        removeEntry(EditBoxWidget, crossButtonComponent);
    }

    private void removeEntry(EditBoxWidget editBoxWidget, CrossButtonComponent crossButtonWidget) {
        this.removeWidget(crossButtonWidget);
        this.removeWidget(editBoxWidget);
        this.editBoxWidgets.remove(editBoxWidget);
        adjustLayoutForRemovedEntry();
    }

    private void adjustLayoutForNewEntry(Button addEntryButton) {
        this.setHeight(this.getHeight() + DEFAULT_HEIGHT + GAP_WIDTH);
        addEntryButton.setY(addEntryButton.getY() + DEFAULT_HEIGHT + GAP_WIDTH);
    }

    private void adjustLayoutForRemovedEntry() {
        this.setHeight(this.getHeight() - DEFAULT_HEIGHT - GAP_WIDTH);
        this.addEntryButton.setY(this.addEntryButton.getY() - DEFAULT_HEIGHT - GAP_WIDTH);

        // Update positions of remaining components
        updateComponentPositions();
    }

    private void updateComponentPositions() {
        int index = 0;
        for (Map.Entry<EditBoxWidget, CrossButtonComponent> entry : editBoxWidgets.entrySet()) {
            int yPosition = index * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH;
            entry.getKey().setY(yPosition);
            entry.getValue().setY(yPosition + 3);
            index++;
        }
    }

    @Override
    public void startRenderable() {
        this.addWidgets(new ArrayList<>(editBoxWidgets.keySet()));
        this.addWidgets(new ArrayList<>(editBoxWidgets.values()));
        this.addWidget(this.addEntryButton);
        super.startRenderable();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        super.render(guiGraphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
        renderHorizontalLines(guiGraphics);
        this.addEntryButton.active = getConfigEntry().getMaxLength() > this.editBoxWidgets.size();
        this.editBoxWidgets.values().forEach(crossButtonComponent -> crossButtonComponent.active = getConfigEntry().getMinLength() < this.editBoxWidgets.size());
    }

    private void renderHorizontalLines(GuiGraphics graphics) {
        int lineYStart = Minecraft.getInstance().font.lineHeight + 6;
        int lineYEnd = getHeight() - DEFAULT_HEIGHT + 6;
        graphics.fill(0, lineYStart, WIDTH, lineYStart + 1, 0xFFFFFFFF);
        graphics.fill(0, lineYEnd, WIDTH, lineYEnd + 1, 0xFFFFFFFF);
    }

    @Override
    public boolean isOriginalValue() {
        List<String> currentValues = this.editBoxWidgets.keySet().stream()
                .map(EditBoxWidget::getValue)
                .toList();
        List<String> originalValues = this.getConfigEntry().get().stream()
                .map(Object::toString)
                .toList();
        return currentValues.equals(originalValues);
    }

    @Override
    public void resetValue() {
        clearEntries();
        this.editBoxWidgets.putAll(createEditBoxWidgets());
        this.addWidgets(new ArrayList<>(editBoxWidgets.keySet()));
        this.addWidgets(new ArrayList<>(editBoxWidgets.values()));
        resetLayout();
    }

    private void clearEntries() {
        this.editBoxWidgets.keySet().forEach(this::removeWidget);
        this.editBoxWidgets.values().forEach(this::removeWidget);
        this.editBoxWidgets.clear();
    }

    private void resetLayout() {
        int newHeight = calculateInitialHeight(this.getConfigEntry());
        this.setHeight(newHeight);
        this.addEntryButton.setY(newHeight - DEFAULT_HEIGHT * 2);
    }

    private Map<EditBoxWidget, CrossButtonComponent> createEditBoxWidgets() {
        Map<EditBoxWidget, CrossButtonComponent> components = new LinkedHashMap<>();
        List<?> configValues = configEntry.get();
        for (int i = 0; i < configValues.size(); i++) {
            EditBoxWidget textBox = createEditBoxWidget(i);
            textBox.setValue(configValues.get(i).toString());
            CrossButtonComponent crossButton = createCrossButtonComponent(textBox);
            components.put(textBox, crossButton);
        }
        return components;
    }

    public boolean hasInputValidationErrors() {
//        return this.editBoxWidgets.keySet().stream()
//                .anyMatch(IInputValidatable::hasInputValidationErrors);
        //TODO
        return false;
    }
}
