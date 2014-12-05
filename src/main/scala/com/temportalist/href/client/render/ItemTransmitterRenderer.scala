package com.temportalist.href.client.render

import com.temportalist.href.common.tile.TETransmitter
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.{ItemRenderType, ItemRendererHelper}
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author TheTemportalist
 */
class ItemTransmitterRenderer(tesr: TileEntitySpecialRenderer) extends IItemRenderer {

	private val tile: TileEntity = new TETransmitter() {
		override def getBlockMetadata: Int = 0
	}

	override def handleRenderType(item: ItemStack, `type`: ItemRenderType): Boolean = true

	override def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack,
			helper: ItemRendererHelper): Boolean = true

	override def renderItem(`type`: ItemRenderType, item: ItemStack, data: AnyRef*): Unit = {
		GL11.glScalef(2, 2, 2)
		`type` match {
			case ItemRenderType.INVENTORY =>
				GL11.glTranslated(0, 0.25, 0)
			case ItemRenderType.ENTITY =>
				GL11.glTranslated(-0.5, 0, -0.5)
			case ItemRenderType.EQUIPPED =>
				GL11.glTranslated(-0.25, 0.25, 0)
			case ItemRenderType.EQUIPPED_FIRST_PERSON =>
				GL11.glTranslated(0.5, 0, -0.25)
			case _ =>
		}
		tesr.renderTileEntityAt(this.tile, 0, 0, 0, 0)
	}

}
