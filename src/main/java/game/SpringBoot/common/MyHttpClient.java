package game.SpringBoot.common;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class MyHttpClient 
{

    public static String doGet(String url) 
    {
    	InputStream is = null;
    	BufferedReader br = null;
    	
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        
        GetMethod getMethod = new GetMethod(url);
        
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
       
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
        try 
        {
            int statusCode = httpClient.executeMethod(getMethod);

            if (statusCode != HttpStatus.SC_OK) 
            {
                System.err.println("Method faild: " + getMethod.getStatusLine());
            } 
            else
            {
            	is = getMethod.getResponseBodyAsStream();
              
            	br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
              
                String temp = null;
                while ((temp = br.readLine()) != null) 
                {
                    sbf.append(temp).append("\r\n");
                }

                return sbf.toString();
            }

        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            if (null != br)
            {
                try
                {
                    br.close();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
            if (null != is)
            {
                try 
                {
                   is.close();
                } 
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            getMethod.releaseConnection();
        }
        return "";
    }

    public static String doPost(String url, Map<String, Object> paramMap) 
    {
        InputStream is = null;
        BufferedReader br = null;
        
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        PostMethod postMethod = new PostMethod(url);
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);

        NameValuePair[] nvp = null;
        if (null != paramMap && paramMap.size() > 0)
        {
            nvp = new NameValuePair[paramMap.size()];
            Set<Entry<String, Object>> entrySet = paramMap.entrySet();
            Iterator<Entry<String, Object>> iterator = entrySet.iterator();

            int index = 0;
            while (iterator.hasNext()) 
            {
                Entry<String, Object> mapEntry = iterator.next();
                try
                {
                    nvp[index] = new NameValuePair(mapEntry.getKey(),
                            new String(mapEntry.getValue().toString().getBytes("UTF-8"), "UTF-8"));
                } 
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                index++;
            }
        }
        if (null != nvp && nvp.length > 0) 
        {
            postMethod.setRequestBody(nvp);
        }
        try
        {
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_OK) 
            {
                System.err.println("Method faild: " + postMethod.getStatusLine());
            }
            
            is = postMethod.getResponseBodyAsStream();
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuffer sbf = new StringBuffer();
            String temp = null;
            while ((temp = br.readLine()) != null)
            {
                sbf.append(temp).append("\r\n");
            }

            return sbf.toString();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
            if (null != is) 
            {
                try 
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            postMethod.releaseConnection();
        }
        return "";
    }
    
    public static String getPostData(HttpServletRequest request)
    {
    	InputStream is = null;
		try
		{
			is = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) 
			{
			    sb.append(line);
			}
			return sb.toString();
		}
		catch (IOException e)
		{
			LogUtils.getLogger().info(e.getMessage());
		}
		finally 
		{
			if(is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					LogUtils.getLogger().info(e.getMessage());
				}
			}
		}
		return "";
    }
}