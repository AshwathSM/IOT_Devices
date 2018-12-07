package org.ashwath.iot.module07;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

/**
 * This class implements a CoAP Californium client (which runs in the local machine) and connects to the
 * CoAP Server (which is also running in the same machine for test)
 *
 */
public class CoAP_Client_Connector 
{
	/*Logger for the class to log all the information*/
	private static final Logger _logger = Logger.getLogger(CoAP_Client_Connector.class.getName());
	
	/*
	 * initialize all the class variables to their defaults
	 * protocol: CoAP
	 * IP of the server: loopback- 127.0.0.1 (because it is running in the same machine)
	 * port to connect 5683 (default CoAP port)
	 * 
	 */	
	private String  _protocol = "coap";
	private String  _host = "127.0.0.1";
	private int     _port = 5683;
	private String  _connUrl = null;
	private CoapClient _coapClient;
	private String  _serverAddr;	
	private boolean isInitialized;
	
	
	/*Create a client connector which connects to the sever
	 * running in the local machine and insecure connection (default)*/
	public CoAP_Client_Connector()
	{
		this("10.0.0.228", false);
	}
	
	/*
	 * This constructor takes the hostname (server) and secure connection details and 
	 * creates a CoAP client to connect to the host 	
	 */
	public CoAP_Client_Connector(String host, boolean isSecure)
	{
		super();
		
		/*Select the protocol and ports depending on the security requested */
		if(isSecure)
		{
//			_protocol = ConfigConst.SECURE_COAP_PROTOCOL;
//			_port = ConfigConst.SECURE_COAP_PORT;
		}
		else {
//			_protocol = ConfigConst.DEFAULT_COAP_PROTOCOL;
//			_port = ConfigConst.DEFAULT_COAP_PORT;
		}
		
		/*Create a host only if the hostname is valid (not null and without any extra spaces)*/
		if (host!=null && host.trim().length()>0)
		{
			_host=host;			
		}
		
		else {
			/*Or else connect to default CoAP server*/
//			_host= ConfigConst.DEFAULT_COAP_SERVER;
		}
		
		/*Create a server address using the host name and port informations*/
		_serverAddr = _protocol + "://" + _host + ":" + _port;
//		_connUrl = _serverAddr;
		
		/*Log the server address information*/
		_logger.info("URL for the connection: "+ _serverAddr);
	}
	
	/*This method is to simplify the testing of the coap client
	 * Initializes the coap client, establishes the connection (verifies by pinging the server)
	 * and calls all the request methods 
	 * */
	public void runTests(String resourceName)
	{
		try {
			
			/*set initialized flag to false till initialization is not complete*/
			isInitialized = false;
			
			/*initialize the client with a resource*/
			initClient(resourceName);
			
			/*Log the current server URL*/
			_logger.info("current URL: "+ getCurrentUri());
			
			/*create a test payload*/
			String payload = "Sample payload";
			
			/*ping the server*/
			pingServer();
			
			/*Discover all the resources which the client is associated with*/
			discoverResources();
			
			/*Display the resources*/
			displayResources();
			
			/*send a post request with the payload to post and use non-confirmed connection
			 * (no acknowledgement) */
			sendPostRequest(payload, false);
			
			/*send a post request with the payload to post and use confirmed connection
			 * (with acknowledgement) */
			sendPostRequest(payload, true);
			
			/*send a put (update) request with non-confirmed connection*/
			sendPutRequest(payload, false);
			
			/*send a put (update) request with confirmed connection*/
			sendPutRequest(payload, true);
			
			/*Send a request to get the transaction (data) with non confirmed connection*/
			sendGetRequest();
			
			/*Send a request to get the transaction (data) with a confirmed connection*/
			sendGetRequest(true);
			
			/*Send a delete request to delete the transaction (data)*/
			sendDeleteRequest();
			
		}catch(Exception e)
		{
			/*In case of a failure to issue any of these requests, log a failure*/
			_logger.log(Level.SEVERE, "Failed to issue request to CoAP server", e);
		}
	}
	
	/*
	 * Gets the current URL of the server
	 * if already connected: get the URL of the connected server
	 * else return the default URL to be connected
	 */
	public String getCurrentUri()
	{
		return (_coapClient !=null ? _coapClient.getURI(): _serverAddr);
	}
	
	/*tries to locate all the resources the client in connected to*/
	public void discoverResources()
	{
		_logger.info("issuing discover...");
		
		/*Initialize the client before discovering*/
		initClient();
		
		/*create a set to hold all the discover sets - weblinks (connection URLs)*/
		Set<WebLink> wlSet = _coapClient.discover();
		
		/*log all the connection links*/
		if(wlSet != null)
		{
			for(WebLink wl:wlSet)
				_logger.info("--> WebLink: "+ wl.getURI());
		}
		
	}
	
	/*send a ping, use default timeout*/
    public void pingServer()
    {
    	_logger.info("sending ping...");
    	
    	/*Initialize the client before pinging*/
    	initClient();
    	
    	/*ping, display if successful*/
    	if(_coapClient.ping())
    	{
    		_logger.info("Ping successful!");
    	}
    }
    
    /*Send a get request with non confirmed connection*/
    public void sendGetRequest()
    {
    	/*Initialize the client with a server connection*/
    	initClient();
    	
    	/*call handle get request - to display the response from the server*/
    	handleGetRequest(false);
    }
    
    /*send get request with a resource name (only get a particular resource)*/
    public void sendGetRequest(String resourceName)
    {
    	/*set initialized to false before initializing the client with the resource*/
    	isInitialized = false;
    	
    	/*Initialize the client with the resource "resourcename"*/
    	initClient(resourceName);
    	
    /*call handle get request - to display the response from the server with 
    * non confirmed  connection*/
    	handleGetRequest(false);
    }
    
    /*Send a get request with a non confirmed connection */
    public void sendGetRequest(boolean useNON)
    { 
    	/*Initialize the client*/
    	initClient();
    	
    	/*call handleGet request method to display the data received from the server*/
    	handleGetRequest(useNON);
    }
    
    /*send post request with payload and the confirmed connection */
    public void sendPostRequest(String payload, boolean useCON)
    {
    	initClient();
    	
    	/*call handle post request method which sends the message to post and 
    	 * handles the response from the server*/
    	handlePostRequest(payload, useCON);
    }
    
    /*Send a post request with data to send, resourcename to be attached, and a confirmed connection*/
    public void sendPostRequest(String resourceName, String payload, boolean useCON)
    {
    	/*set initialize to false because resource is not attached to the client connection*/
    	isInitialized = false;
    	
    	/*initialize he client with the server+resource*/
    	initClient(resourceName);
    	
    	/*call handle post request method which sends the message to post and 
    	 * handles the response from the server*/
    	handlePostRequest(payload, useCON);
    }
    
    /*send an update request with confirmed connection and the data to put*/
    public void sendPutRequest(String payload, boolean useCON)
    {
    	/*initialize the client to connect to the server*/
    	initClient();
    	
    	/*call handle function to send the payload and handle the response from the server*/
    	handlePutRequest(payload, useCON);
    }
    
    /*Send update request with confirmed connection and the resource to be updated*/
    public void sendPutRequest(String resourceName, String payload, boolean useCON)
    {
    	/*set initialized flag to false because the client is not attached with the resource*/
    	isInitialized = false;
    	
    	/*call initClient() to connect the client with the server and resource*/
    	initClient(resourceName);
    	
    	/*call handle function to send the payload and handle the response from the server*/
    	handlePutRequest(payload, useCON);
    }
    
    /*Send the delete request to delete*/
    public void sendDeleteRequest()
    {
    	/*initialize the client with a default connection*/
    	initClient();
    	
    	/*call handleDeleteRequest() to send a delete request with non confirmed connection*/
    	handleDeleteRequest(false);
    }
    
    /*Send the delete request to delete particular resource*/
    public void sendDeleteRequest(String resourceName)
    {
    	/*set initialized flag to false because the client is not attached with the resource*/
    	isInitialized = false;
    	
    	/*call initClient() to connect the client with the server and resource*/
    	initClient(resourceName);
    	
    	/*call handleDeleteRequest() with non confirmed connection*/
    	handleDeleteRequest(false);
    }
    
    
    public void registerObserver(boolean enableWait)
    {
    	_logger.info("registering observer...");
    	CoapClientObserverHandler handler = null;
    	
    	if (enableWait)
    	{
    		_coapClient.observeAndWait(handler);
    	}else
    	{
    		_coapClient.observe(handler);
    	}
    }
   
    /*function to display all the resources the client is attached to*/
    public void displayResources()
    {
    	/*initialize the client before listing all the resources*/
    	initClient();
    	
    	_logger.info("getting all remote web links...");
    	
    	/*create a conainer to hold all the weblinks (resource connection urls)*/
    	Set<WebLink> webLinkSet = _coapClient.discover();
    	
    	/*diplay all the content of the container (all the reources)*/
    	for(WebLink wl:webLinkSet)
    	{
    		_logger.info(wl.getURI());
    	}    	
    }
    
    public void Request()
    {
    	sendGetRequest(null);
    }
    
    /**
     * 
     * 'resource' should ONLY be the file path
     * @param resource
     */
    
//    public void sendGetRequest(String resources)
//    {
//    	initClient(resources);
//    	CoapResponse response = _coapClient.get();
//    	byte[] rawPayload = response.getPayload();
//    	
//    	_logger.info(new String(rawPayload));
//    	
//    }
    

    
    //private methods
    /*
     * Initialize the client with a specified resource
     */
    private void initClient(String resourceName)
    {
    	/*return if already intialized*/
    	if(isInitialized)
    	{
    		return;
    	}
    	
    	/*if there is a coap client already, shut it down (don't use the old one)*/
    	if(_coapClient!=null)
    	{
    		_coapClient.shutdown();
    		_coapClient = null;    		
    	}
    	
    	try {    		
    		/*use only valid resourcename*/
    		if(resourceName != null)
    		{
    			_serverAddr += "/"+resourceName;
    		}
    		
    		/*create a new CoapClient*/
    		_coapClient = new CoapClient(_serverAddr);
    		
    		_logger.info("Created client connection to server / resource: "+_serverAddr);
    	}
    	
    	catch(Exception e)
    	{
    		/*in case of failure to create a client, log the failure*/
    		_logger.log(Level.SEVERE,"Failed to connect to broker: "+getCurrentUri(), e);
    	}
    }
    
    /*initialize the client with default resourcename (NULL)*/
    private void initClient()
    {
    	initClient(null);
    }
    
    
    /*
     * this method sends the delete request to the server and logs the response from the server
     */
    private void handleDeleteRequest(boolean useNON) 
    {    	
    	_logger.info("Sending DELETE request...");
    	
    	CoapResponse response = null;
    	
    	/*select the type of connction - confirmed or not depending on the argument*/
    	if(useNON)
    	{
    		_coapClient.useNONs().useEarlyNegotiation(32).get();
    	}
    	
    	/*send the delete request and get he respose from the server*/
    	response = _coapClient.delete();
    	
    	/*display the response from the server, in case of no response,
    	 * log the warning*/
    	if(response!=null)
    	{
    		_logger.info("Response: "+response.isSuccess()+"-"+response.getOptions()+"-"+response.getCode());
    		_logger.info("RESPONSE FROM THE SERVER: " + response.getResponseText());
    	}
    	else {
    		_logger.warning("No response received");
    	}
    }
    
    /*this function sends the get request to the server, 
     * gets the response message and the contents 
     * and displays it on the log file*/
    private void handleGetRequest(boolean useNON)
    {
    	
    	_logger.info("Sending GET request...");
    	
    	CoapResponse response = null;
    	
    	/*use the specified (confirmed or not) connection type*/
    	if(useNON)
    	{
    		_coapClient.useNONs().useEarlyNegotiation(32).get();
    	}
    	
    	/*send a get request and get the message*/
    	response = _coapClient.get();
    	
    	/*
    	 * if the the client successfully gets the message, then display the success message
    	 * with the actual content of the message or else log the warning  
    	 */
    	if(response!=null)
    	{
    		_logger.info("Response: "+response.isSuccess()+"-"+response.getOptions()+"-"+response.getCode());
    		_logger.info("RESPONSE FROM THE SERVER: " + response.getResponseText());
    	}
    	else {
    		_logger.warning("No response received");
    	}    	
    }
    
    /*this function sends the put request to the server for a particular resource, 
     * with the data to be updated, gets the response message 
     * and displays it on the log file*/
    private void handlePutRequest(String payload, boolean useCON)
    {
    	_logger.info("sending PUT....");
    	
    	CoapResponse response = null;
    	
    	/*use the confirmed connection if specified*/
    	if(useCON)
    	{
    		_coapClient.useCONs().useEarlyNegotiation(32).get();
    	}
    	
    	/*send the update request to the resource with the payload 
    	 * (payload type is plain text)*/
    	response = _coapClient.put(payload, MediaTypeRegistry.TEXT_PLAIN);
    	
    	/*gets the response from the server and logs it otherwise logs the failure*/
    	if(response!=null)
    	{
    		_logger.info("Response: "+response.isSuccess()+"-"+response.getOptions()+"-"+response.getCode());
    		_logger.info("RESPONSE FROM SERVER: "+ response.getResponseText());
    	}
    	else {
    		_logger.warning("No response received");
    	}    	
    }
    
    /*this function sends the post request to the server, 
     * with the data to be updated, gets the response message 
     * and displays it on the log file*/
    private void handlePostRequest(String payload, boolean useCON)
    {
    	_logger.info("sending POST");
    	CoapResponse response= null;
    	/*use confirmed connection type if specified*/
    	if(useCON)
    	{
    		_coapClient.useCONs().useEarlyNegotiation(32).get();
    	}
    	
    	/*send the post request with the payload and specifying the type of payload
    	 * and get the response back*/
    	response = _coapClient.post(payload, MediaTypeRegistry.TEXT_PLAIN);
    	
    	/*log the success message and text from the server or else log the failure as 
    	 * a warning*/
    	if(response!=null)
    	{
    		_logger.info("Response:" + response.isSuccess() + "-" + response.getOptions() +"-" + response.getCode());
    		_logger.info("RESPONSE FROM SERVER: "+ response.getResponseText());
    	}else {
    		_logger.warning("No response received");
    	}
    }    
}
