package com.temportalist.href.common.tile

import java.util

import codechicken.lib.raytracer.IndexedCuboid6
import codechicken.lib.vec.{Cuboid6, Vector3}
import com.temportalist.href.common.lib._
import com.temportalist.origin.library.common.lib.vec.Vector3b
import com.temportalist.origin.wrapper.common.tile.TEWrapper
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

	private var freq: Frequency = new Frequency(0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF)
	private var coord: Vector3b = null
	private var dirFrom: ForgeDirection = null
	private var isActive: Boolean = false
	// todo switch on/off on block clicked like translocator

	private val perPixel: Double = 0.03125D
	private val buttonS: Double = perPixel * 5
	private val buttonH: Double = 0.0625D // .125 / 2

	override def updateEntity(): Unit = {
		/*
		val inv: Object = this.getAttachedInv()
		if (inv != null) {
			val ender: EnderInventory = this.getEnderInv()
			if (ender != null) {
				if (this.isActive) {
					EnderHandler.sync(inv, ender, fromInv = true)
					ender.markDirty()
				}
				else if (this.isValidInv(inv, ender)) {
					this.isActive = true
				}
			}
		}
		else this.isActive = false
		*/

	}

	def getAttachedInv(): Object = {
		if (this.coord == null) return null
		val te: TileEntity = this.coord.getTileEntity(this.worldObj)
		if (te != null && (te.isInstanceOf[IInventory] || te.isInstanceOf[IFluidHandler])) te
		else null
	}

	def setAttachedInv(dir: ForgeDirection, coord: Vector3b): Unit = {
		this.dirFrom = dir
		this.coord = coord
	}

	def getEnderInv(): EnderInventory = {
		EnderHandler.getEnder(this.freq)
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

	def addAttributes(itemStack: ItemStack): Unit = {
		val tagCom: NBTTagCompound = new NBTTagCompound
		val freqTag: NBTTagCompound = new NBTTagCompound
		this.freq.toNBT(freqTag)
		tagCom.setTag("freq", freqTag)
		itemStack.setTagCompound(tagCom)
	}

	def setAttributes(itemStack: ItemStack): Unit = {
		if (itemStack.hasTagCompound)
			this.freq.fromNBT(itemStack.getTagCompound.getCompoundTag("freq"))

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
		this.freq.setColor(button, color)

	}

}
