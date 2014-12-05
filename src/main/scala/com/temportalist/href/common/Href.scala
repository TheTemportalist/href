package com.temportalist.href.common

import com.temportalist.href.common.init.HrefBlocks
import com.temportalist.origin.wrapper.common.{PluginWrapper, ProxyWrapper}
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Mod, SidedProxy}

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
	final val serverProxy = "com.temportalist.href.server.ServerProxy"

	@SidedProxy(
		clientSide = this.clientProxy,
		serverSide = this.serverProxy
	)
	var proxy: ProxyWrapper = null

	@Mod.EventHandler
	def preInit(event: FMLPreInitializationEvent): Unit = {
		super.preInitialize(this.modid, this.modname, event, this.proxy, HrefBlocks)

	}

	@Mod.EventHandler
	def init(event: FMLInitializationEvent): Unit = {
		super.initialize(event)
	}

	@Mod.EventHandler
	def postInit(event: FMLPostInitializationEvent): Unit = {
		super.postInitialize(event)
	}

}
