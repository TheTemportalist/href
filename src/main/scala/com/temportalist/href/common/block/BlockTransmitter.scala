package com.temportalist.href.common.block

import com.temportalist.href.common.Href
import com.temportalist.href.common.lib.{BlockPos, EnderHandler}
import com.temportalist.href.common.tile.TETransmitter
import com.temportalist.origin.wrapper.common.block.BlockWrapperTE
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{MovingObjectPosition, Vec3}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.ForgeDirection

/**
 *
 *
 * @author TheTemportalist
 */
class BlockTransmitter() extends BlockWrapperTE(Material.rock, Href.modid, "Transmitter",
	classOf[TETransmitter]) {

	// todo move to lib class DONE
	def getBlockOn(world: World, x: Int, y: Int, z: Int, meta: Int): Block = {
		val dir: ForgeDirection = ForgeDirection.getOrientation(meta)
		val blockPos: Vec3 = Vec3.createVectorHelper(x, y, z).addVector(
			dir.offsetX, dir.offsetY, dir.offsetZ
		)
		world.getBlock(blockPos.xCoord.toInt, blockPos.yCoord.toInt, blockPos.zCoord.toInt)
	}

	override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int,
			entity: EntityLivingBase, itemStack: ItemStack): Unit = {
		world.getTileEntity(x, y, z).asInstanceOf[TETransmitter].setAttributes(itemStack)
	}

	override def onBlockPlaced(world: World, x: Int, y: Int,
			z: Int, side: Int, hitX: Float, hitY: Float,
			hitZ: Float, meta: Int): Int = {
		// return the opposite side (this's mouth is facing the block it was placed on)
		val newMeta: Int = ForgeDirection.getOrientation(side).getOpposite.ordinal()

		val tile: TileEntity = world.getTileEntity(x, y, z)
		if (tile != null && tile.isInstanceOf[TETransmitter]) {
			val dir: ForgeDirection = ForgeDirection.getOrientation(newMeta)
			tile.asInstanceOf[TETransmitter].setAttachedInv(dir, Vec3.createVectorHelper(
				x, y, z
			).addVector(dir.offsetX, dir.offsetY, dir.offsetZ))
		}

		newMeta
	}

	override def onBlockPreDestroy(
			world: World, x: Int, y: Int, z: Int, meta: Int
			): Unit = {
		val te: TileEntity = world.getTileEntity(x, y, z)
		te match {
			case transmitter: TETransmitter =>
				EnderHandler.removeTile(transmitter.getFrequency(),
					new BlockPos(x, y, z, world.provider.dimensionId))
			case _ =>
		}
	}

	override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int,
			block: Block): Unit = {
		super.onNeighborBlockChange(world, x, y, z, block)
		val meta: Int = world.getBlockMetadata(x, y, z)
		if (!this.getBlockOn(world, x, y, z, meta).getMaterial.isSolid) {
			this.dropBlockAsItem(world, x, y, z, meta, 0)
			world.setBlockToAir(x, y, z)
		}
	}

	override def dropBlockAsItem(world: World, x: Int, y: Int, z: Int,
			itemStack: ItemStack): Unit = {
		world.getTileEntity(x, y, z) match {
			case transmitter: TETransmitter =>
				transmitter.addAttributes(itemStack)
			case _ =>
		}
	}

	override def isOpaqueCube: Boolean = false

	override def getRenderType: Int = -1

	override def renderAsNormalBlock(): Boolean = false

	override def setBlockBoundsBasedOnState(
			world: IBlockAccess, x: Int, y: Int, z: Int): Unit = {
		val meta: Int = world.getBlockMetadata(x, y, z)
		//val w: Float = 1F / 2F
		val w2: Float = 1F / 4F
		//val l: Float = 1F / 2F
		val l2: Float = 1F / 4F
		val h: Float = 1F / 8F
		meta match {
			case 0 =>
				this.setBlockBounds(
					0.5F - w2, 0F, 0.5F - l2,
					0.5F + w2, h, 0.5F + l2
				)
			case 1 =>
				this.setBlockBounds(
					0.5F - w2, 1F - h, 0.5F - l2,
					0.5F + w2, 1F, 0.5F + l2
				)
			case 2 =>
				this.setBlockBounds(
					0.5F - l2, 0.5F - w2, 0F,
					0.5F + l2, 0.5F + w2, h
				)
			case 3 =>
				this.setBlockBounds(
					0.5F - l2, 0.5F - w2, 1F - h,
					0.5F + l2, 0.5F + w2, 1F
				)
			case 4 =>
				this.setBlockBounds(
					0F, 0.5F - w2, 0.5F - l2,
					h, 0.5F + w2, 0.5F + l2
				)
			case 5 =>
				this.setBlockBounds(
					1F - h, 0.5F - w2, 0.5F - l2,
					1F, 0.5F + w2, 0.5F + l2
				)
			case _ =>
		}
	}

	override def collisionRayTrace(world: World, x: Int, y: Int, z: Int,
			headVec: Vec3, cursorPosVec: Vec3): MovingObjectPosition = {

	}

	override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer,
			side: Int, offsetX: Float, offsetY: Float, offsetZ: Float): Boolean = {

	}

}
