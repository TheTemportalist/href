package com.temportalist.href.common

import com.temportalist.href.common.inventory.ContainerTransmitter
import com.temportalist.href.common.tile.TETransmitter
import com.temportalist.origin.wrapper.common.ProxyWrapper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist
 */
class CommonProxy() extends ProxyWrapper {

	override def registerRender(): Unit = {

	}

	override def getClientElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int, tileEntity: TileEntity): AnyRef = null

	override def getServerElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int, tileEntity: TileEntity): AnyRef = {
		tileEntity match {
			case null =>
				null
			case te: TETransmitter =>
				new ContainerTransmitter(player, te)
			case _ =>
				null
		}
	}

}
