package com.quasma.mynextrip.android.rest;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class Response 
{
    public static int NO_CONNECTION = 600;
    /**
     * The HTTP status code
     */
    public int status = -1;
    
    public String statusMsg = null;
    /**
     * The HTTP headers received in the response
     */
    public  Map<String, List<String>> headers = null;
    
    /**
     * The response body, if any
     */
    public byte[] body = null;
    
    public URL url = null; 

    protected Response(URL url, int status,  String statusMsg, Map<String, List<String>> headers, byte[] body) 
    {
    	this.url = url;
        this.status = status; 
        this.statusMsg = statusMsg;
        this.headers = headers; 
        this.body = body;
    }
}

