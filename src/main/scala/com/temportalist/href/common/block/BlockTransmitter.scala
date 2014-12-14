package com.temportalist.href.common.block

import java.util

import codechicken.lib.raytracer.{IndexedCuboid6, RayTracer}
import codechicken.lib.vec.{BlockCoord, Cuboid6, Vector3}
import com.temportalist.href.common.Href
import com.temportalist.href.common.lib._
import com.temportalist.href.common.tile.TETransmitter
import com.temportalist.origin.library.common.helpers.RegisterHelper
import com.temportalist.origin.library.common.lib.LogHelper
import com.temportalist.origin.library.common.lib.vec.Vector3O
import com.temportalist.origin.wrapper.common.block.BlockWrapperTE
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{AxisAlignedBB, MovingObjectPosition, Vec3}
import net.minecraft.world.World
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.IFluidHandler

/**
 *
 *
 * @author TheTemportalist
 */
class BlockTransmitter() extends BlockWrapperTE(Material.rock, Href.modid, "Transmitter",
	classOf[TETransmitter]) {

	RegisterHelper.registerHandler(this, null)

	def getVectorOn(x: Int, y: Int, z: Int, meta: Int): Vector3O = {
		Vector3O.from(x, y, z, ForgeDirection.getOrientation(meta))
	}

	override def onBlockPlaced(world: World, x: Int, y: Int,
			z: Int, side: Int, hitX: Float, hitY: Float,
			hitZ: Float, meta: Int): Int = {
		ForgeDirection.getOrientation(side).getOpposite.ordinal()
	}

	override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int,
			entity: EntityLivingBase, itemStack: ItemStack): Unit = {
		if (entity.isSneaking) {
			val te: TileEntity = this.getVectorOn(x, y, z, world.getBlockMetadata(x, y, z))
					.getTile(world)
			if (te == null || (!te.isInstanceOf[IInventory] && !te.isInstanceOf[IFluidHandler])) {
				world.setBlockMetadataWithNotify(
					x, y, z,
					ForgeDirection.getOrientation(
						world.getBlockMetadata(x, y, z)
					).getOpposite.ordinal(), 3
				)
			}
		}
		world.getTileEntity(x, y, z).asInstanceOf[TETransmitter].setAttributes(itemStack)
	}

	override def onBlockPreDestroy(
			world: World, x: Int, y: Int, z: Int, meta: Int
			): Unit = {
		val te: TileEntity = world.getTileEntity(x, y, z)
		te match {
			case transmitter: TETransmitter =>
				if (!world.isRemote)
					EnderHandler.removeTile(transmitter.getFrequency(), new BlockPos(transmitter))
			case _ =>
		}
	}

	override def hasTileEntityDrops(metadata: Int): Boolean = true

	override def isOpaqueCube: Boolean = false

	override def getRenderType: Int = -1

	override def renderAsNormalBlock(): Boolean = false

	def setBounds(meta: Int): Unit = {
		val aabb: AxisAlignedBB = this.getBounds(meta)
		this.setBlockBounds(
			aabb.minX.toFloat, aabb.minY.toFloat, aabb.minZ.toFloat,
			aabb.maxX.toFloat, aabb.maxY.toFloat, aabb.maxZ.toFloat
		)
	}

	def getBounds(meta: Int): AxisAlignedBB = {
		BoundsHelper.getBounds(new Vector3(0.25, 0, 0.25), new Vector3(0.5, 0.125, 0.5), meta)
		/*
		//val w: Float = 1F / 2F
		val w2: Float = 1F / 4F
		//val l: Float = 1F / 2F
		val l2: Float = 1F / 4F
		val h: Float = 1F / 8F
		meta match {
			case 0 =>
				// xyz1  0.250x, 0.000y, 0.250z
				// xyz2  0.750x, 0.125y, 0.750z
				// whl_  0.500w, 0.125h, 0.500l
				AxisAlignedBB.getBoundingBox(
					0.5F - w2, 0F - 0, 0.5F - l2,
					0.5F + w2, 0f + h, 0.5F + l2
				)
			case 1 =>
				// xyz1  0.250x, 0.875y, 0.250z
				// xyz2  0.750x, 1.000y, 0.750z
				// whl_  0.500w, 0.125h, 0.500l
				AxisAlignedBB.getBoundingBox(
					0.5F - w2, 1F - h, 0.5F - l2,
					0.5F + w2, 1F + 0, 0.5F + l2
				)
			case 2 =>
				// xyz1  0.250z, 0.250x, 0.000y
				// xyz2  0.750z, 0.750x, 0.125y
				// wlh_  0.500l, 0.500w, 0.125h
				AxisAlignedBB.getBoundingBox(
					0.5F - l2, 0.5F - w2, 0F - 0,
					0.5F + l2, 0.5F + w2, 0F + h
				)
			case 3 =>
				// xyz1  0.250z, 0.250x, 0.875y
				// xyz2  0.750z, 0.750x, 1.000y
				// wlh_  0.500l, 0.500w, 0.125h
				AxisAlignedBB.getBoundingBox(
					0.5F - l2, 0.5F - w2, 1F - h,
					0.5F + l2, 0.5F + w2, 1F + 0
				)
			case 4 =>
				// xyz1  0.000y, 0.250x, 0.250z
				// xyz2  0.125y, 0.750x, 0.250z
				// wlh_  0.125h, 0.500w, 0.500l
				AxisAlignedBB.getBoundingBox(
					0F - 0, 0.5F - w2, 0.5F - l2,
					0F + h, 0.5F + w2, 0.5F + l2
				)
			case 5 =>
				// xyz1  0.875y, 0.250x, 0.250z
				// xyz2  1.000y, 0.750x, 0.250z
				// wlh_  0.125h, 0.500w, 0.500l
				AxisAlignedBB.getBoundingBox(
					1F - h, 0.5F - w2, 0.5F - l2,
					1F + 0, 0.5F + w2, 0.5F + l2
				)
			case _ =>
				AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1)
		}
		*/
	}

	val raytracer: RayTracer = new RayTracer

	override def collisionRayTrace(world: World, x: Int, y: Int, z: Int, headVec: Vec3,
			cursorPosVec: Vec3): MovingObjectPosition = {
		val te: TileEntity = world.getTileEntity(x, y, z)
		if (te == null) return null
		val meta: Int = world.getBlockMetadata(x, y, z)

		val cuboids: util.List[IndexedCuboid6] = new util.LinkedList[IndexedCuboid6]()
		cuboids.add(
			new IndexedCuboid6(0,
				new Cuboid6(this.getBounds(meta)).add(Vector3.fromTileEntity(te))))

		te match {
			case trans: TETransmitter =>
				trans.addCuboids(cuboids, meta)
			case _ =>
		}

		raytracer.rayTraceCuboids(
			new Vector3(headVec), new Vector3(cursorPosVec),
			cuboids, new BlockCoord(x, y, z), this
		)
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	def onDrawHighlight(event: DrawBlockHighlightEvent): Unit = {
		if (event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK &&
				event.player.worldObj.getBlock(
					event.target.blockX, event.target.blockY, event.target.blockZ
				) == this) {
			RayTracer.retraceBlock(event.player.worldObj, event.player, event.target.blockX,
				event.target.blockY, event.target.blockZ)
		}
	}

	override def addCollisionBoxesToList(world: World, x: Int, y: Int, z: Int, aabb: AxisAlignedBB,
			list: util.List[_], entity: Entity): Unit = {
		this.setBounds(world.getBlockMetadata(x, y, z))
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
	}

	override def getCollisionBoundingBoxFromPool(
			world: World, x: Int, y: Int, z: Int): AxisAlignedBB = {
		this.setBounds(world.getBlockMetadata(x, y, z))
		super.getCollisionBoundingBoxFromPool(world, x, y, z)
	}

	override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer,
			side: Int, offsetX: Float, offsetY: Float, offsetZ: Float): Boolean = {
		val te: TileEntity = world.getTileEntity(x, y, z)
		if (!player.isSneaking && side == world.getBlockMetadata(x, y, z)) {
			te match {
				case trans: TETransmitter =>
					LogHelper.info(Href.modname, "Open gui")
					player.openGui(Href.modid, 0, world, x, y, z)
				case _ =>
			}

			return true
		}

		if (world.isRemote) return true

		val mop: MovingObjectPosition = RayTracer.retraceBlock(world, player, x, y, z)

		if (mop == null || te == null || !te.isInstanceOf[TETransmitter]) return false

		mop.subHit match {
			case 1 | 2 | 3 | 4 => // button
				val itemStack: ItemStack = player.getCurrentEquippedItem
				if (itemStack != null) {
					val color: Int = Dyes.getDyeColor(itemStack)
					if (color >= 0)
						te.asInstanceOf[TETransmitter].setButtonColor(
							mop.subHit - 1, color
						)
				}
				true // makes sure that the only action on the button is to change color
			case _ => false
		}
	}

}
