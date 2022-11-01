package wraith.alloyforgery;

import io.wispforest.owo.client.screens.ScreenUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import wraith.alloyforgery.block.ForgeControllerBlockEntity;
import wraith.alloyforgery.forges.ForgeFuelRegistry;

public class AlloyForgeScreenHandler extends ScreenHandler {

    private final Inventory controllerInventory;
    private final PropertyDelegate propertyDelegate;

    public AlloyForgeScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(ForgeControllerBlockEntity.INVENTORY_SIZE), new ArrayPropertyDelegate(3));
    }

    public AlloyForgeScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(AlloyForgery.ALLOY_FORGE_SCREEN_HANDLER_TYPE, syncId);

        this.controllerInventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);

        //Fuel Slot
        this.addSlot(new Slot(controllerInventory, 11, 8, 74) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return ForgeFuelRegistry.hasFuel(stack.getItem());
            }
        });

        //Recipe Output
        this.addSlot(new Slot(controllerInventory, 10, 145, 50) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

        //Recipe Inputs
        for (int m = 0; m < 2; m++) {
            for (int l = 0; l < 5; l++) {
                this.addSlot(new Slot(controllerInventory, l + m * 5, 44 + l * 18, 43 + m * 18));
            }
        }

        ScreenUtils.generatePlayerSlots(8, 107, playerInventory, this::addSlot);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        return ScreenUtils.handleSlotTransfer(this, invSlot, this.controllerInventory.size());
    }

    public int getSmeltProgress() {
        return propertyDelegate.get(0);
    }

    public int getFuelProgress() {
        return propertyDelegate.get(1);
    }

    public int getLavaProgress() {
        return propertyDelegate.get(2);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.controllerInventory.canPlayerUse(player);
    }

    public Inventory getControllerInventory(){
        return this.controllerInventory;
    }
}
