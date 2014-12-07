package com.temportalist.href.common.lib

import com.temportalist.origin.library.common.lib.vec.Vector3b
import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager

/**
 *
 *
 * @author TheTemportalist
 */
class BlockPos(val vec: Vector3b, val dimid: Int) {

	def getWorld(): World = {
		DimensionManager.getWorld(this.dimid)
	}

	def getBlock(): Block = {
		this.vec.getBlock(this.getWorld())
	}

	def getMeta(): Int = {
		this.vec.getBlockMetadata(this.getWorld())
	}

	def getTile(): TileEntity = {
		this.vec.getTileEntity(this.getWorld())
	}

}
