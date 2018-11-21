package org.ashwath.iot.module07;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

public class CoapClientObserverHandler implements CoapHandler {
	
	private static Logger _logger = Logger.getLogger(CoapClientObserverHandler.class.getName());

	public void onLoad(CoapResponse response) {
		
		_logger.info("Response received: "+response.getResponseText());
		

	}

	public void onError() {
		
		_logger.log(Level.WARNING, "response not received");

	}

}
