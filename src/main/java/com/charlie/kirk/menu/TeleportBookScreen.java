package com.charlie.kirk.menu;

import com.charlie.kirk.Kirk;
import com.charlie.kirk.data.TeleportBookRecord;
import com.charlie.kirk.data.TeleportPosition;
import com.charlie.kirk.item.TeleportBookItem;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TeleportBookScreen extends Screen {
    private static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(Kirk.MODID, "textures/gui/backgrounds/silo_background.png");
    private Button newLocationButton;
    private Button addButton;
    private boolean shouldrenderbutton = true;
    private static final Component WARNING_LABEL = Component.translatable("options.languageAccuracyWarning").withColor(-4539718);
    public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private EditBox name;
    private EditBox x_input;
    private EditBox y_input;
    private EditBox z_input;
    private final InteractionHand hand;
    private final ItemStack teleportBook;
    public ObjectList<Entry> positions;
    public TeleportBookScreen(Component title, ItemStack teleportBook, InteractionHand hand) {
        super(title);
        this.layout.setFooterHeight(53);
        this.hand = hand;
        this.teleportBook = teleportBook;
    }

    @Override
    protected void init() {
        super.init();
        addFooter();
        this.layout.addTitleHeader(this.title, this.font);
        this.layout.visitWidgets((p_345605_) -> {
            AbstractWidget abstractwidget = (AbstractWidget)this.addRenderableWidget(p_345605_);
        });
        this.layout.arrangeElements();
        int i = (this.width - 212) / 2;
        int j = (this.height - 222) / 2;
        this.name = new EditBox(this.font, i + 62, j + 24, 103, 12, Component.translatable("container.repair"));
        this.name.setCanLoseFocus(true);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(true);
        this.name.setMaxLength(50);
        //this.name.setResponder(this::onNameChanged);
        this.name.setValue("");
        this.addWidget(this.name);
        name.visible = false;
        //x_input
        this.x_input = new EditBox(this.font, i , j, 40, 12, Component.translatable("container.repai"));
        this.x_input.setCanLoseFocus(true);
        this.x_input.setTextColor(-1);
        this.x_input.setTextColorUneditable(-1);
        this.x_input.setBordered(true);
        this.x_input.setMaxLength(50);
        //this.name.setResponder(this::onNameChanged);
        this.x_input.setValue("");
        this.addWidget(this.x_input);
        this.x_input.visible = false;
        //y_input
        this.y_input = new EditBox(this.font, i + 30 , j, 40, 12, Component.translatable("container.repai"));
        this.y_input.setCanLoseFocus(true);
        this.y_input.setTextColor(-1);
        this.y_input.setTextColorUneditable(-1);
        this.y_input.setBordered(true);
        this.y_input.setMaxLength(50);
        //this.name.setResponder(this::onNameChanged);
        this.y_input.setValue("");
        this.y_input.visible = false;
        this.addWidget(this.y_input);
        //z_input
        this.z_input = new EditBox(this.font, i - 30 , j - 20, 40, 12, Component.translatable("container.repai"));
        this.z_input.setCanLoseFocus(true);
        this.z_input.setTextColor(-1);
        this.z_input.setTextColorUneditable(-1);
        this.z_input.setBordered(true);
        this.z_input.setMaxLength(50);
        //this.name.setResponder(this::onNameChanged);
        this.z_input.setValue("");
        this.z_input.visible = false;
        this.addWidget(this.z_input);
        //this.name.setEditable(((AnvilMenu)this.menu).getSlot(0).hasItem());
        this.newLocationButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("kirk.button.new_location"), (p_98177_) -> {
            //this.updateButtonVisibility();
            newLocationButton.visible = false;
            addButton.visible = true;
            x_input.visible = true;
            y_input.visible = true;
            z_input.visible = true;
            name.visible = true;
        }).bounds(this.width / 2 - 100, 196, 98, 50).build());
        this.addButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("kirk.button.add"), (p_98177_) -> {
            //this.updateButtonVisibility();

            addButton.visible = false;
            newLocationButton.visible = true;
            x_input.visible = false;
            y_input.visible = false;
            z_input.visible = false;
            name.visible = false;
            TeleportBookRecord record = teleportBook.get(Kirk.TELEPORT_BOOK_DATA_COMPONENT);
            List<TeleportPosition> newPos = new java.util.ArrayList<>(record.positions());
            newPos.add(new TeleportPosition(name.getValue(),Integer.parseInt(x_input.getValue()),Integer.parseInt(y_input.getValue()),Integer.parseInt(z_input.getValue())));
            teleportBook.set(Kirk.TELEPORT_BOOK_DATA_COMPONENT, new TeleportBookRecord(newPos));
            record = teleportBook.get(Kirk.TELEPORT_BOOK_DATA_COMPONENT);
            for(int k = 0; k < record.positions().toArray().length; k++)
            {
                Kirk.LOGGER.info(record.positions().get(k).name());
                Kirk.LOGGER.info(String.valueOf(record.positions().get(k).x()));
                Kirk.LOGGER.info(String.valueOf(record.positions().get(k).y()));
                Kirk.LOGGER.info(String.valueOf(record.positions().get(k).z()));
            }

        }).bounds(this.width / 2 - 100, 250, 98, 20).build());
        addButton.visible = false;
    }
    protected void addFooter() {
        LinearLayout linearlayout = ((LinearLayout)this.layout.addToFooter(LinearLayout.vertical())).spacing(8);
        linearlayout.defaultCellSetting().alignHorizontallyCenter();
        linearlayout.addChild(new StringWidget(WARNING_LABEL, this.font));
        LinearLayout linearlayout1 = (LinearLayout)linearlayout.addChild(LinearLayout.horizontal().spacing(8));
    }

    @Override
    public void tick() {
        super.tick();
        if(name.getValue().isEmpty() || x_input.getValue().isEmpty() || y_input.getValue().isEmpty() || z_input.getValue().isEmpty())
        {

            addButton.active = false;
        }else{

            addButton.active = true;
            for(int l = 0; l < x_input.getValue().length(); l++)
            {
                if(x_input.getValue().toCharArray()[l] == 65 || x_input.getValue().toCharArray()[l] == 66 ||x_input.getValue().toCharArray()[l] == 67 ||x_input.getValue().toCharArray()[l] == 68 ||x_input.getValue().toCharArray()[l] == 69 ||x_input.getValue().toCharArray()[l] == 70 ||x_input.getValue().toCharArray()[l] == 71 ||x_input.getValue().toCharArray()[l] == 72 ||x_input.getValue().toCharArray()[l] == 73)
                {

                }else{
                    addButton.active = false;
                }
            }
        }
    }

    public void renderFg(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.name.render(guiGraphics, mouseX, mouseY, partialTick);
        this.x_input.render(guiGraphics, mouseX, mouseY, partialTick);
        this.y_input.render(guiGraphics, mouseX, mouseY, partialTick);
        this.z_input.render(guiGraphics, mouseX, mouseY, partialTick);
        this.newLocationButton.render(guiGraphics, mouseX, mouseY, partialTick);
        this.addButton.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND_LOCATION, (this.width - 212) / 2, 0, 0, 0, 212, 222);
        renderFg(graphics, mouseX, mouseY, partialTick);
        /*
         * This method is added by the container screen to render
         * the tooltip of the hovered slot.
         */
        //this.renderTooltip(graphics, mouseX, mouseY);
    }
    @OnlyIn(Dist.CLIENT)
    public class Entry extends ObjectSelectionList.Entry {
        final String code;
        private final Component language;
        private long lastClickTime;

        public Entry(String code, LanguageInfo languageInfo) {
            this.code = code;
            this.language = languageInfo.toComponent();
        }

        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            guiGraphics.drawCenteredString(TeleportBookScreen.this.font, this.language, 100 / 2, top + height / 2 - 4, -1);
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (CommonInputs.selected(keyCode)) {
                this.select();
                TeleportBookScreen.this.onDone();
                return true;
            } else {
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        }

        @Override
        public Component getNarration() {
            return null;
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.select();
            if (Util.getMillis() - this.lastClickTime < 250L) {
                TeleportBookScreen.this.onDone();
            }

            this.lastClickTime = Util.getMillis();
            return super.mouseClicked(mouseX, mouseY, button);
        }

        private void select() {
            //LanguageSelectScreen.LanguageSelectionList.this.setSelected(this);
        }
    }

    private void onDone() {
    }
}
