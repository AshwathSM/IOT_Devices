package org.ashwath.iot.module07;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

/*This class handles the asynchronous response from the coap client*/
public class CoapClientObserverHandler implements CoapHandler {
	
	private static Logger _logger = Logger.getLogger(CoapClientObserverHandler.class.getName());

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.californium.core.CoapHandler#onLoad(org.eclipse.californium.core.CoapResponse)
	 * This is invoked in case of an arrival of a message
	 */
	public void onLoad(CoapResponse response) {
		/*display the response text*/		
		_logger.info("Response received: "+response.getResponseText());
	}

	/*called in case of request time-out or server rejects a request*/
	public void onError() {		
		_logger.log(Level.WARNING, "response not received");
	}

}
