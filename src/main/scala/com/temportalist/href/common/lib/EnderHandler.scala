package com.temportalist.href.common.lib

import java.util
import java.util.{Collections, Comparator}

import com.temportalist.href.common.tile.TETransmitter
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{FluidTankInfo, IFluidHandler}

/**
 *
 *
 * @author TheTemportalist
 */
object EnderHandler {

	private val tiles: util.HashMap[Frequency, util.ArrayList[BlockPos]] =
		new util.HashMap[Frequency, util.ArrayList[BlockPos]]()

	def size(): Int = this.tiles.size()

	def toNBT(tag: NBTTagCompound): Unit = {
		val freqList: NBTTagList = new NBTTagList
		val iterTiles: util.Iterator[Frequency] = this.tiles.keySet().iterator()
		var freq: Frequency = null
		var freqTag: NBTTagCompound = null
		var posList: NBTTagList = null
		var iterCoords: util.Iterator[BlockPos] = null
		var posTag: NBTTagCompound = null

		// iterate through all frequencies
		while (iterTiles.hasNext) {
			// local variable storing of frequency
			freq = iterTiles.next()
			// create a new tag for each frequency
			freqTag = new NBTTagCompound

			// save each frequency to the tag
			freq.toNBT(freqTag)

			// create a new list of poses per freq
			posList = new NBTTagList
			// iterate through all poses per freq
			iterCoords = EnderHandler.tiles.get(freq).iterator()
			while (iterCoords.hasNext) {
				// new tag per pos
				posTag = new NBTTagCompound
				// save pos to tag
				iterCoords.next().toNBT(posTag)
				// save tag to list of poses
				posList.appendTag(posTag)
			}
			// save list of poses to freq tag
			freqTag.setTag("posList", posList)

			// save freq tag to list of freq's and pos's
			freqList.appendTag(freqTag)
		}
		// save list of freq/pos to tag
		tag.setTag("Href.EnderHandler.tiles", freqList)
	}

	def fromNBT(tag: NBTTagCompound): Unit = {
		val freqList: NBTTagList = tag.getTagList("Href.EnderHandler.tiles", 10)
		var freqTag: NBTTagCompound = null
		var posList: NBTTagList = null
		var posArrayList: util.ArrayList[BlockPos] = null

		this.tiles.clear()
		for (i <- 0 until freqList.tagCount()) {
			freqTag = freqList.getCompoundTagAt(i)

			posList = freqTag.getTagList("posList", 10)
			posArrayList = new util.ArrayList[BlockPos]()
			for (j <- 0 until posList.tagCount()) {
				posArrayList.add(new BlockPos(posList.getCompoundTagAt(j)))
			}

			this.tiles.put(new Frequency(freqTag), new util.ArrayList[BlockPos](posArrayList))
		}
	}

	def checkInventories(): Unit = {
		val iter: util.Iterator[Frequency] = this.tiles.keySet().iterator()
		var freq: Frequency = null
		while (iter.hasNext) {
			freq = iter.next()
			if (!this.tiles.get(freq).isEmpty) {
				val iterTile: util.Iterator[BlockPos] = this.tiles.get(freq).listIterator()
				while (iterTile.hasNext) {
					if (iterTile.next().getTile() == null) iterTile.remove()
				}
			}
			if (this.tiles.get(freq).isEmpty) {
				iter.remove()
			}
		}
	}

	def addTile(freq: Frequency, pos: BlockPos): Unit = {
		this.checkInventories()
		if (!this.tiles.containsKey(freq)) {
			//println("new list of positions")
			this.tiles.put(freq, new util.ArrayList[BlockPos]())
		}

		this.tiles.get(freq).add(pos)
		//println("added a tile and size is now: " + this.tiles.get(freq).size())
	}

	def removeTile(freq: Frequency, pos: BlockPos): Unit = {
		if (this.tiles.containsKey(freq) && this.tiles.get(freq).contains(pos)) {
			this.tiles.get(freq).remove(pos)
		}
		this.checkInventories()
	}

	def updateTile(oldFreq: Frequency, newFreq: Frequency, pos: BlockPos): Unit = {
		this.removeTile(oldFreq, pos)
		this.addTile(newFreq, pos)
	}

	private def getTiles(freq: Frequency, tileType: Class[_],
			callback: (TileEntity) => Unit): Unit = {
		this.checkInventories()
		var tile: TileEntity = null
		for (tileIndex <- 0 until this.tiles.get(freq).size()) {
			tile = this.tiles.get(freq).get(tileIndex).getTile()
			if (tile != null && tileType.isAssignableFrom(tile.getClass)) {
				callback(tile)
			}
		}
	}

	def getStacks(freq: Frequency, filters: util.ArrayList[Filter],
			sorters: util.ArrayList[Comparator[ItemStack]]): util.ArrayList[ItemStack] = {
		val stacks: util.ArrayList[ItemStack] = this.getStacks_Unsorted(freq)
		// sort the list of stacks
		this.sortStacks(this.filterStacks(stacks, filters), sorters)
		stacks
	}

	private def getStacks_Unsorted(freq: Frequency): util.ArrayList[ItemStack] = {
		this.checkInventories()
		// create a list to store stacks
		val stacks: util.ArrayList[ItemStack] = new util.ArrayList[ItemStack]()
		if (!this.tiles.containsKey(freq)) {
			//LogHelper.info(Href.modname, "No tiles stored with frequency " + freq)
			return stacks
		}

		def callbackInv(tile: TileEntity): Unit = {
			tile match {
				case trans: TETransmitter =>
					trans.getAttachedInv() match {
						case inv: IInventory =>
							var stack: ItemStack = null
							for (slotID <- 0 until inv.getSizeInventory) {
								stack = inv.getStackInSlot(slotID)
								if (stack != null) {
									stacks.add(stack.copy())
								}
							}
						case _ =>
					}
				case _ =>
			}
		}

		this.getTiles(freq, classOf[IInventory], callbackInv)

		stacks
	}

	private def addStack(stacks: util.List[ItemStack], stack: ItemStack): Unit = {
		// iterate through all the current stacks
		for (i <- 0 until stacks.size()) {
			// check to see if stack matches type
			if (stacks.get(i).getItem == stack.getItem &&
					stacks.get(i).getItemDamage == stack.getItemDamage &&
					ItemStack.areItemStackTagsEqual(stacks.get(i), stack)) {
				// add stack size
				stacks.get(i).stackSize += stack.stackSize
				return
			}
		}
		// add stack to list, because it is not already there
		stacks.add(stack)
	}

	private def filterStacks(stacks: util.ArrayList[ItemStack],
			filters: util.ArrayList[Filter]): util.ArrayList[ItemStack] = {
		val stackList: util.ArrayList[ItemStack] = new util.ArrayList[ItemStack]()
		for (i <- 0 until filters.size()) for (j <- 0 until stacks.size()) {
			if (filters.get(i).isApplicable(stacks.get(i)))
				stackList.add(stacks.get(i).copy())
		}
		stackList
	}

	private def sortStacks(stacks: util.ArrayList[ItemStack],
			sorters: util.ArrayList[Comparator[ItemStack]]): Unit = {
		for (i <- 0 until sorters.size()) Collections.sort(stacks, sorters.get(i))
	}

	class Inventory() {

		private var stacks: Array[ItemStack] = null

		private var tankInfo: Array[FluidTankInfo] = null

		def this(inv: IInventory) {
			this()
			val array: Array[ItemStack] = new Array[ItemStack](inv.getSizeInventory)
			var stack: ItemStack = null
			for (i <- 0 until array.length) {
				stack = inv.getStackInSlot(i)
				if (stack != null) array(i) = stack.copy()
			}
			this.setStacks(array)
		}

		def this(inv: IFluidHandler) {
			this()
			this.setTanks(inv.getTankInfo(ForgeDirection.UNKNOWN))
		}

		def setStacks(content: Array[ItemStack]): Inventory = {
			this.stacks = content
			this
		}

		def getStacks(): Array[ItemStack] = this.stacks

		def setTanks(content: Array[FluidTankInfo]): Inventory = {
			this.tankInfo = content
			this
		}

		def getTanks(): Array[FluidTankInfo] = this.tankInfo

		def toNBT(): NBTTagCompound = {
			val tagCom: NBTTagCompound = new NBTTagCompound

			val stacksTag: NBTTagList = new NBTTagList
			for (i <- 0 until this.stacks.length) {
				if (this.stacks(i) != null) {
					val stack: NBTTagCompound = new NBTTagCompound

					stack.setInteger("slot", i)
					this.stacks(i).writeToNBT(stack)

					stacksTag.appendTag(stack)
				}
			}
			tagCom.setTag("stacks", stacksTag)
			tagCom.setInteger("stacksSize", this.stacks.length)

			val fluidsTag: NBTTagList = new NBTTagList
			for (i <- 0 until this.tankInfo.length) {
				if (this.tankInfo(i) != null) {
					val tank: NBTTagCompound = new NBTTagCompound

					tank.setInteger("slot", i)
					tank.setInteger("capacity", this.tankInfo(i).capacity)
					this.tankInfo(i).fluid.writeToNBT(tank)

					fluidsTag.appendTag(tank)
				}
			}
			tagCom.setTag("fluids", fluidsTag)
			tagCom.setInteger("fluidsSize", this.tankInfo.length)

			tagCom
		}

	}

}
