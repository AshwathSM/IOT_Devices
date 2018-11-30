package org.ashwath.iot.module07;

import java.util.logging.Logger;

/*Create a CoAP_Client_Connector instance and attaches the client with a resource
 * and runs the tests (all kinds of requests for testing)*/
public class CoapConnectionClientApp {
	
	private static final Logger _logger = Logger.getLogger(CoapConnectionClientApp.class.getName());
	private static CoapConnectionClientApp _app;
	private CoAP_Client_Connector _coapClient;
	
	public CoapConnectionClientApp()
	{
		super();		
	}

	/*creates the instance of the app and starts the app*/
	public static void main(String[] args) {
		
		_app = new CoapConnectionClientApp();
		
		try {
			_app.start();
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	/*instance of CoAP_Client_Connector is created and the runTests() method is called*/
	public void start()
	{
		_coapClient = new CoAP_Client_Connector();
		_coapClient.runTests("temp");
	}

}
