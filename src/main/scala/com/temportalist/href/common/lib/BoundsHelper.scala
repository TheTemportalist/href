package com.temportalist.href.common.lib

import codechicken.lib.vec.Vector3
import net.minecraft.util.AxisAlignedBB

/**
 *
 *
 * @author TheTemportalist
 */
object BoundsHelper {

	def getBounds(pos: Vector3, area: Vector3, meta: Int): AxisAlignedBB = {
		meta match {
			case 0 =>
				AxisAlignedBB.getBoundingBox(
					pos.x,
					pos.y,
					pos.z,
					pos.x + area.x,
					pos.y + area.y,
					pos.z + area.z
				)
			case 1 =>
				AxisAlignedBB.getBoundingBox(
					pos.x, 1D - area.y - pos.y, pos.z,
					pos.x + area.x, 1D - pos.y, pos.z + area.z
				)
			case 2 =>
				AxisAlignedBB.getBoundingBox(
					pos.z, pos.x, pos.y,
					pos.z + area.z, pos.x + area.x, pos.y + area.y
				)
			case 3 =>
				AxisAlignedBB.getBoundingBox(
					pos.z, pos.x, 1D - area.y - pos.y,
					pos.z + area.z, pos.x + area.x, 1D - pos.y
				)
			case 4 =>
				AxisAlignedBB.getBoundingBox(
					pos.y, pos.x, pos.z,
					pos.y + area.y, pos.x + area.x, pos.z + area.z
				)
			case 5 =>
				AxisAlignedBB.getBoundingBox(
					1D - area.y - pos.y, pos.x, pos.z,
					1D - pos.y, pos.x + area.x, pos.z + area.z
				)
			case _ =>
				AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1)
		}
	}

}
