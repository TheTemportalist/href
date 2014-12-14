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

	def this(tag: NBTTagCompound) {
		this(0, 0, 0, 0)
		this.fromNBT(tag)
	}

	override def equals(obj: scala.Any): Boolean = {
		obj match {
			case freq: Frequency =>
				if (this.colors.length == freq.colors.length) {
					for (i <- 0 until this.colors.length) {
						if (this.colors(i) != freq.colors(i)) return false
					}
					return true
				}
			case _ =>

		}
		false
	}

	override def hashCode(): Int = {
		var hash: Int = 1
		for (i <- 0 until this.colors.length)
			hash = hash * 31 + this.colors(i).hashCode()
		hash
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

	def copy(): Frequency = new Frequency(this.colors)

}
