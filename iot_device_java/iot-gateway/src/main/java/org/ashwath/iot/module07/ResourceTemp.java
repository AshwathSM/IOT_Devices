package org.ashwath.iot.module07;

import java.util.logging.Logger;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class ResourceTemp extends CoapResource {
	
	private static final Logger _logger = Logger.getLogger(ResourceTemp.class.getName());
	
	
	public ResourceTemp(String name)
	{
		super(name, true);
	}
	
	
	public ResourceTemp(String name, boolean visible)
	{
		super(name, visible);
	}
	
	
	public void handleGet(CoapExchange ce)
	{
		String responseMsg = "Here's my response to:" + super.getName();
		ce.respond(ResponseCode.VALID, responseMsg);
		
		_logger.info("");
	}

}
