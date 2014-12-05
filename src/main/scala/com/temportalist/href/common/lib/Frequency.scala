package com.temportalist.href.common.lib

import net.minecraft.nbt.NBTTagCompound

/**
 *
 *
 * @author TheTemportalist
 */
class Frequency(
		private var a: Int, private var b: Int, private var c: Int, private var d: Int,
		private var size: Int
		) {

	def getSize(): Int = this.size

	override def equals(obj: scala.Any): Boolean = {
		obj match {
			case freq: Frequency =>
				return this.a == freq.a && this.b == freq.b && this.c == freq.c &&
						this.d == freq.d && this.size == freq.size
			case _ =>
		}
		false
	}

	def toNBT(tagCom: NBTTagCompound): Unit = {
		tagCom.setInteger("a", this.a)
		tagCom.setInteger("b", this.b)
		tagCom.setInteger("c", this.c)
		tagCom.setInteger("d", this.d)
		tagCom.setInteger("size", this.size)
	}

	def fromNBT(tagCom: NBTTagCompound): Unit = {
		this.a = tagCom.getInteger("a")
		this.b = tagCom.getInteger("b")
		this.c = tagCom.getInteger("c")
		this.d = tagCom.getInteger("d")
		this.size = tagCom.getInteger("size")
	}

}
