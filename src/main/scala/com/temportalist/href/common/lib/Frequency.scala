package com.temportalist.href.common.lib

import net.minecraft.nbt.NBTTagCompound

/**
 *
 *
 * @author TheTemportalist
 */
class Frequency(private var colors: Array[Int]) {

	def this(a: Int, b: Int, c: Int, d: Int) {
		this(Array[Int](a, b, c, d))
	}

	override def equals(obj: scala.Any): Boolean = {
		obj match {
			case freq: Frequency =>
				return java.util.Arrays.equals(this.colors, freq.colors)
			case _ =>

		}
		false
	}

	def toNBT(tagCom: NBTTagCompound): Unit = {
		tagCom.setIntArray("colors", this.colors)
	}

	def fromNBT(tagCom: NBTTagCompound): Unit = {
		this.colors = tagCom.getIntArray("colors")
	}

	def getColor(i: Int): Int = this.colors(i)

	def setColor(i: Int, color: Int): Unit = {
		this.colors(i) = color
	}

}
