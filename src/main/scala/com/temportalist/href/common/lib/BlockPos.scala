package com.temportalist.href.common.lib

import com.temportalist.origin.library.common.lib.vec.Vector3O
import net.minecraft.block.Block
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager

/**
 *
 *
 * @author TheTemportalist
 */
class BlockPos(var vec: Vector3O, var dimid: Int) {

	def this(tile: TileEntity) {
		// todo
		this(new Vector3O(tile.xCoord, tile.yCoord, tile.zCoord),
			tile.getWorldObj.provider.dimensionId)
	}

	def this(tag: NBTTagCompound) {
		this(null, 0)
		this.fromNBT(tag)
	}

	def isLoaded(): Boolean = {
		this.getWorld() != null &&
				this.getWorld().getChunkProvider.chunkExists(vec.x_i() >> 4, vec.z_i() >> 4)
	}

	def getWorld(): World = {
		DimensionManager.getWorld(this.dimid)
	}

	def getBlock(): Block = {
		if (this.isLoaded())
			this.vec.getBlock(this.getWorld())
	}

	def getMeta(): Int = {
		if (this.isLoaded())
			this.vec.getMetadata(this.getWorld())
	}

	def getTile(): TileEntity = {
		if (this.isLoaded())
			this.vec.getTile(this.getWorld())
	}

	def toNBT(tag: NBTTagCompound): Unit = {
		this.vec.toNBT(tag)
		tag.setInteger("dimid", this.dimid)
	}

	def fromNBT(tag: NBTTagCompound): Unit = {
		this.vec = new Vector3O(tag)
		this.dimid = tag.getInteger("dimid")
	}

	override def hashCode(): Int = {
		var hash: Int = 1
		hash = hash * 31 + this.vec.hashCode()
		hash = hash * 31 + this.dimid
		hash
	}

	override def equals(obj: scala.Any): Boolean = {
		obj match {
			case pos: BlockPos =>
				return this.vec.equals(pos.vec) && this.dimid == pos.dimid
			case _ =>
		}
		false
	}

	override def toString: String = {
		"Dim:" + this.dimid + " | " + this.vec
	}

}
