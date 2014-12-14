package com.temportalist.href.common

import com.temportalist.href.common.init.HrefBlocks
import com.temportalist.origin.library.common.helpers.RegisterHelper
import com.temportalist.origin.library.common.lib.LogHelper
import com.temportalist.origin.library.common.utility.WorldHelper
import com.temportalist.origin.wrapper.common.{PluginWrapper, ProxyWrapper}
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.{Mod, SidedProxy}
import net.minecraftforge.event.world.WorldEvent

/**
 *
 *
 * @author TheTemportalist
 */
@Mod(modid = Href.modid, name = Href.modname, version = "@PLUGIN_VERSION@",
	guiFactory = Href.clientProxy,
	dependencies = "required-after:Forge@[10.13,);required-after:origin@[3.3,);",
	modLanguage = "scala"
)
object Href extends PluginWrapper {

	final val modid = "href"
	final val modname = "HREF"
	final val clientProxy = "com.temportalist.href.client.ClientProxy"
	final val serverProxy = "com.temportalist.href.common.CommonProxy"

	@SidedProxy(
		clientSide = this.clientProxy, serverSide = this.serverProxy
	)
	var proxy: ProxyWrapper = null

	@Mod.EventHandler
	def preInit(event: FMLPreInitializationEvent): Unit = {
		super.preInitialize(this.modid, this.modname, event, this.proxy, HrefBlocks)

		RegisterHelper.registerHandler(this, null)

	}

	@Mod.EventHandler
	def init(event: FMLInitializationEvent): Unit = {
		super.initialize(event)
	}

	@Mod.EventHandler
	def postInit(event: FMLPostInitializationEvent): Unit = {
		super.postInitialize(event)
	}

	@SubscribeEvent
	def load(event: WorldEvent.Load): Unit = {
		if (WorldHelper.isServer() && event.world.provider.dimensionId == 0) {
			var href_worldsavedata: HrefWorldSaveData =
				event.world.perWorldStorage.loadData(classOf[HrefWorldSaveData], "Href_WSD")
						.asInstanceOf[HrefWorldSaveData]
			LogHelper.info(Href.modname,
				"Load | " + event.world.provider.dimensionId + " | " + href_worldsavedata)
			if (href_worldsavedata == null) {
				href_worldsavedata = new HrefWorldSaveData("Href_WSD")
				event.world.perWorldStorage.setData("Href_WSD", href_worldsavedata)
			}
		}
	}

}
