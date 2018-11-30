package org.ashwath.iot.module07;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

/*
 * This class is a resource handler class.. 
 * extends the CoapResource class
 * this helps the server in handling different types of requests
 * from clients
 * 
 * For testing purpose, all the requests are acting on a text file residing in
 * the local machine
 */
public class TempResourceHandler extends CoapResource {
	
	/*
	 * Create a resource handler with the resource name and no visibility
	 */
	public TempResourceHandler()
	{
		super("temp", false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.californium.core.CoapResource#handleGET(org.eclipse.californium.core.server.resources.CoapExchange)
	 * handles the GET requests from the clients
	 * 
	 */
	public void handleGET(CoapExchange ce)
	{
		
		/*
		 * create a file in the specified path, and open file input stream to read the data
		 */
		File file = new File("/home/ashwath/Downloads/connectedDocs/myTemp.txt");
		FileInputStream fis = null;
		
		
		try {
			/*
			 * read the data in bytes 
			 */
			fis = new FileInputStream(file);			
			int bytes = fis.available();
			byte[] data = new byte[bytes];
			int count = fis.read(data);
			
			/*
			 * if there are any bytes of data to send,
			 * respond to the client with the response code as VALID
			 * and with the data
			 */
			if(count>0)
			{
				ce.respond(ResponseCode.VALID, new String(data));
			}
		
			
		} catch(Exception e)
		{
			/*log any types of exception*/
			e.printStackTrace();
		}
		
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.californium.core.CoapResource#handlePOST(org.eclipse.californium.core.server.resources.CoapExchange)
	 * called when a client requests the server to post
	 * 
	 */
	public void handlePOST(CoapExchange ce)
	{
		/*create a file in the specified path. create a file output stream to write the data
		 * 
		 */
		File file = new File("/home/ashwath/Downloads/connectedDocs/myTemp.txt");
		FileOutputStream fos = null;		
		
		try {
			fos = new FileOutputStream(file);
			
			/*
			 * POST: in case of no such file with the name, create a new file
			 */
			if(!file.exists())
				file.createNewFile();
			
			/*
			 * get the bytes to write from the coap exchange variable 
			 * (from the request message of the client)
			 */
			byte[] data = ce.getRequestPayload();
			
			/*
			 * Write on the output stream which writes the data to the text file
			 */
			fos.write(data);			
			fos.flush();
			fos.close();
			
			/*
			 * respond to the client with a code VALID and "success" message
			 */
			ce.respond(ResponseCode.VALID, "Success");			
		
			
		} catch(Exception e)
		{
			/*
			 * log any failure
			 */
			e.printStackTrace();
		}			
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.californium.core.CoapResource#handlePUT(org.eclipse.californium.core.server.resources.CoapExchange)
	 * handles the PUT request from the clients
	 */
	public void handlePUT(CoapExchange ce)
	{
		/*
		 * creates a file in the specified location 
		 * new output stream to write data onto the file
		 */
		File file = new File("/home/ashwath/Downloads/connectedDocs/myTemp.txt");
		FileOutputStream fos = null;		
		
		try {

			fos = new FileOutputStream(file, true);
			
			/*if no such file exists, create a new one*/
			if(!file.exists())
				file.createNewFile();
			
			/*
			 * get the data to be updated from the client message (coap exchange variable)
			 */
			byte[] data = ce.getRequestPayload();
			
			/*
			 * write the data onto the output stream which writes the local text file			
			 */
			fos.write(data);			
			fos.flush();
			fos.close();
			
			/*respond to the client with VALID response code and "success" message*/
			ce.respond(ResponseCode.VALID, "Success");			
		
			
		} catch(Exception e)
		{
			/*
			 * log any failures
			 */
			e.printStackTrace();
		}	
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.californium.core.CoapResource#handleDELETE(org.eclipse.californium.core.server.resources.CoapExchange)
	 * Called when a client requests to DELETE 
	 */
	public void handleDELETE(CoapExchange ce)
	{
		/*
		 * create a file in the specified location
		 */
		File file = new File("/home/ashwath/Downloads/connectedDocs/myTemp.txt");
		
		
		try {			
			/*
			 * create a print writer with the file, close it which
			 * deletes the file
			 */ 			 
			PrintWriter pw = new PrintWriter(file);
			pw.close();			
			
			/*
			 * respond to the client with VALID as code and a message "success"
			 */
			ce.respond(ResponseCode.VALID, "Success");			
		
			
		} catch(Exception e)		
		{
			/*
			 * log any failures
			 */
			e.printStackTrace();
		}	
		
	}



}
