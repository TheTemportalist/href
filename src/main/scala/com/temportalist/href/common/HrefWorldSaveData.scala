package com.temportalist.href.common

import com.temportalist.href.common.lib.EnderHandler
import com.temportalist.origin.library.common.lib.LogHelper
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.WorldSavedData

/**
 *
 *
 * @author TheTemportalist
 */
class HrefWorldSaveData(name: String) extends WorldSavedData(name) {

	LogHelper.info(Href.modname, "wsd_init")

	override def writeToNBT(tag: NBTTagCompound): Unit = {
		LogHelper.info(Href.modname, "Saving tiles " + EnderHandler.size())
		EnderHandler.toNBT(tag)
	}

	override def readFromNBT(tag: NBTTagCompound): Unit = {
		EnderHandler.fromNBT(tag)
		LogHelper.info(Href.modname, "Loaded tiles " + EnderHandler.size())
	}

}
