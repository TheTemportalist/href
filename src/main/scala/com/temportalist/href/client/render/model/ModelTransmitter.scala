package com.temportalist.href.client.render.model

import com.temportalist.origin.library.client.render.model.ModelHelper
import com.temportalist.origin.wrapper.client.render.model.ModelWrapper

/**
 *
 *
 * @author TheTemportalist
 */
class ModelTransmitter() extends ModelWrapper(32, 32) {

	this.addModel(ModelHelper.createModel(this, this,
		0.0F, 16.0F, 0.0F,
		-4.0F, 0.0F, -4.0F,
		0.0F, 0.0F, 0.0F,
		8, 2, 8,
		0, 0
	))

}
