package com.temportalist.href.common.lib

import java.util

import net.minecraft.inventory.IInventory
import net.minecraftforge.fluids.{FluidTankInfo, IFluidHandler}

/**
 *
 *
 * @author TheTemportalist
 */
object EnderHandler {

	private val inventories: util.HashMap[Frequency, EnderInventory] =
		new util.HashMap[Frequency, EnderInventory]()
	private val tiles: util.HashMap[Frequency, util.ArrayList[BlockPos]] =
		new util.HashMap[Frequency, util.ArrayList[BlockPos]]()

	def getEnder(freq: Frequency): EnderInventory = {
		this.inventories.get(freq)
	}

	def checkTiles(): Unit = {
		val iter: util.Iterator[Frequency] = this.tiles.keySet().iterator()
		var freq: Frequency = null
		var tiles: util.ArrayList[BlockPos] = null
		val newTiles: util.ArrayList[BlockPos] = new util.ArrayList[BlockPos]()
		while (iter.hasNext) {
			freq = iter.next()
			tiles = this.tiles.get(freq)
			newTiles.clear()
			for (i <- 0 until tiles.size()) {
				if (tiles.get(i).getTile() != null)
					newTiles.add(tiles.get(i))
			}
			this.tiles.get(freq).clear()
			this.tiles.get(freq).addAll(newTiles)
		}
	}

	def updateTiles(freq: Frequency): Unit = {
		this.checkTiles()
		for (i <- 0 until this.tiles.get(freq).size()) {
			this.sync(this.tiles.get(freq).get(i), this.inventories.get(freq), fromInv = false)
		}
	}

	def removeTile(freq: Frequency, coord: BlockPos): Boolean = {
		this.checkTiles()
		if (this.tiles.containsKey(freq))
			this.tiles.get(freq).remove(coord)
		else false
	}

	def sync(inv1: Object, ender: EnderInventory, fromInv: Boolean): Boolean = {
		inv1 match {
			case itemInv: IInventory =>
				if (itemInv.getSizeInventory != ender.getSizeInventory) return false

				if (fromInv)
					for (i <- 0 until itemInv.getSizeInventory)
						ender.setInventorySlotContents(i, itemInv.getStackInSlot(i))
				else
					for (i <- 0 until ender.getSizeInventory)
						itemInv.setInventorySlotContents(i, ender.getStackInSlot(i))
			case fluidInv: IFluidHandler =>
				val invTanks: Array[FluidTankInfo] = fluidInv.getTankInfo(null)
				val enderTanks: Array[FluidTankInfo] = ender.getTankInfo(null)

				if (invTanks.length != enderTanks.length) return false

				if (fromInv)
					for (i <- 0 until invTanks.length)
						ender.fill(null, invTanks(i).fluid, true)
				else
					for (i <- 0 until enderTanks.length)
						fluidInv.fill(null, enderTanks(i).fluid, true)
			case _ =>
				return false
		}
		true
	}

}
