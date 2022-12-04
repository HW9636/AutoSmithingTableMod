package io.github.hw9636.autosmithingtable.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org._9636dev.autolib.lib.container.AutoContainer;
import org._9636dev.autolib.lib.container.AutoContainerData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AutoSmithingContainer extends AutoContainer {
    public static final int BASE_SLOTS_START = 36;
    public static final int BASE_SLOTS_END = 36;

    public static final int ADDITION_SLOTS_START = 37;
    public static final int ADDITION_SLOTS_END = 37;

    public AutoSmithingContainer(int id, Inventory playerInv) {
        this(id, playerInv, BlockPos.ZERO, new SimpleContainerData(4), new ItemStackHandler(1),
                new ItemStackHandler(1), new ItemStackHandler(1));
    }

    public AutoSmithingContainer(int id, Inventory playerInv, BlockPos pos, ContainerData data, IItemHandler baseSlots,
                                 IItemHandler additionSlots, IItemHandler outputSlots) {
        super(Registries.AUTO_SMITHING_CONTAINER.get(), id, playerInv, pos, data);

        this.addInventorySlots(playerInv);


        this.addSlot(new SlotItemHandler(baseSlots, 0, 27, 47));
        this.addSlot(new SlotItemHandler(additionSlots,  0, 76, 47));
        this.addSlot(new SlotItemHandler(outputSlots,  0, 134, 47) {
            public boolean mayPlace(@NotNull ItemStack p_39818_) {
                return false;
            }
        });

    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.containerAccess, player, Registries.AUTO_SMITHING_TABLE.get());
    }

    public static MenuConstructor getServerContainer(AutoSmithingTableBlockEntity entity, BlockPos pos) {
        return (id, playerInv, player) -> new AutoSmithingContainer(id, playerInv, pos,
                new AutoContainerData(entity), entity.baseSlots, entity.additionSlots,
                entity.outputSlots);
    }

    protected void moveItemToContainer(ItemStack item) {
        Optional<Optional<UpgradeRecipe>> optionalOptionalRecipe = this.containerAccess.evaluate((level, pos) -> level.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING).stream()
                .filter((ur) -> ur.isAdditionIngredient(item)).findFirst());
        if (optionalOptionalRecipe.isPresent()) {
            Optional<UpgradeRecipe> recipeOptional = optionalOptionalRecipe.get();
            if (recipeOptional.isPresent()) {
                moveItemStackTo(item, ADDITION_SLOTS_START, ADDITION_SLOTS_END + 1, false);
                return;
            }
        }
        moveItemStackTo(item, BASE_SLOTS_START, BASE_SLOTS_END + 1, false);
    }
}