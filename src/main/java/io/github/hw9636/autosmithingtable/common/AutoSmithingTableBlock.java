package io.github.hw9636.autosmithingtable.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org._9636dev.autolib.lib.block.AutoBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutoSmithingTableBlock extends AutoBlock {

    private static final Component CONTAINER_TITLE = new TranslatableComponent("container.autosmithingtable.title");

    public AutoSmithingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult serverUse(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof AutoSmithingTableBlockEntity be) {
            ItemStack itemStack = pPlayer.getItemInHand(pHand);

            if (itemStack.isEmpty() && pPlayer.isCrouching()) {
                int sidesConfig = be.data.get(3);
                Direction direction = pHit.getDirection();
                int value = AutoSmithingTableBlockEntity.getSide(sidesConfig, direction);
                int newValue = value < AutoSmithingTableBlockEntity.SIDE_OUTPUT ? value + 1 : 0;

                be.data.set(3, AutoSmithingTableBlockEntity.setSide(direction, newValue, sidesConfig));
                pPlayer.sendMessage(new TranslatableComponent("message.autosmithingtable.change_side_to_" + newValue),
                        pPlayer.getUUID());

                return InteractionResult.SUCCESS;
            }

            final MenuProvider menu = new SimpleMenuProvider(AutoSmithingContainer.getServerContainer(be, pPos), CONTAINER_TITLE);
            NetworkHooks.openGui((ServerPlayer) pPlayer, menu, pPos);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult clientUse(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null :
                (level, pos, state0, blockentity) -> ((AutoSmithingTableBlockEntity) blockentity).serverTick();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new AutoSmithingTableBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }
}