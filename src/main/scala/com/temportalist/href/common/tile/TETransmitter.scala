package com.temportalist.href.common.tile

import java.util
import java.util.Comparator

import codechicken.lib.raytracer.IndexedCuboid6
import codechicken.lib.vec.{Cuboid6, Vector3}
import com.temportalist.href.common.lib._
import com.temportalist.origin.library.common.lib.vec.Vector3O
import com.temportalist.origin.wrapper.common.tile.TEWrapper
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.IFluidHandler

/**
 *
 *
 * @author TheTemportalist
 */
class TETransmitter() extends TEWrapper("Transmitter") {

	private val freq: Frequency = new Frequency(0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF)
	private var coord: Vector3O = null
	private var dirFrom: ForgeDirection = null

	private val perPixel: Double = 0.03125D
	private val buttonS: Double = perPixel * 5
	private val buttonH: Double = 0.0625D // .125 / 2

	override def updateEntity(): Unit = {
		super.updateEntity()

		if (this.dirFrom == null)
			this.dirFrom = ForgeDirection.getOrientation(this.getBlockMetadata)
		if (this.coord == null) {
			// todo
			this.coord = new Vector3O(this.xCoord, this.yCoord, this.zCoord).add(this.dirFrom)
		}

	}

	def getAttachedInv(): Object = {
		if (this.coord == null) return null
		val te: TileEntity = this.coord.getTile(this.worldObj)
		if (te != null && (te.isInstanceOf[IInventory] || te.isInstanceOf[IFluidHandler])) te
		else null
	}

	def isValidInv(inv: Object, ender: EnderInventory): Boolean = {
		inv match {
			case itemInv: IInventory =>
				return ender.getSizeInventory == -1 ||
						itemInv.getSizeInventory == ender.getSizeInventory
			case fluidInv: IFluidHandler =>
				return ender.getTankInfo(null) == null ||
						fluidInv.getTankInfo(this.dirFrom).length == ender.getTankInfo(null).length
			case _ =>
		}
		false
	}

	def getFrequency(): Frequency = this.freq

	override def getDrops(drops: util.ArrayList[ItemStack], block: Block,
			metadata: Int): Unit = {
		drops.clear()
		val stack: ItemStack = new ItemStack(block, 1, metadata)
		stack.setTagCompound(new NBTTagCompound)
		this.toNBT(stack.getTagCompound)
		drops.add(stack)
	}

	def addAttributes(itemStack: ItemStack): Unit = {
		val tagCom: NBTTagCompound = new NBTTagCompound

		this.writeToNBT(tagCom)

		itemStack.setTagCompound(tagCom)
	}

	def setAttributes(itemStack: ItemStack): Unit = {
		if (itemStack.hasTagCompound)
			this.freq.fromNBT(itemStack.getTagCompound.getCompoundTag("freq"))
		if (!this.worldObj.isRemote) {
			EnderHandler.addTile(this.freq, new BlockPos(this))
		}
	}

	def getButtons(meta: Int): Array[DyeButton] = {
		val buttons: Array[DyeButton] = new Array[DyeButton](4)

		val minPos: Double = 0.25
		val x2y1: Double = minPos + this.perPixel * 2
		val x1y2: Double = minPos + this.buttonS + (this.perPixel * 4)
		val area: Vector3 = new Vector3(this.buttonS, this.buttonH, this.buttonS)
		val posi: Array[Vector3] = Array[Vector3](
			new Vector3(x1y2, 0.125, x2y1),
			new Vector3(x1y2, 0.125, x1y2),
			new Vector3(x2y1, 0.125, x2y1),
			new Vector3(x2y1, 0.125, x1y2)
		)

		for (i <- 0 until buttons.length) {
			buttons(i) = new DyeButton(i, BoundsHelper.getBounds(posi(i), area, meta))
		}

		buttons
	}

	def addCuboids(cuboids: util.List[IndexedCuboid6], meta: Int): Unit = {
		val tevec: Vector3 = Vector3.fromTileEntity(this)
		val buttons: Array[DyeButton] = this.getButtons(meta)
		for (i <- 0 until buttons.length) {
			cuboids.add(new IndexedCuboid6(i + 1,
				new Cuboid6(buttons(i).minv(), buttons(i).maxv()).add(tevec)))
		}
	}

	def setButtonColor(button: Int, color: Int): Unit = {
		val oldFreq: Frequency = this.freq.copy()
		this.freq.setColor(button, color)
		EnderHandler.updateTile(oldFreq, this.freq, new BlockPos(this))
		this.markDirty()
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	override def writeToNBT(tagCom: NBTTagCompound): Unit = {
		super.writeToNBT(tagCom)
		this.toNBT(tagCom)
	}

	override def readFromNBT(tagCom: NBTTagCompound): Unit = {
		super.readFromNBT(tagCom)
		this.fromNBT(tagCom)
	}

	def toNBT(tagCom: NBTTagCompound): Unit = {
		val freqTag: NBTTagCompound = new NBTTagCompound
		this.freq.toNBT(freqTag)
		tagCom.setTag("freq", freqTag)
	}

	def fromNBT(tagCom: NBTTagCompound): Unit = {
		this.freq.fromNBT(tagCom.getCompoundTag("freq"))
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private val filters: util.ArrayList[Filter] = new util.ArrayList[Filter]()
	private val sorters: util.ArrayList[Comparator[ItemStack]] =
		new util.ArrayList[Comparator[ItemStack]]()
	private val stacks: util.ArrayList[ItemStack] = new util.ArrayList[ItemStack]()

	def updateStacks(): Unit = {
		this.stacks.clear()
		this.stacks.addAll(EnderHandler.getStacks(this.freq, this.filters, this.sorters))
	}

	def getStacks(): util.ArrayList[ItemStack] = {
		this.updateStacks()
		this.stacks
	}

	override def getSizeInventory: Int = {
		this.updateStacks()
		this.stacks.size()
	}

	override def getStackInSlot(slotID: Int): ItemStack = {
		this.updateStacks()
		val shift: Int = 0 // todo
		val i: Int = slotID + shift
		if (i >= 0 && i < this.stacks.size())
			this.stacks.get(i)
		else null
	}

	override def decrStackSize(slotID: Int, decrement: Int): ItemStack = {
		this.updateStacks()
		// Check to see if self has a inventory
		// check if it is a valid slot
		if (slotID < this.stacks.size()) {
			// check to see of stack in slot is occupied
			if (this.stacks.get(slotID) != null) {
				// get a copy of stack in slot
				var itemStack: ItemStack = this.stacks.get(slotID).copy()
				// if extracting more than exists
				if (itemStack.stackSize <= decrement) {
					// remove internal stack
					this.stacks.set(slotID, null)
					// mark for resync
					this.markDirty()
					// return the extracted stack
					return itemStack
				}
				else {
					// split the stack in slot by the decrement,
					// leaving (stack in slot's stack size - decrement)
					// of the internal stack behind
					itemStack = this.stacks.get(slotID).splitStack(decrement)

					// if the stack in slot is non existant
					if (this.stacks.get(slotID).stackSize <= 0) {
						// remove the dead stack
						this.stacks.set(slotID, null)
					}

					// mark for resync
					this.markDirty()
					// return the extracted stack
					return itemStack
				}
			}
		}
		null
	}

	override def getStackInSlotOnClosing(slotID: Int): ItemStack = {
		this.getStackInSlot(slotID)
	}

	override def setInventorySlotContents(slotID: Int, itemStack: ItemStack): Unit = {
		if (slotID < this.stacks.size()) {
			if (itemStack == null)
				this.stacks.set(slotID, null)
			else
				this.stacks.set(slotID, itemStack.copy())
			this.markDirty()
		}
	}

	override def getInventoryName: String = "ender.inventory"

	override def hasCustomInventoryName: Boolean = {
		false
	}

	override def getInventoryStackLimit: Int = 64

	override def isUseableByPlayer(p1: EntityPlayer): Boolean = true

	override def openInventory(): Unit = {}

	override def closeInventory(): Unit = {}

	override def isItemValidForSlot(slotID: Int, itemStack: ItemStack): Boolean =
		slotID < this.stacks.size()

	override def getAccessibleSlotsFromSide(side: Int): Array[Int] = {
		if (side == this.getBlockMetadata) {
			val slotsFromSide: Array[Int] = new Array[Int](this.stacks.size())

			for (i <- 0 to this.stacks.size()) {
				slotsFromSide(i) = i
			}

			return slotsFromSide
		}
		null
	}

	override def canInsertItem(slotID: Int, itemStack: ItemStack, side: Int): Boolean = {
		side == this.getBlockMetadata
	}

	override def canExtractItem(slotID: Int, itemStack: ItemStack, side: Int): Boolean = {
		side == this.getBlockMetadata
	}

}
