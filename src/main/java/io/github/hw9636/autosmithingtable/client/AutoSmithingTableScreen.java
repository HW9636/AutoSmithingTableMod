package io.github.hw9636.autosmithingtable.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import io.github.hw9636.autosmithingtable.AutoSmithingTableMod;
import io.github.hw9636.autosmithingtable.common.AutoSmithingContainer;
import io.github.hw9636.autosmithingtable.common.Registries;
import io.github.hw9636.autosmithingtable.common.config.ASTConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org._9636dev.autolib.lib.screen.AutoScreen;
import org._9636dev.autolib.lib.screen.widget.ProgressBar;
import org._9636dev.autolib.lib.screen.widget.TexturedButton;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class AutoSmithingTableScreen extends AutoScreen<AutoSmithingContainer> {
    private static final int PROGRESS_BAR_ONS_LEFT = 100;
    private static final int PROGRESS_BAR_ONS_TOP = 46;
    private static final int PROGRESS_BAR_OFS_LEFT = 177;
    private static final int PROGRESS_BAR_OFS_TOP = 1;
    private static final int ENERGY_BAR_ONS_LEFT = 164;
    private static final int ENERGY_BAR_ONS_TOP = 8;
    private static final int ENERGY_BAR_OFS_LEFT = 177;
    private static final int ENERGY_BAR_OFS_TOP = 22;
    private static final int CONFIG_CLS_ONS_TOP = 16;
    private static final int CONFIG_CLS_ONS_LEFT = -22;
    private static final int CONFIG_CLS_OFS_LEFT = 204;
    private static final int CONFIG_CLS_OFS_TOP = 1;
    private static final int CONFIG_OPN_ONS_LEFT = -48;
    private static final int CONFIG_OPN_ONS_TOP = 16;
    private static final int CONFIG_OPN_OFS_LEFT = 182;
    private static final int CONFIG_OPN_OFS_TOP = 22;

    public static final int PROGRESS_BAR_WIDTH = 27;
    public static final int PROGRESS_BAR_HEIGHT = 20;
    private static final int ENERGY_BAR_WIDTH = 4;
    private static final int ENERGY_BAR_HEIGHT = 71;
    private static final int CONFIG_CLS_WIDTH = 18;
    private static final int CONFIG_CLS_HEIGHT = 18;
    private static final int CONFIG_OPN_HEIGHT = 44;
    private static final int CONFIG_OPN_WIDTH = 44;

    private static final int WHITE_SQ_LEFT = 223;
    private static final int WHITE_SQ_TOP = 1; // Size 16

    // TODO: 12/4/2022 Add Widget locations

    private boolean configIsOpen;
    private final TexturedButton closedConfig;

    private static final ResourceLocation TEXTURE = new ResourceLocation(AutoSmithingTableMod.MOD_ID, "textures/gui/auto_smithing_table.png");

    public AutoSmithingTableScreen(AutoSmithingContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.configIsOpen = false;
        this.closedConfig = new TexturedButton(getGuiLeft() + CONFIG_CLS_ONS_LEFT, getGuiTop() + CONFIG_CLS_ONS_TOP, CONFIG_CLS_WIDTH, CONFIG_CLS_HEIGHT,
                new TexturedButton.Texture(TEXTURE, CONFIG_CLS_OFS_LEFT, CONFIG_CLS_OFS_TOP), (button, mouseButton) -> {
           LogUtils.getLogger().info("Mouse Clicked, Button: {}", mouseButton);
        });
    }

    @Override
    protected void init() {
        super.init();

        int i = this.getGuiLeft();
        int j = this.getGuiTop();

        this.addRenderableWidget(new ProgressBar(
                i + PROGRESS_BAR_ONS_LEFT,
                j + PROGRESS_BAR_ONS_TOP,
                0,
                PROGRESS_BAR_HEIGHT,
                PROGRESS_BAR_WIDTH,
                PROGRESS_BAR_HEIGHT,
                0,
                ASTConfig.COMMON.ticksPerCraft.get(),
                0,
                0,
                ProgressBar.ProgressDirection.LEFT_TO_RIGHT,
                new TexturedButton.Texture(TEXTURE, PROGRESS_BAR_OFS_LEFT, PROGRESS_BAR_OFS_TOP),
                () -> menu.data.get(2)
        ));

        this.addRenderableWidget(new ProgressBar(
                i + ENERGY_BAR_ONS_LEFT,
                j + ENERGY_BAR_ONS_TOP,
                ENERGY_BAR_WIDTH,
                0,
                ENERGY_BAR_WIDTH,
                ENERGY_BAR_HEIGHT,
                0,
                0,
                0,
                ASTConfig.COMMON.maxEnergyStored.get(),
                ProgressBar.ProgressDirection.BOTTOM_TO_TOP,
                new TexturedButton.Texture(TEXTURE, ENERGY_BAR_OFS_LEFT, ENERGY_BAR_OFS_TOP),
                () -> (menu.data.get(0) << 16) | menu.data.get(1)
        ));

        this.addRenderableWidget(this.closedConfig);
    }

    @Override
    public void render(@NotNull PoseStack stack, int mx, int my, float pPartialTick) {
        super.render(stack, mx, my, pPartialTick);

        int i = this.getGuiLeft();
        int j = this.getGuiTop();

        this.renderTooltip(stack, mx, my);

        if (isIn(mx, my, i + ENERGY_BAR_ONS_LEFT,j + ENERGY_BAR_ONS_TOP,
                i + ENERGY_BAR_ONS_LEFT + ENERGY_BAR_WIDTH, j + ENERGY_BAR_ONS_TOP + ENERGY_BAR_HEIGHT)) {
            renderComponentTooltip(stack, List.of(new TextComponent((this.menu.data.get(0) << 16 | this.menu.data.get(1)) + "/" + ASTConfig.COMMON.maxEnergyStored.get())), mx, my);
        }

        if (!configIsOpen) {
            assert this.minecraft != null;
            this.minecraft.getItemRenderer().renderGuiItem(Registries.AUTO_SMITHING_TABLE_ITEM.get().getDefaultInstance(), CONFIG_CLS_ONS_LEFT + 1, CONFIG_CLS_ONS_TOP + 1);
        }
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(stack, pPartialTick, pMouseX, pMouseY);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(stack, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.imageWidth, this.imageHeight);
    }
}
