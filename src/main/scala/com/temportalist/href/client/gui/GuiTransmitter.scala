package com.temportalist.href.client.gui

import com.temportalist.href.common.inventory.ContainerTransmitter
import com.temportalist.href.common.tile.TETransmitter
import com.temportalist.origin.wrapper.client.gui.GuiContainerWrapper
import net.minecraft.entity.player.EntityPlayer

/**
 *
 *
 * @author TheTemportalist
 */
class GuiTransmitter(p: EntityPlayer, te: TETransmitter) extends GuiContainerWrapper(
	176, 166, new ContainerTransmitter(p, te)
) {

}
