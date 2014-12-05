package com.temportalist.href.common.tile

import com.temportalist.href.common.lib.{EnderHandler, EnderInventory, Frequency}
import com.temportalist.origin.wrapper.common.tile.TEWrapper
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Vec3
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.IFluidHandler

/**
 *
 *
 * @author TheTemportalist
 */
class TETransmitter() extends TEWrapper("Transmitter") {

	private var freq: Frequency = new Frequency(0, 0, 0, 0, -1)
	// todo change to Vector3
	private var coord: Vec3 = null
	private var dirFrom: ForgeDirection = null
	private var isActive: Boolean = false // todo switch on/off on block clicked like translocator

	override def updateEntity(): Unit = {
		val inv: Object = this.getAttachedInv()
		if (inv != null) {
			val ender: EnderInventory = this.getEnderInv()
			if (this.isActive) {
				EnderHandler.sync(inv, ender, fromInv = true)
				ender.markDirty()
			}
			else if (this.isValidInv(inv, ender)) {
				this.isActive = true
			}
		}
		else this.isActive = false

	}

	def getAttachedInv(): Object = {
		if (this.coord == null) return null
		val te: TileEntity = this.worldObj.getTileEntity(
			this.coord.xCoord.toInt, this.coord.yCoord.toInt, this.coord.zCoord.toInt
		)
		if (te != null && (te.isInstanceOf[IInventory] || te.isInstanceOf[IFluidHandler])) te
		else null
	}

	def setAttachedInv(dir: ForgeDirection, coord: Vec3): Unit = {
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

}
