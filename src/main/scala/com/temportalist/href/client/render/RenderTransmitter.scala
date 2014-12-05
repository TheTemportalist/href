package com.temportalist.href.client.render

import com.temportalist.href.client.render.model.ModelTransmitter
import com.temportalist.href.common.Href
import com.temportalist.origin.wrapper.client.render.TERenderer
import com.temportalist.origin.wrapper.client.render.model.ModelWrapper
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author TheTemportalist
 */
class RenderTransmitter() extends TERenderer(
	new ResourceLocation(Href.modid, "textures/blocks/Transmitter.png")
) {

	val model: ModelWrapper = new ModelTransmitter()

	override protected def render(tileEntity: TileEntity, partialTicks: Float, f5: Float): Unit = {
		val meta: Int = tileEntity.getBlockMetadata

		meta match {
			case 0 =>
			case 1 =>
				GL11.glRotated(180, 1, 0, 0)
			case 2 =>
				GL11.glRotatef(90, 1, 0, 0)
			case 3 =>
				GL11.glRotatef(-90, 1, 0, 0)
			case 4 =>
				GL11.glRotatef(-90, 0, 0, 1)
			case 5 =>
				GL11.glRotatef(90, 0, 0, 1)
			case _ =>
		}

		new ModelTransmitter().renderModel(f5)

	}

}
