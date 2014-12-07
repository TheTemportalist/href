package com.temportalist.href.common.lib

import codechicken.lib.vec.{Quat, Vector3}
import com.temportalist.href.common.Href
import com.temportalist.origin.library.client.utility.Rendering
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.{AxisAlignedBB, ResourceLocation}
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author TheTemportalist
 */
class DyeButton(val index: Int, min: Vector3, max: Vector3) {

	val verticies: Array[Vector3] = new Array[Vector3](8)

	this.setVerts(min, max)

	def this(i: Int, aabb: AxisAlignedBB) {
		this(i, new Vector3(aabb.minX, aabb.minY, aabb.minZ),
			new Vector3(aabb.maxX, aabb.maxY, aabb.maxZ))
	}

	def setVerts(min: Vector3, max: Vector3): Unit = {
		this.verticies(0) = new Vector3(min.x, min.y, min.z)
		this.verticies(1) = new Vector3(max.x, min.y, min.z)
		this.verticies(2) = new Vector3(max.x, min.y, max.z)
		this.verticies(3) = new Vector3(min.x, min.y, max.z)

		this.verticies(4) = new Vector3(min.x, max.y, min.z)
		this.verticies(5) = new Vector3(max.x, max.y, min.z)
		this.verticies(6) = new Vector3(max.x, max.y, max.z)
		this.verticies(7) = new Vector3(min.x, max.y, max.z)
	}

	//this.fixRotation(1)

	def fixRotation(unit: Int): Unit = {
		val quat2: Quat = Quat.aroundAxis(1, 0, 0, -0.5 * 3.14159 * unit)
		for (i <- 0 until this.verticies.length) {
			quat2.rotate(this.verticies(i))
		}
	}

	def translate(vec: Vector3): Unit = {
		for (i <- 0 until this.verticies.length)
			this.verticies(i).add(vec)
	}

	def rotate(px: Double, py: Double, pz: Double, ax: Double, ay: Double, az: Double,
			angle: Double): Unit = {
		val quat: Quat = Quat.aroundAxis(ax, ay, az, angle)
		for (i <- 0 until this.verticies.length) {
			this.verticies(i).add(-px, -py, -pz)
			quat.rotate(this.verticies(i))
			this.verticies(i).add(px, py, pz)
		}
	}

	def rotateMeta(metadata: Int): Unit = {
		metadata match {
			case 1 =>
				this.rotate(0, 0, 0, 1, 0, 0, Math.toRadians(180))
			case 2 =>
				this.rotate(0, 0, 0, 1, 0, 0, Math.toRadians(90))
			case 3 =>
				this.rotate(0, 0, 0, -1, 0, 0, Math.toRadians(90))
			case 4 =>
				this.rotate(0, 0, 0, 0, 0, -1, Math.toRadians(90))
			case 5 =>
				this.rotate(0, 0, 0, 0, 0, 1, Math.toRadians(90))
			case _ =>
		}
	}

	def fromAABB(aabb: AxisAlignedBB): Unit = {
		this.setVerts(
			new Vector3(aabb.minX, aabb.minY, aabb.minZ),
			new Vector3(aabb.maxX, aabb.maxY, aabb.maxZ)
		)
	}

	def copy(): DyeButton = new DyeButton(this.index, this.min, this.max)

	val buttonTex: ResourceLocation = new
					ResourceLocation(Href.modid, "textures/blocks/Transmitter.png")

	@SideOnly(Side.CLIENT)
	def draw(color: Int, meta: Int): Unit = {

		Rendering.bindResource(this.buttonTex)
		GL11.glColor4d(
			this.getR(color).toDouble / 255D,
			this.getG(color).toDouble / 255D,
			this.getB(color).toDouble / 255D,
			1D
		)

		val x1: Double = 5D / 64D
		val x2: Double = 10D / 64D
		val y1: Double = x1
		val y2: Double = x2
		val y3: Double = 12D / 64D
		val uv: Array[Double] = Array[Double](x1, x2, y1, y2, y3)

		val tess: Tessellator = Tessellator.instance
		tess.setColorRGBA_F(1F, 1F, 1F, 1F)
		tess.startDrawingQuads()

		meta match {
			case 0 | 1 =>
				this.drawTB(uv)
			case 2 | 3 =>
				this.drawLR(uv)
			case 4 | 5 =>
				this.drawFB(uv)
			case _ =>
		}

		tess.draw()

	}

	def drawTB(uv: Array[Double]): Unit = {
		// top
		this.addVecUV(this.verticies(4), uv(0), uv(3))
		this.addVecUV(this.verticies(7), uv(1), uv(3))
		this.addVecUV(this.verticies(6), uv(1), uv(2))
		this.addVecUV(this.verticies(5), uv(0), uv(2))

		// bottom
		this.addVecUV(this.verticies(1), uv(0), uv(3))
		this.addVecUV(this.verticies(2), uv(1), uv(3))
		this.addVecUV(this.verticies(3), uv(1), uv(2))
		this.addVecUV(this.verticies(0), uv(0), uv(2))

		// front
		this.addVecUV(this.verticies(0), uv(0), uv(4))
		this.addVecUV(this.verticies(3), uv(1), uv(4))
		this.addVecUV(this.verticies(7), uv(1), uv(3))
		this.addVecUV(this.verticies(4), uv(0), uv(3))

		// back
		this.addVecUV(this.verticies(2), uv(0), uv(4))
		this.addVecUV(this.verticies(1), uv(1), uv(4))
		this.addVecUV(this.verticies(5), uv(1), uv(3))
		this.addVecUV(this.verticies(6), uv(0), uv(3))

		// right
		this.addVecUV(this.verticies(3), uv(0), uv(4))
		this.addVecUV(this.verticies(2), uv(1), uv(4))
		this.addVecUV(this.verticies(6), uv(1), uv(3))
		this.addVecUV(this.verticies(7), uv(0), uv(3))

		// left
		this.addVecUV(this.verticies(1), uv(0), uv(4))
		this.addVecUV(this.verticies(0), uv(1), uv(4))
		this.addVecUV(this.verticies(4), uv(1), uv(3))
		this.addVecUV(this.verticies(5), uv(0), uv(3))
	}

	def drawLR(uv: Array[Double]): Unit = {
		this.addVecUV(this.verticies(4), uv(1), uv(4))
		this.addVecUV(this.verticies(7), uv(1), uv(3))
		this.addVecUV(this.verticies(6), uv(0), uv(3))
		this.addVecUV(this.verticies(5), uv(0), uv(4))

		this.addVecUV(this.verticies(1), uv(1), uv(4))
		this.addVecUV(this.verticies(2), uv(1), uv(3))
		this.addVecUV(this.verticies(3), uv(0), uv(3))
		this.addVecUV(this.verticies(0), uv(0), uv(4))

		this.addVecUV(this.verticies(0), uv(1), uv(4))
		this.addVecUV(this.verticies(3), uv(1), uv(3))
		this.addVecUV(this.verticies(7), uv(0), uv(3))
		this.addVecUV(this.verticies(4), uv(0), uv(4))

		this.addVecUV(this.verticies(2), uv(0), uv(3))
		this.addVecUV(this.verticies(1), uv(0), uv(4))
		this.addVecUV(this.verticies(5), uv(1), uv(4))
		this.addVecUV(this.verticies(6), uv(1), uv(3))

		this.addVecUV(this.verticies(3), uv(1), uv(3))
		this.addVecUV(this.verticies(2), uv(1), uv(2))
		this.addVecUV(this.verticies(6), uv(0), uv(2))
		this.addVecUV(this.verticies(7), uv(0), uv(3))

		this.addVecUV(this.verticies(1), uv(0), uv(2))
		this.addVecUV(this.verticies(0), uv(0), uv(3))
		this.addVecUV(this.verticies(4), uv(1), uv(3))
		this.addVecUV(this.verticies(5), uv(1), uv(2))
	}

	def drawFB(uv: Array[Double]): Unit = {
		this.addVecUV(this.verticies(4), uv(0), uv(4))
		this.addVecUV(this.verticies(7), uv(1), uv(4))
		this.addVecUV(this.verticies(6), uv(1), uv(3))
		this.addVecUV(this.verticies(5), uv(0), uv(3))

		this.addVecUV(this.verticies(1), uv(0), uv(4))
		this.addVecUV(this.verticies(2), uv(1), uv(4))
		this.addVecUV(this.verticies(3), uv(1), uv(3))
		this.addVecUV(this.verticies(0), uv(0), uv(3))

		this.addVecUV(this.verticies(0), uv(0), uv(3))
		this.addVecUV(this.verticies(3), uv(1), uv(3))
		this.addVecUV(this.verticies(7), uv(1), uv(2))
		this.addVecUV(this.verticies(4), uv(0), uv(2))

		this.addVecUV(this.verticies(2), uv(0), uv(3))
		this.addVecUV(this.verticies(1), uv(1), uv(3))
		this.addVecUV(this.verticies(5), uv(1), uv(2))
		this.addVecUV(this.verticies(6), uv(0), uv(2))

		this.addVecUV(this.verticies(3), uv(0), uv(3))
		this.addVecUV(this.verticies(2), uv(0), uv(4))
		this.addVecUV(this.verticies(6), uv(1), uv(4))
		this.addVecUV(this.verticies(7), uv(1), uv(3))

		this.addVecUV(this.verticies(1), uv(0), uv(3))
		this.addVecUV(this.verticies(0), uv(0), uv(4))
		this.addVecUV(this.verticies(4), uv(1), uv(4))
		this.addVecUV(this.verticies(5), uv(1), uv(3))
	}

	// todo move to Vector3
	@SideOnly(Side.CLIENT)
	def addVecUV(vec: Vector3, u: Double, v: Double): Unit = {
		Tessellator.instance.addVertexWithUV(vec.x, vec.y, vec.z, u, v)
	}

	// todo move these 3 color funcs to Vector3

	def getR(color: Int): Int = ((0xff000000 | color) >> 16) & 0xFF

	def getG(color: Int): Int = ((0xff000000 | color) >> 8) & 0xFF

	def getB(color: Int): Int = ((0xff000000 | color) >> 0) & 0xFF

	def minv(): Vector3 = this.min.copy()

	def maxv(): Vector3 = this.max.copy()

	def toAABB(): AxisAlignedBB = {
		AxisAlignedBB.getBoundingBox(
			this.min.x, this.min.y, this.min.z,
			this.max.x, this.max.y, this.max.z
		)
	}

}
