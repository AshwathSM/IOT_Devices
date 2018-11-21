package org.ashwath.iot.module07;

import java.util.logging.Logger;

public class CoapConnectionClientApp {
	
	private static final Logger _logger = Logger.getLogger(CoapConnectionClientApp.class.getName());
	private static CoapConnectionClientApp _app;
	private CoAP_Client_Connector _coapClient;
	
	public CoapConnectionClientApp()
	{
		super();
		
	}

	public static void main(String[] args) {
		
		_app = new CoapConnectionClientApp();
		
		try {
			_app.start();
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public void start()
	{
		_coapClient = new CoAP_Client_Connector();
		_coapClient.runTests("temp");
	}

}
