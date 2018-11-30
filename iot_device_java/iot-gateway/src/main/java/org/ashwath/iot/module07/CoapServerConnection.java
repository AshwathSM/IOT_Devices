package org.ashwath.iot.module07;

import java.util.logging.Logger;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

/*This class implements the Californium Coap Server
 *  and creates the resource Handler
 * implements the start method to create a server 
 * instance and start the server and also to stop the server*/
public class CoapServerConnection {
	
	private static final Logger _logger = Logger.getLogger(CoapServerConnection.class.getName()); 
	private CoapServer _coapServer;
	
	public CoapServerConnection()
	{
		super();
	}
	
	/*
	 * this function adds the resources to the server if the 
	 * resource specified is not null
	 */
	public void addResource(CoapResource resource)
	{
		if (resource!=null)
			_coapServer.add(resource);
	}

	/*
	 * This method creates an instance of Californium CoapServer, creates a ResourceHandler, 
	 * adds the resourceHandler instance to the server, and starts the server to
	 * handle the requests from the clients
	 */
	public void start()
	{
		
		/*create a new server only when there is no server exists previously*/
		if(_coapServer==null)
		{
			_logger.info("creating coap server instance and 'temp' handler...");
			
			/*create an instance of Californium Coap server*/
			_coapServer = new CoapServer();
			
			/*Create a resource handler instance (handles all post, Get, put, delete requests 
			 * from the clients) and adds the handler to the server instance*/
			TempResourceHandler tempHandler = new TempResourceHandler();
			_coapServer.add(tempHandler);
			
		}
		
		_logger.info("Starting the CoAP server...");
		
		/*start the server*/
		_coapServer.start();
	}
	
	/*Stop the Coap server*/
	public void stop()
	{
		_logger.info("stopping the CoAP server");
		_coapServer.stop();
	}
}
