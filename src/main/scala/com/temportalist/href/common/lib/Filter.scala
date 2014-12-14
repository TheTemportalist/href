package com.temportalist.href.common.lib

import java.util.Comparator

import net.minecraft.item.ItemStack

/**
 *
 *
 * @author TheTemportalist
 */
abstract class Filter {

	def isApplicable(itemStack: ItemStack): Boolean

}

object Filter {

	/* Filters */

	val withNBT: Filter = new Filter {
		override def isApplicable(itemStack: ItemStack): Boolean = itemStack.hasTagCompound
	}
	val withoutNBT: Filter = new Filter {
		override def isApplicable(itemStack: ItemStack): Boolean = !itemStack.hasTagCompound
	}

	/* Sorting */

	// Compare to: neg if should go further up in list, zero if same, positive if should go down in list

	val alpha: Comparator[ItemStack] = new Comparator[ItemStack] {
		override def compare(stack1: ItemStack, stack2: ItemStack): Int = {
			stack1.getDisplayName.compareTo(stack2.getDisplayName)
		}
	}
	val quantityTop: Comparator[ItemStack] = new Comparator[ItemStack] {
		override def compare(stack1: ItemStack, stack2: ItemStack): Int = {
			stack1.stackSize - stack2.stackSize
		}
	}
	val quantityBottom: Comparator[ItemStack] = new Comparator[ItemStack] {
		override def compare(stack1: ItemStack, stack2: ItemStack): Int = {
			stack2.stackSize - stack1.stackSize
		}
	}

}
