package moze_intel.projecte.gameObjs.container;

import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.gameObjs.container.inventory.EternalDensityInventory;
import moze_intel.projecte.gameObjs.container.slots.SlotGhost;
import moze_intel.projecte.gameObjs.container.slots.SlotPredicates;
import moze_intel.projecte.utils.GuiHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class EternalDensityContainer extends Container
{
	public final EternalDensityInventory inventory;

	public static EternalDensityContainer fromNetwork(int windowId, PlayerInventory invPlayer, PacketBuffer data)
	{
		return new EternalDensityContainer(windowId, invPlayer,
				new EternalDensityInventory(GuiHandler.getHeldFromBuf(data), invPlayer.player));
	}
	
	public EternalDensityContainer(int windowId, PlayerInventory invPlayer, EternalDensityInventory gemInv)
	{
		super(ObjHandler.ETERNAL_DENSITY_CONTAINER, windowId);
		inventory = gemInv;
		
		 for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 3; ++j)
			{
				this.addSlot(new SlotGhost(gemInv, j + i * 3, 62 + j * 18, 26 + i * 18, SlotPredicates.HAS_EMC));
			}

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
			{
				this.addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 93 + i * 18));
			}

		for (int i = 0; i < 9; ++i)
		{
			this.addSlot(new Slot(invPlayer, i, 8 + i * 18, 151));
		}
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex)
	{
		Slot slot = getSlot(slotIndex);
		if (slotIndex > 8)
		{
			ItemStack toSet = slot.getStack().copy();
			toSet.setCount(1);
			ItemHandlerHelper.insertItem(inventory, toSet, false);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity player)
	{
		return true;
	}

	@Nonnull
	@Override
	public ItemStack slotClick(int slot, int button, ClickType flag, PlayerEntity player)
	{
		if (slot >= 0 && getSlot(slot).getStack() == inventory.invItem)
		{
			return ItemStack.EMPTY;
		}
		
		if (slot >= 0 && slot < 9)
		{
			inventory.setStackInSlot(slot, ItemStack.EMPTY);
		}
		
		return super.slotClick(slot, button, flag, player);
	}
	
	@Override
	public boolean canDragIntoSlot(Slot slot) 
	{
		return false;
	}
}
