package com.daqem.yamlconfig.client.gui.component.entry.list;

import com.daqem.uilib.client.gui.component.io.ButtonComponent;
import com.daqem.uilib.client.gui.component.io.TextBoxComponent;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.list.IListConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IComponentValidator;
import com.daqem.yamlconfig.client.gui.component.CrossButtonComponent;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.*;

public abstract class BaseListConfigEntryComponent<T extends BaseListConfigEntryComponent<T, C>, C extends IListConfigEntry<?>> extends BaseConfigEntryComponent<T, C> {

    private static final int WIDTH = KEY_WIDTH + GAP_WIDTH + VALUE_WIDTH;

    protected final Map<TextBoxComponent, CrossButtonComponent> textBoxComponents;
    protected final ButtonComponent addEntryButton;

    private final IComponentValidator validator;

    public BaseListConfigEntryComponent(String key, C configEntry, IComponentValidator validator) {
        super(key, configEntry, 0, 0, calculateInitialHeight(configEntry), WIDTH);
        this.validator = validator;
        this.textBoxComponents = createTextBoxComponents();
        this.addEntryButton = createAddEntryButton();
    }

    private static int calculateInitialHeight(IListConfigEntry<?> configEntry) {
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
        TextBoxComponent newTextBox = createTextBoxComponent(textBoxComponents.size());
        CrossButtonComponent newCrossButton = createCrossButtonComponent(newTextBox);

        textBoxComponents.put(newTextBox, newCrossButton);
        this.addChild(newTextBox);
        this.addChild(newCrossButton);

        adjustLayoutForNewEntry(clickedObject);
        return true;
    }

    private TextBoxComponent createTextBoxComponent(int index) {
        return new TextBoxComponent(
                0,
                index * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH,
                WIDTH,
                DEFAULT_HEIGHT,
                ""
        ) {
            @Override
            public List<Component> validateInput(String input) {
                return validator.validate(input);
            }
        };
    }

    private CrossButtonComponent createCrossButtonComponent(TextBoxComponent textBoxComponent) {
        return new CrossButtonComponent(
                WIDTH + GAP_WIDTH + 3,
                textBoxComponent.getY() + 3,
                (clickedObject, screen, mouseX, mouseY, button) -> handleRemoveEntryButtonClick(textBoxComponent, (CrossButtonComponent) clickedObject)
        );
    }

    private boolean handleRemoveEntryButtonClick(TextBoxComponent textBoxComponent, CrossButtonComponent crossButtonComponent) {
        removeEntry(textBoxComponent, crossButtonComponent);
        return true;
    }

    private void removeEntry(TextBoxComponent textBoxComponent, CrossButtonComponent crossButtonComponent) {
        this.removeChild(crossButtonComponent);
        this.removeChild(textBoxComponent);
        this.textBoxComponents.remove(textBoxComponent);
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
        for (Map.Entry<TextBoxComponent, CrossButtonComponent> entry : textBoxComponents.entrySet()) {
            int yPosition = index * (DEFAULT_HEIGHT + GAP_WIDTH) + DEFAULT_HEIGHT + GAP_WIDTH;
            entry.getKey().setY(yPosition);
            entry.getValue().setY(yPosition + 3);
            index++;
        }
    }

    @Override
    public void startRenderable() {
        this.addChildren(new ArrayList<>(textBoxComponents.keySet()));
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
        List<String> currentValues = this.textBoxComponents.keySet().stream()
                .map(TextBoxComponent::getValue)
                .toList();
        List<String> originalValues = this.getConfigEntry().get().stream()
                .map(Object::toString)
                .toList();
        return currentValues.equals(originalValues);
    }

    @Override
    public void resetValue() {
        clearEntries();
        this.textBoxComponents.putAll(createTextBoxComponents());
        this.addChildren(new ArrayList<>(textBoxComponents.keySet()));
        this.addChildren(new ArrayList<>(textBoxComponents.values()));
        resetLayout();
    }

    private void clearEntries() {
        this.textBoxComponents.keySet().forEach(this::removeChild);
        this.textBoxComponents.values().forEach(this::removeChild);
        this.textBoxComponents.clear();
    }

    private void resetLayout() {
        int newHeight = calculateInitialHeight(this.getConfigEntry());
        this.setHeight(newHeight);
        this.addEntryButton.setY(newHeight - DEFAULT_HEIGHT * 2);
    }

    private Map<TextBoxComponent, CrossButtonComponent> createTextBoxComponents() {
        Map<TextBoxComponent, CrossButtonComponent> components = new LinkedHashMap<>();
        List<?> configValues = configEntry.get();
        for (int i = 0; i < configValues.size(); i++) {
            TextBoxComponent textBox = createTextBoxComponent(i);
            textBox.setValue(configValues.get(i).toString());
            CrossButtonComponent crossButton = createCrossButtonComponent(textBox);
            components.put(textBox, crossButton);
        }
        return components;
    }
}
