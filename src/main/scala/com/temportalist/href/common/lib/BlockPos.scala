package com.temportalist.href.common.lib

import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager

/**
 *
 *
 * @author TheTemportalist
 */
class BlockPos(val x: Int, val y: Int, val z: Int, val dimid: Int) {

	def getWorld(): World = {
		DimensionManager.getWorld(this.dimid)
	}

	def getBlock(): Block = {
		this.getWorld().getBlock(this.x, this.y, this.z)
	}

	def getMeta(): Int = {
		this.getWorld().getBlockMetadata(this.x, this.y, this.z)
	}

	def getTile(): TileEntity = {
		this.getWorld().getTileEntity(this.x, this.y, this.z)
	}

}
