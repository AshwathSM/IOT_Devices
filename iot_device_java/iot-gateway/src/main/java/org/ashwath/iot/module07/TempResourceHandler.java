package org.ashwath.iot.module07;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.observe.ObserveRelation;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.eclipse.californium.core.server.resources.ResourceAttributes;
import org.eclipse.californium.core.server.resources.ResourceObserver;

public class TempResourceHandler extends CoapResource {
	
	
	public TempResourceHandler()
	{
		super("temp", false);
	}
	
	
	
	public void handleGET(CoapExchange ce)
	{
		File file = new File("MyTempFIle");
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(file);
			
			int bytes = fis.available();
			byte[] data = new byte[bytes];
			int count = fis.read(data);
			
			if(count>0)
			{
				ce.respond(ResponseCode.VALID, new String(data));
			}
			
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
		ce.respond(ResponseCode.VALID, "GET temp: "+20.0);
	}



}
