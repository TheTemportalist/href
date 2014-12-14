package com.temportalist.href.common.inventory

import com.temportalist.href.common.tile.TETransmitter
import com.temportalist.origin.wrapper.common.inventory.ContainerWrapper
import net.minecraft.entity.player.EntityPlayer

/**
 *
 *
 * @author TheTemportalist
 */
class ContainerTransmitter(p: EntityPlayer, te: TETransmitter) extends ContainerWrapper(p, te) {

	override protected def registerSlots(): Unit = {

		for (x <- 0 until 9) {
			for (y <- 0 until 4) {
				this.registerSlot(x + (y * 9), 8 + (x * 18), 8 + (y * 18), isFinal = false)
			}
		}

		this.registerPlayerSlots(0, 0)
	}

}
