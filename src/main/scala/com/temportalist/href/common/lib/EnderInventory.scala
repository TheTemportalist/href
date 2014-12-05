package com.temportalist.href.common.lib

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._

/**
 *
 *
 * @author TheTemportalist
 */
class EnderInventory(freq: Frequency) extends IInventory with IFluidHandler {

	private val items: Array[ItemStack] = new Array[ItemStack](freq.getSize())

	override def getSizeInventory: Int = {
		this.items.length
	}

	override def getStackInSlot(i: Int): ItemStack = this.items(i)

	override def decrStackSize(i: Int, amount: Int): ItemStack = {
		if (this.items(i) != null) {
			var stack: ItemStack = null
			if (this.items(i).stackSize <= amount) {
				stack = this.items(i)
				this.items(i) = null
				this.markDirty()
				return stack
			}
			else {
				stack = this.items(i).splitStack(amount)
				if (this.items(i).stackSize == 0) {
					this.items(i) = null
				}
				this.markDirty()
				return stack
			}
		}
		null
	}

	override def getStackInSlotOnClosing(i: Int): ItemStack = {
		if (this.items(i) != null) {
			val stack: ItemStack = this.items(i)
			this.items(i) = null
			return stack
		}
		null
	}

	override def setInventorySlotContents(i: Int, itemStack: ItemStack): Unit = {
		this.items(i) = itemStack
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit) {
			itemStack.stackSize = this.getInventoryStackLimit
		}
		this.markDirty()
	}

	override def getInventoryName: String = "container.ender"

	override def hasCustomInventoryName: Boolean = false

	override def getInventoryStackLimit: Int = 64

	override def isUseableByPlayer(p_70300_1_ : EntityPlayer): Boolean = true

	override def openInventory(): Unit = {}

	override def closeInventory(): Unit = {}

	override def isItemValidForSlot(p_94041_1_ : Int, p_94041_2_ : ItemStack): Boolean = ???

	override def markDirty(): Unit = {
		EnderHandler.updateTiles(this.freq)
	}

	// todo nbt things

	val tank: FluidTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16)

	override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack = {
		val amount: FluidStack = this.tank.drain(maxDrain, doDrain)
		if (amount != null && doDrain) {
			this.markDirty()
		}
		amount
	}

	override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = {
		if (this.tank.getFluidAmount == 0)
			return null
		if (this.tank.getFluid.getFluid != resource.getFluid)
			return null
		this.drain(from, resource.amount, doDrain)
	}

	override def canFill(from: ForgeDirection, fluid: Fluid): Boolean = {
		this.tank.getFluidAmount == 0 ||
				(this.tank.getFluid.getFluid == fluid && tank.getFluidAmount < tank.getCapacity)
	}

	override def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = {
		this.tank.getFluidAmount > 0
	}

	override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int = {
		val amount: Int = this.tank.fill(resource, doFill)
		if (amount > 0 && doFill) {
			this.markDirty()
		}
		amount
	}

	override def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] = {
		Array[FluidTankInfo](new FluidTankInfo(this.tank))
	}

}
