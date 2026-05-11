package com.daqem.yamlconfig.client.gui.component.entry.map;

import com.daqem.uilib.api.widget.IInputValidatable;
import com.daqem.uilib.gui.widget.ButtonWidget;
import com.daqem.uilib.gui.widget.EditBoxWidget;
import com.daqem.uilib.util.ValidationErrors;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.map.IMapConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IComponentValidator;
import com.daqem.yamlconfig.client.gui.component.CrossButtonComponent;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;

import java.util.*;
import java.util.stream.Stream;

public abstract class BaseMapConfigEntryComponent<C extends IMapConfigEntry<?>> extends BaseConfigEntryComponent<C> {

    private static final int WIDTH = KEY_WIDTH + GAP_WIDTH + VALUE_WIDTH;

    protected final Map<Tuple<EditBoxWidget, EditBoxWidget>, CrossButtonComponent> editBoxWidgets;
    protected final ButtonWidget addEntryButton;

    protected final IComponentValidator validator;

    public BaseMapConfigEntryComponent(String key, C configEntry, IComponentValidator validator) {
        super(key, configEntry, 0, 0, calculateInitialHeight(configEntry), WIDTH);
        this.validator = validator;
        this.editBoxWidgets = createEditBoxWidgets(new ArrayList<>(new LinkedHashMap<>(configEntry.get()).entrySet()));
        this.addEntryButton = createAddEntryButton();

        this.addWidgets(new ArrayList<>(editBoxWidgets.keySet().stream().flatMap(tuple -> Stream.of(tuple.getA(), tuple.getB())).toList()));
        this.addWidgets(new ArrayList<>(editBoxWidgets.values()));
        this.addWidget(this.addEntryButton);
    }

    private static int calculateInitialHeight(IMapConfigEntry<?> configEntry) {
        int entryCount = configEntry.get().size();
        return DEFAULT_HEIGHT // Title
                + GAP_WIDTH // Gap between title and first entry or add button
                + DEFAULT_HEIGHT // Add an entry button
                + (entryCount * (DEFAULT_HEIGHT + GAP_WIDTH)); // Entries with gaps
    }

    private ButtonWidget createAddEntryButton() {
        return new ButtonWidget(
                0,
                this.getHeight() - DEFAULT_HEIGHT,
                WIDTH,
                DEFAULT_HEIGHT,
                YamlConfig.translatable("gui.add_entry"),
                this::handleAddEntryButtonClick
        );
    }

    private void handleAddEntryButtonClick(Button clickedButton) {
        Tuple<EditBoxWidget, EditBoxWidget> newTextBoxes = createEditBoxWidgets(editBoxWidgets.size());
        CrossButtonComponent newCrossButton = createCrossButtonComponent(newTextBoxes);

        editBoxWidgets.put(newTextBoxes, newCrossButton);
        this.addWidget(newTextBoxes.getA());
        this.addWidget(newTextBoxes.getB());
        this.addWidget(newCrossButton);

        adjustLayoutForNewEntry(clickedButton);
    }

    private Tuple<EditBoxWidget, EditBoxWidget> createEditBoxWidgets(int index) {
        return new Tuple<>(createKeyEditBoxWidget(index), createValueEditBoxWidget(index));
    }

    private EditBoxWidget createKeyEditBoxWidget(int index) {
        return new EditBoxWidget(
                Minecraft.getInstance().font,
                0,
                index * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH,
                KEY_WIDTH,
                DEFAULT_HEIGHT,
                Component.empty()
        ) {
            @Override
            public List<Component> validateInput(String input) {
                List<Component> errors = super.validateInput(input);

                if (editBoxWidgets != null) {
                    List<String> currentKeys = editBoxWidgets.keySet().stream()
                            .map(Tuple::getA)
                            .filter(EditBoxWidget -> EditBoxWidget != this)
                            .map(EditBoxWidget::getValue)
                            .toList();

                    if (currentKeys.contains(input)) {
                        errors.add(ValidationErrors.duplicateKey());
                        editBoxWidgets.keySet().stream()
                                .map(Tuple::getA)
                                .filter(EditBoxWidget -> EditBoxWidget != this)
                                .filter(EditBoxWidget -> EditBoxWidget.getValue().equals(input))
                                .forEach(EditBoxWidget -> {
                                    EditBoxWidget.setInputValidationErrors(new ArrayList<>(EditBoxWidget.getInputValidationErrors()));
                                    if (!EditBoxWidget.getInputValidationErrors().contains(ValidationErrors.duplicateKey())) {
                                        EditBoxWidget.getInputValidationErrors().add(ValidationErrors.duplicateKey());
                                    }
                                });
                    } else {
                        for (Map.Entry<Tuple<EditBoxWidget, EditBoxWidget>, CrossButtonComponent> entry : editBoxWidgets.entrySet()) {
                            String key = entry.getKey().getA().getValue();
                            List<String> duplicateKeys = editBoxWidgets.keySet().stream()
                                    .map(Tuple::getA)
                                    .filter(EditBoxWidget -> EditBoxWidget != this)
                                    .filter(EditBoxWidget -> EditBoxWidget != entry.getKey().getA())
                                    .map(EditBoxWidget::getValue)
                                    .filter(value -> value.equals(key))
                                    .toList();

                            if (duplicateKeys.isEmpty()) {
                                entry.getKey().getA().setInputValidationErrors(
                                        new ArrayList<>(entry.getKey().getA().getInputValidationErrors()
                                                .stream()
                                                .filter(component -> !component.equals(ValidationErrors.duplicateKey()))
                                                .toList()
                                        )
                                );
                            }
                        }
                    }
                }
                if (input.isEmpty()) {
                    errors.add(ValidationErrors.emptyKey());
                }
                if (!input.matches("^[a-zA-Z0-9_-]+$")) {
                    errors.add(ValidationErrors.pattern("^[a-zA-Z0-9_-]+$"));
                }

                return errors;
            }
        };
    }

    private EditBoxWidget createValueEditBoxWidget(int index) {
        return new EditBoxWidget(
                Minecraft.getInstance().font,
                KEY_WIDTH + GAP_WIDTH,
                index * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH,
                VALUE_WIDTH,
                DEFAULT_HEIGHT,
                Component.empty()
        ) {
            @Override
            public List<Component> validateInput(String input) {
                return validator.validate(input);
            }
        };
    }

    private CrossButtonComponent createCrossButtonComponent(Tuple<EditBoxWidget, EditBoxWidget> editBoxWidgets) {
        return new CrossButtonComponent(
                WIDTH + GAP_WIDTH + 3,
                editBoxWidgets.getA().getY() + 3,
                button -> handleRemoveEntryButtonClick(editBoxWidgets, (CrossButtonComponent) button)
        );
    }

    private void handleRemoveEntryButtonClick(Tuple<EditBoxWidget, EditBoxWidget> editBoxWidgets, CrossButtonComponent crossButtonComponent) {
        removeEntry(editBoxWidgets, crossButtonComponent);
    }

    private void removeEntry(Tuple<EditBoxWidget, EditBoxWidget> editBoxWidgets, CrossButtonComponent crossButtonWidget) {
        this.removeWidget(crossButtonWidget);
        this.removeWidget(editBoxWidgets.getA());
        this.removeWidget(editBoxWidgets.getB());
        this.editBoxWidgets.remove(editBoxWidgets);
        adjustLayoutForRemovedEntry();
    }

    private void adjustLayoutForNewEntry(Button addEntryButton) {
        this.setHeight(this.getHeight() + DEFAULT_HEIGHT + GAP_WIDTH);
        addEntryButton.setY(this.getHeight() - DEFAULT_HEIGHT);
    }

    private void adjustLayoutForRemovedEntry() {
        this.setHeight(this.getHeight() - DEFAULT_HEIGHT - GAP_WIDTH);
        this.addEntryButton.setY(this.getHeight() - DEFAULT_HEIGHT);

        // Update positions of remaining components
        updateComponentPositions();
    }

    private void updateComponentPositions() {
        int index = 0;
        for (Map.Entry<Tuple<EditBoxWidget, EditBoxWidget>, CrossButtonComponent> entry : editBoxWidgets.entrySet()) {
            int yPosition = index * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH;
            entry.getKey().getA().setY(yPosition);
            entry.getKey().getB().setY(yPosition);
            entry.getValue().setY(yPosition + 3);
            index++;
        }
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
        graphics.fill(getTotalX(), getTotalY() + lineYStart, getTotalX() + WIDTH, getTotalY() + lineYStart + 1, 0xFFFFFFFF);
    }

    @Override
    public boolean isOriginalValue() {
        List<String> currentKeyValues = this.editBoxWidgets.keySet().stream()
                .map(Tuple::getA)
                .map(EditBoxWidget::getValue)
                .toList();
        List<String> originalKeyValues = this.getConfigEntry().getDefaultValue().keySet().stream()
                .map(Object::toString)
                .toList();
        List<String> currentValueValues = this.editBoxWidgets.keySet().stream()
                .map(Tuple::getB)
                .map(EditBoxWidget::getValue)
                .toList();
        List<String> originalValueValues = this.getConfigEntry().getDefaultValue().values().stream()
                .map(Object::toString)
                .toList();
        return currentKeyValues.equals(originalKeyValues) && currentValueValues.equals(originalValueValues);
    }

    @Override
    public void resetValue() {
        clearEntries();
        this.editBoxWidgets.putAll(createEditBoxWidgets(new ArrayList<>(new LinkedHashMap<>(configEntry.getDefaultValue()).entrySet())));
        this.addWidgets(new ArrayList<>(editBoxWidgets.keySet().stream().flatMap(tuple -> Stream.of(tuple.getA(), tuple.getB())).toList()));
        this.addWidgets(new ArrayList<>(editBoxWidgets.values()));
        resetLayout();
    }

    private void clearEntries() {
        this.editBoxWidgets.keySet().stream().flatMap(tuple -> Stream.of(tuple.getA(), tuple.getB())).forEach(this::removeWidget);
        this.editBoxWidgets.values().forEach(this::removeWidget);
        this.editBoxWidgets.clear();
    }

    private void resetLayout() {
        int newHeight = calculateInitialHeight(this.getConfigEntry());
        this.setHeight(newHeight);
        this.addEntryButton.setY(newHeight - DEFAULT_HEIGHT);
    }

    private Map<Tuple<EditBoxWidget, EditBoxWidget>, CrossButtonComponent> createEditBoxWidgets(List<Map.Entry<String, ?>> configValues) {
        Map<Tuple<EditBoxWidget, EditBoxWidget>, CrossButtonComponent> components = new LinkedHashMap<>();
        for (int i = 0; i < configValues.size(); i++) {
            EditBoxWidget keyTextBox = createKeyEditBoxWidget(i);
            EditBoxWidget valueTextBox = createValueEditBoxWidget(i);
            keyTextBox.setValue(configValues.get(i).getKey());
            valueTextBox.setValue(configValues.get(i).getValue().toString());
            Tuple<EditBoxWidget, EditBoxWidget> tuple = new Tuple<>(keyTextBox, valueTextBox);
            CrossButtonComponent crossButton = createCrossButtonComponent(tuple);
            components.put(tuple, crossButton);
        }
        return components;
    }

    public boolean hasInputValidationErrors() {
        return this.editBoxWidgets.keySet().stream()
                .flatMap(key -> Stream.of(key.getA(), key.getB()))
                .anyMatch(IInputValidatable::hasInputValidationErrors);
    }

    @Override
    public boolean hasValidationErrors() {
        return hasInputValidationErrors();
    }
}