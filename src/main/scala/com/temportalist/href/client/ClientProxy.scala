package com.temportalist.href.client

import java.util

import com.temportalist.href.client.render.{ItemTransmitterRenderer, RenderTransmitter}
import com.temportalist.href.common.CommonProxy
import com.temportalist.href.common.init.HrefBlocks
import com.temportalist.href.common.tile.TETransmitter
import cpw.mods.fml.client.IModGuiFactory
import cpw.mods.fml.client.IModGuiFactory.{RuntimeOptionCategoryElement, RuntimeOptionGuiHandler}
import cpw.mods.fml.client.registry.ClientRegistry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.client.MinecraftForgeClient

/**
 *
 *
 * @author TheTemportalist
 */
class ClientProxy() extends CommonProxy with IModGuiFactory {

	override def registerRender(): Unit = {

		val tesr: TileEntitySpecialRenderer = new RenderTransmitter()
		ClientRegistry.bindTileEntitySpecialRenderer(classOf[TETransmitter], tesr)
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HrefBlocks.transmitter),
			new ItemTransmitterRenderer(tesr))

	}

	override def getClientElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int, tileEntity: TileEntity): AnyRef = null

	override def initialize(minecraftInstance: Minecraft): Unit = {}

	override def runtimeGuiCategories(): util.Set[RuntimeOptionCategoryElement] = null

	override def getHandlerFor(
			element: RuntimeOptionCategoryElement): RuntimeOptionGuiHandler = null

	override def mainConfigGuiClass(): Class[_ <: GuiScreen] = null // todo classOf[GuiClass]

}
