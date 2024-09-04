package com.daqem.yamlconfig.client.gui.component.entry.map;

import com.daqem.uilib.client.gui.component.io.ButtonComponent;
import com.daqem.uilib.client.gui.component.io.TextBoxComponent;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.map.IMapConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IComponentValidator;
import com.daqem.yamlconfig.client.gui.component.CrossButtonComponent;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;

import java.util.*;
import java.util.stream.Stream;

public abstract class BaseMapConfigEntryComponent<T extends BaseMapConfigEntryComponent<T, C>, C extends IMapConfigEntry<?>> extends BaseConfigEntryComponent<T, C> {

    private static final int WIDTH = KEY_WIDTH + GAP_WIDTH + VALUE_WIDTH;
    private static final Component DUPLICATE_KEY = YamlConfig.translatable("gui.validation_error.duplicate_key");

    protected final Map<Tuple<TextBoxComponent, TextBoxComponent>, CrossButtonComponent> textBoxComponents;
    protected final ButtonComponent addEntryButton;

    private final IComponentValidator validator;

    public BaseMapConfigEntryComponent(String key, C configEntry, IComponentValidator validator) {
        super(key, configEntry, 0, 0, calculateInitialHeight(configEntry), WIDTH);
        this.validator = validator;
        this.textBoxComponents = createTextBoxComponents();
        this.addEntryButton = createAddEntryButton();
    }

    private static int calculateInitialHeight(IMapConfigEntry<?> configEntry) {
        int entryCount = configEntry.get().size();
        return entryCount * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT * 3 + GAP_WIDTH;
    }

    private ButtonComponent createAddEntryButton() {
        return new ButtonComponent(
                0,
                textBoxComponents.size() * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH,
                WIDTH,
                DEFAULT_HEIGHT,
                YamlConfig.translatable("gui.add_entry"),
                this::handleAddEntryButtonClick
        );
    }

    private boolean handleAddEntryButtonClick(ButtonComponent clickedObject, Object screen, double mouseX, double mouseY, int button) {
        Tuple<TextBoxComponent, TextBoxComponent> newTextBoxes = createTextBoxComponents(textBoxComponents.size());
        CrossButtonComponent newCrossButton = createCrossButtonComponent(newTextBoxes);

        textBoxComponents.put(newTextBoxes, newCrossButton);
        this.addChild(newTextBoxes.getA());
        this.addChild(newTextBoxes.getB());
        this.addChild(newCrossButton);

        adjustLayoutForNewEntry(clickedObject);
        return true;
    }

    private Tuple<TextBoxComponent, TextBoxComponent> createTextBoxComponents(int index) {
        return new Tuple<>(createKeyTextBoxComponent(index), createValueTextBoxComponent(index));
    }

    private TextBoxComponent createKeyTextBoxComponent(int index) {
        return new TextBoxComponent(
                0,
                index * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH,
                KEY_WIDTH,
                DEFAULT_HEIGHT,
                ""
        ) {
            @Override
            public List<Component> validateInput(String input) {
                List<Component> errors = super.validateInput(input);

                if (textBoxComponents != null) {
                    List<String> currentKeys = textBoxComponents.keySet().stream()
                            .map(Tuple::getA)
                            .filter(textBoxComponent -> textBoxComponent != this)
                            .map(TextBoxComponent::getValue)
                            .toList();

                    if (currentKeys.contains(input)) {
                        errors.add(DUPLICATE_KEY);
                        textBoxComponents.keySet().stream()
                                .map(Tuple::getA)
                                .filter(textBoxComponent -> textBoxComponent != this)
                                .filter(textBoxComponent -> textBoxComponent.getValue().equals(input))
                                .forEach(textBoxComponent -> {
                                    textBoxComponent.setInputValidationErrors(new ArrayList<>(textBoxComponent.getInputValidationErrors()));
                                    if (!textBoxComponent.getInputValidationErrors().contains(DUPLICATE_KEY)) {
                                        textBoxComponent.getInputValidationErrors().add(DUPLICATE_KEY);
                                    }
                                });
                    } else {
                        for (Map.Entry<Tuple<TextBoxComponent, TextBoxComponent>, CrossButtonComponent> entry : textBoxComponents.entrySet()) {
                            String key = entry.getKey().getA().getValue();
                            List<String> duplicateKeys = textBoxComponents.keySet().stream()
                                    .map(Tuple::getA)
                                    .filter(textBoxComponent -> textBoxComponent != this)
                                    .filter(textBoxComponent -> textBoxComponent != entry.getKey().getA())
                                    .map(TextBoxComponent::getValue)
                                    .filter(value -> value.equals(key))
                                    .toList();

                            if (duplicateKeys.isEmpty()) {
                                entry.getKey().getA().setInputValidationErrors(
                                        new ArrayList<>(entry.getKey().getA().getInputValidationErrors()
                                                .stream()
                                                .filter(component -> !component.equals(DUPLICATE_KEY))
                                                .toList()
                                        )
                                );
                            }
                        }
                    }
                }
                if (input.isEmpty()) {
                    errors.add(YamlConfig.translatable("gui.validation_error.empty_key"));
                }
                if (!input.matches("^[a-zA-Z0-9_-]+$")) {
                    errors.add(YamlConfig.translatable("gui.validation_error.pattern", "^[a-zA-Z0-9_-]+$"));
                }

                return errors;
            }
        };
    }

    private TextBoxComponent createValueTextBoxComponent(int index) {
        return new TextBoxComponent(
                KEY_WIDTH + GAP_WIDTH,
                index * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH,
                VALUE_WIDTH,
                DEFAULT_HEIGHT,
                ""
        ) {
            @Override
            public List<Component> validateInput(String input) {
                return validator.validate(input);
            }
        };
    }

    private CrossButtonComponent createCrossButtonComponent(Tuple<TextBoxComponent, TextBoxComponent> textBoxComponents) {
        return new CrossButtonComponent(
                WIDTH + GAP_WIDTH + 3,
                textBoxComponents.getA().getY() + 3,
                (clickedObject, screen, mouseX, mouseY, button) -> handleRemoveEntryButtonClick(textBoxComponents, (CrossButtonComponent) clickedObject)
        );
    }

    private boolean handleRemoveEntryButtonClick(Tuple<TextBoxComponent, TextBoxComponent> textBoxComponents, CrossButtonComponent crossButtonComponent) {
        removeEntry(textBoxComponents, crossButtonComponent);
        return true;
    }

    private void removeEntry(Tuple<TextBoxComponent, TextBoxComponent> textBoxComponents, CrossButtonComponent crossButtonComponent) {
        this.removeChild(crossButtonComponent);
        this.removeChild(textBoxComponents.getA());
        this.removeChild(textBoxComponents.getB());
        this.textBoxComponents.remove(textBoxComponents);
        adjustLayoutForRemovedEntry();
    }

    private void adjustLayoutForNewEntry(ButtonComponent addEntryButton) {
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
        for (Map.Entry<Tuple<TextBoxComponent, TextBoxComponent>, CrossButtonComponent> entry : textBoxComponents.entrySet()) {
            int yPosition = index * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH;
            entry.getKey().getA().setY(yPosition);
            entry.getKey().getB().setY(yPosition);
            entry.getValue().setY(yPosition + 3);
            index++;
        }
    }

    @Override
    public void startRenderable() {
        this.addChildren(new ArrayList<>(textBoxComponents.keySet().stream().flatMap(tuple -> Stream.of(tuple.getA(), tuple.getB())).toList()));
        this.addChildren(new ArrayList<>(textBoxComponents.values()));
        this.addChild(this.addEntryButton);
        super.startRenderable();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        renderHorizontalLines(graphics);
        this.addEntryButton.setEnabled(getConfigEntry().getMaxLength() > this.textBoxComponents.size());
        this.textBoxComponents.values().forEach(crossButtonComponent -> crossButtonComponent.setEnabled(getConfigEntry().getMinLength() < this.textBoxComponents.size()));
    }

    private void renderHorizontalLines(GuiGraphics graphics) {
        int lineYStart = Objects.requireNonNull(this.keyText.getText()).getFont().lineHeight + 6;
        int lineYEnd = getHeight() - DEFAULT_HEIGHT + 6;
        graphics.fill(0, lineYStart, WIDTH, lineYStart + 1, 0xFFFFFFFF);
        graphics.fill(0, lineYEnd, WIDTH, lineYEnd + 1, 0xFFFFFFFF);
    }

    @Override
    public boolean isOriginalValue() {
        List<String> currentKeyValues = this.textBoxComponents.keySet().stream()
                .map(Tuple::getA)
                .map(TextBoxComponent::getValue)
                .toList();
        List<String> originalKeyValues = this.getConfigEntry().get().keySet().stream()
                .map(Object::toString)
                .toList();
        List<String> currentValueValues = this.textBoxComponents.keySet().stream()
                .map(Tuple::getB)
                .map(TextBoxComponent::getValue)
                .toList();
        List<String> originalValueValues = this.getConfigEntry().get().values().stream()
                .map(Object::toString)
                .toList();
        return currentKeyValues.equals(originalKeyValues) && currentValueValues.equals(originalValueValues);
    }

    @Override
    public void resetValue() {
        clearEntries();
        this.textBoxComponents.putAll(createTextBoxComponents());
        this.addChildren(new ArrayList<>(textBoxComponents.keySet().stream().flatMap(tuple -> Stream.of(tuple.getA(), tuple.getB())).toList()));
        this.addChildren(new ArrayList<>(textBoxComponents.values()));
        resetLayout();
    }

    private void clearEntries() {
        this.textBoxComponents.keySet().stream().flatMap(tuple -> Stream.of(tuple.getA(), tuple.getB())).forEach(this::removeChild);
        this.textBoxComponents.values().forEach(this::removeChild);
        this.textBoxComponents.clear();
    }

    private void resetLayout() {
        int newHeight = calculateInitialHeight(this.getConfigEntry());
        this.setHeight(newHeight);
        this.addEntryButton.setY(newHeight - DEFAULT_HEIGHT * 2);
    }

    private Map<Tuple<TextBoxComponent, TextBoxComponent>, CrossButtonComponent> createTextBoxComponents() {
        Map<Tuple<TextBoxComponent, TextBoxComponent>, CrossButtonComponent> components = new LinkedHashMap<>();
        List<Map.Entry<String, ?>> configValues = new ArrayList<>(new LinkedHashMap<>(configEntry.get()).entrySet());
        for (int i = 0; i < configValues.size(); i++) {
            TextBoxComponent keyTextBox = createKeyTextBoxComponent(i);
            TextBoxComponent valueTextBox = createValueTextBoxComponent(i);
            keyTextBox.setValue(configValues.get(i).getKey());
            valueTextBox.setValue(configValues.get(i).getValue().toString());
            Tuple<TextBoxComponent, TextBoxComponent> tuple = new Tuple<>(keyTextBox, valueTextBox);
            CrossButtonComponent crossButton = createCrossButtonComponent(tuple);
            components.put(tuple, crossButton);
        }
        return components;
    }
}