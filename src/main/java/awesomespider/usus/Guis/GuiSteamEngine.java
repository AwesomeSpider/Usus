package awesomespider.usus.Guis;

import awesomespider.usus.Containers.ContainerSteamEngine;
import awesomespider.usus.TileEntities.TileEntitySteamEngine;
import awesomespider.usus.Usus;
import awesomespider.usus.Utils.TextureUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by Awesome_Spider on 7/5/2015.
 */
public class GuiSteamEngine extends GuiContainer {
    TileEntitySteamEngine tileSteamEngine;

    public GuiSteamEngine(InventoryPlayer inventory, TileEntitySteamEngine tileSteamEngine) {
        super(new ContainerSteamEngine(inventory, tileSteamEngine));
        this.tileSteamEngine = tileSteamEngine;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int mouseX, int mouseY) {
        TextureUtil.bindTexture(Usus.MODID, "textures/guis/guiSteamEngine.png");
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        //Progress/quantity bars
        int scaledRf = this.tileSteamEngine.scaleRfForDisplay(30);
        this.drawTexturedModalRect(guiLeft + 111, guiTop + 40, 176, 31, 14, scaledRf);
        int scaledWater = this.tileSteamEngine.scaleWaterForDisplay(30);
        this.drawTexturedModalRect(guiLeft + 39, guiTop + 38, 176, 61, 14, scaledWater);

        if(tileSteamEngine.burningFuel){
            int i1 = tileSteamEngine.getBurnTimeRemainingScaled(13);
            this.drawTexturedModalRect(guiLeft + 56, guiTop + 25 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
            i1 = tileSteamEngine.getCookProgressScaled(24);
            this.drawTexturedModalRect(guiLeft + 80, guiTop + 47, 176, 14, i1 + 1, 16);
        }
    }
}
