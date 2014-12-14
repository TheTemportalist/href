package com.temportalist.href.common.lib

import java.util

import com.temportalist.href.api.IDye
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

/**
 *
 *
 * @author TheTemportalist
 */
object Dyes {

	def getDyeColor(itemStack: ItemStack): Int = {
		val oreIDs: Array[Int] = OreDictionary.getOreIDs(itemStack)
		var name: String = null
		for (i <- 0 until oreIDs.length) {
			name = OreDictionary.getOreName(oreIDs(i))
			if (name.equals("gemLapis")) name = "dyeBlue"
			if (name.startsWith("dye")) {
				return Dyes.getDyeColor(itemStack, name.substring(3))
			}
		}
		0xFFFFFF // white
	}

	def getDyeColor(itemStack: ItemStack, oreDictName: String): Int = {
		if (Dyes.hasName(oreDictName))
			Dyes.getColorFromName(oreDictName)
		else itemStack.getItem match {
			case dye: IDye =>
				dye.getColor(itemStack)
			case _ =>
				-1
		}
	}

	val metadataToName: util.List[String] = util.Arrays.asList(
		"Black",
		"Red",
		"Green",
		"Brown",
		"Blue",
		"Purple",
		"Cyan",
		"LightGray",
		"Gray",
		"Pink",
		"Lime",
		"Yellow",
		"LightBlue",
		"Magenta",
		"Orange",
		"White"
	)
	val metadataToColor: util.List[Integer] = util.Arrays.asList(
		0x00000,
		0xFF0000,
		0x00FF00,
		0x643200,
		0x0000FF,
		0x7F007F,
		0x006464,
		0xC0C0C0,
		0x7F7F7F,
		0xFF00FF,
		0x21DE21,
		0xFFFF00,
		0x0064FF,
		0xC000FF,
		0xFF7F00,
		0xFFFFFF
	)

	def hasName(name: String): Boolean = this.metadataToName.contains(name)

	def hasColor(color: Int): Boolean = this.metadataToColor.contains(color)

	def getColorFromName(name: String): Int = {
		this.metadataToColor.get(this.metadataToName.indexOf(name))
	}

	def getNameFromColor(color: Int): String = {
		this.metadataToName.get(this.metadataToColor.indexOf(color))
	}

}
