package org.ashwath.iot.module07;

import java.util.logging.Logger;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

public class CoapServerConnection {
	
	private static final Logger _logger = Logger.getLogger(CoapServerConnection.class.getName()); 
	private CoapServer _coapServer;
	
	public CoapServerConnection()
	{
		super();
	}
	
	public void addResource(CoapResource resource)
	{
		if (resource!=null)
			_coapServer.add(resource);
	}
	
	
//	public CoapServerConnection()
//	{
//		super();
//		_coapServer = new CoapServerConnection();
//		
//		ResourceTemp = resourceTemp = new ResourceTemp("myHouseOnTHeLake/temp");
//		ResourceHumidity resourceHumidity = new ResourceHumidity();
//		
//		addResource(resourceTemp);
//		addResource(resourceHumidity);
//	}
	
//	public CoapServerConnection(String ...resourceNames)
//	{
//		super();
//	}
//	
//	public void addDefaultResource(String name)
//	{
//		
//	}
	
	public void addResources(CoapResource resource)
	{
		if(resource!=null)
		{
				_coapServer.add(resource);
		}
		
	}
	
	public void start()
	{
		if(_coapServer==null)
		{
			_logger.info("creating coap server instance and 'temp' handler...");
			_coapServer = new CoapServer();
			
			//test resource handler implementation
			
			TempResourceHandler tempHandler = new TempResourceHandler();
			_coapServer.add(tempHandler);
			
		}
		
		_logger.info("Starting the CoAP server...");
		_coapServer.start();
	}
	
	public void stop()
	{
		_logger.info("stopping the CoAP server");
		_coapServer.stop();
	}
	


}
