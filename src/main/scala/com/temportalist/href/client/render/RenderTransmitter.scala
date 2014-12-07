package com.temportalist.href.client.render

import com.temportalist.href.client.render.model.ModelTransmitter
import com.temportalist.href.common.Href
import com.temportalist.href.common.lib.DyeButton
import com.temportalist.href.common.tile.TETransmitter
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

		GL11.glPushMatrix()
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
		GL11.glPopMatrix()

		GL11.glTranslated(-0.5, -0.5, -0.5)
		GL11.glDisable(GL11.GL_LIGHTING)
		tileEntity match {
			case trans: TETransmitter =>
				val buttons: Array[DyeButton] = trans.getButtons(meta)
				for (i <- 0 until buttons.length) {
					GL11.glPushMatrix()
					buttons(i).draw(trans.getFrequency().getColor(i), meta)
					GL11.glPopMatrix()
				}
			case _ =>
		}
		GL11.glEnable(GL11.GL_LIGHTING)

	}

}
