package tlod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class DerpBot {
    Minecraft mc = Minecraft.getMinecraft();

    public int a;
    public int direction;

    public void onEnable() {
        if (mc.inGameHasFocus) {
            a = 1;
            direction = MathHelper.floor_double((double) ((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
            MinecraftForge.EVENT_BUS.register(this);
        } else
            Settings.setToggled(false);
    }

    public void onDisable() {
        a = 1;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        BlockPos playerBlock = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ);

        if (direction == 0) { //south
            if (a == 0) {
                mc.thePlayer.rotationYaw = 0F;
                mc.thePlayer.rotationPitch = 0F;
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
            }
            if (onEdge()) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                mc.thePlayer.rotationYaw = -135F;
                mc.thePlayer.rotationPitch = 75.8F;
                placeBlock(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
                a = 0;
            }
        } else if (direction == 1) { //west
            if (a == 0) {
                mc.thePlayer.rotationYaw = 90F;
                mc.thePlayer.rotationPitch = 0F;
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
            }
            if (onEdge()) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                mc.thePlayer.rotationYaw = -45F;
                mc.thePlayer.rotationPitch = 75.8F;
                placeBlock(playerBlock.add(0, -1, 0), EnumFacing.WEST);
                a = 0;
            }
        } else if (direction == 2) { //north
            if (a == 0) {
                mc.thePlayer.rotationYaw = -180F;
                mc.thePlayer.rotationPitch = 0F;
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
            }
            if (onEdge()) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                mc.thePlayer.rotationYaw = 45F;
                mc.thePlayer.rotationPitch = 75.8F;
                placeBlock(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
                a = 0;
            }
        } else if (direction == 3) { //east
            if (a == 0) {
                mc.thePlayer.rotationYaw = -90F;
                mc.thePlayer.rotationPitch = 0F;
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
            }
            if (onEdge()) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                mc.thePlayer.rotationYaw = 135F;
                mc.thePlayer.rotationPitch = 75.8F;
                placeBlock(playerBlock.add(0, -1, 0), EnumFacing.EAST);
                a = 0;
            }
        }
    }

    public void placeBlock(BlockPos pos, EnumFacing face) {
        if (!doesSlotHaveBlocks(mc.thePlayer.inventory.currentItem))
            mc.thePlayer.inventory.currentItem = getFirstHotBarSlotWithBlocks();

        if (!isBlockAir(getBlock(pos)))
            return;

        if (face == EnumFacing.UP) {
            pos = pos.add(0, -1, 0);
        } else if (face == EnumFacing.NORTH) {
            pos = pos.add(0, 0, 1);
        } else if (face == EnumFacing.EAST) {
            pos = pos.add(-1, 0, 0);
        } else if (face == EnumFacing.SOUTH) {
            pos = pos.add(0, 0, -1);
        } else if (face == EnumFacing.WEST) {
            pos = pos.add(1, 0, 0);
        }

        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
            mc.thePlayer.swingItem();
            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, face, new Vec3(0.5D, 0.5D, 0.5D));
            a = 1;
        }
    }

    public boolean onEdge() {
        if (direction == 0) {
            if (Math.round(Math.abs(mc.thePlayer.posZ - Math.floor(mc.thePlayer.posZ)) * 10.0D) <= 3L &&
                    Math.round(Math.abs(mc.thePlayer.posZ - Math.floor(mc.thePlayer.posZ)) * 10.0D) >= 1L)
                return true;
        } else if (direction == 3) {
            if (Math.round(Math.abs(mc.thePlayer.posX - Math.floor(mc.thePlayer.posX)) * 10.0D) <= 3L &&
                    Math.round(Math.abs(mc.thePlayer.posX - Math.floor(mc.thePlayer.posX)) * 10.0D) >= 1L)
                return true;
        } else if (direction == 1) {
            if (Math.round(Math.abs(mc.thePlayer.posX - Math.floor(mc.thePlayer.posX)) * 10.0D) <= 9L &&
                    Math.round(Math.abs(mc.thePlayer.posX - Math.floor(mc.thePlayer.posX)) * 10.0D) >= 7L)
                return true;
        } else if (direction == 2) {
            if (Math.round(Math.abs(mc.thePlayer.posZ - Math.floor(mc.thePlayer.posZ)) * 10.0D) <= 9L &&
                    Math.round(Math.abs(mc.thePlayer.posZ - Math.floor(mc.thePlayer.posZ)) * 10.0D) >= 7L)
                return true;
        }
        return false;
    }

    public boolean isBlockAir(Block block) {
        if (block.getMaterial().isReplaceable()) {
            if (block instanceof BlockSnow && block.getBlockBoundsMaxY() > 0.125D)
                return false;
            return true;
        }
        return false;
    }

    public static Block getBlock(BlockPos pos) {
        Minecraft mc = Minecraft.getMinecraft();
        return (mc.theWorld.getBlockState(pos).getBlock());
    }

    public int getFirstHotBarSlotWithBlocks() {
        for (int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventory.getStackInSlot(i) != null && mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock)
                return i;
        }
        return 0;
    }

    public boolean doesSlotHaveBlocks(int slotToCheck) {
        if (mc.thePlayer.inventory.getStackInSlot(slotToCheck) != null && mc.thePlayer.inventory.getStackInSlot(slotToCheck).getItem() instanceof ItemBlock &&
                (mc.thePlayer.inventory.getStackInSlot(slotToCheck)).stackSize > 0)
            return true;
        return false;
    }
}

