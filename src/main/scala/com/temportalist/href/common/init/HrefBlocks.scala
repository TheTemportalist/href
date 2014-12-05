package com.temportalist.href.common.init

import com.temportalist.href.common.block.BlockTransmitter
import com.temportalist.href.common.tile.TETransmitter
import com.temportalist.origin.library.common.Origin
import com.temportalist.origin.library.common.register.BlockRegister
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block

/**
 *
 *
 * @author TheTemportalist
 */
object HrefBlocks extends BlockRegister {

	var transmitter: Block = null

	override def register(): Unit = {

		this.transmitter = new BlockTransmitter()
		Origin.addBlockToTab(this.transmitter)

	}

	override def registerTileEntities(): Unit = {

		GameRegistry.registerTileEntity(classOf[TETransmitter], "Transmitter")

	}

}
