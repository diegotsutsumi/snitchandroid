package com.alieeen.snitch.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alieeen.snitch.SnitchApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alinekborges on 15/04/15.
 */
public class SnitchHttpClient {


    public static final String SNITCH_LOGIN_FILE = "SnitchLoginFile";
    public static final String SNITCH_REGID_FILE = "RegistrationIdFile";
    public static final String SNITCH_PREFERENCES_FILE = "PreferencesFile";
    //public static final String SNITCH_URL = "https://179.217.78.29:8082/android";
    public static final String SNITCH_USER_AGENT = "Snitch-Android";



    //////////////////////////////////////////////////////////////////////////////////////////////////////
    // Essa � a fun��o correta mas como o Snitch est� com problemas de certificado � utilizada a de baixo
    //////////////////////////////////////////////////////////////////////////////////////////////////////
	/*private DefaultHttpClient getClient()
	{
		DefaultHttpClient ret = null;

		//SETS UP PARAMETERS
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);

		//REGISTERS SCHEMES FOR BOTH HTTP AND HTTPS
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
		sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		registry.register(new Scheme("https", sslSocketFactory, 443));

		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
		ret = new DefaultHttpClient(manager, params);
		return ret;
	}*/

    public HttpClient getNewHttpClient()
    {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SnitchSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public boolean doLogin(Context context, String username, String password, String mobilenumber)
    {
        String responseBody = "";
        try
        {
            HttpClient httpclient = getNewHttpClient();
            //HttpPost httppost = new HttpPost("https://christianobelli.no-ip.org:8082/android/doLogin");
            String url = "https://snitch.magnetron.com.br:60001";
            //String url = getPreferences(context, "url");
            if (url.isEmpty()) return false;

            HttpPost httppost = new HttpPost(url + "/android/doLogin");
            httppost.setHeader("User-Agent", SNITCH_USER_AGENT);

            SnitchApplication application = (SnitchApplication)context.getApplicationContext();
            CookieStore store = application.getCookieStore();

            if (store == null)
            {
                store = ((DefaultHttpClient)httpclient).getCookieStore();
                application.setCookieStore(store);
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("mobilenumber", "55" + mobilenumber.substring(1)));
            //TODO
            //nameValuePairs.add(new BasicNameValuePair("googleid", "APA91bFgfDHVOupYZfpPoRnCUU1oqy4t_hW36575FoyLgqAi8LcI_AiKMRcg8g9Nd7oY84fG_MSgH3kI_1qbpH_qYP5HYtzdaIhC32PNuXvABoCUbSW2pU8UPaobbvuiu1rh8dm_4AZ4klfqMWHPO2aLnAb5ywssXA"));
            nameValuePairs.add(new BasicNameValuePair("googleid", "APA91bGkoK04K_cBoJq4hXsjWmEuel9YHVrZ4ubI0FCWLQX4uPza_BX0GvIXJIuNM7cFgusSZLQdojdz5ARwCAIzLQlJAE3VpU1WIxC_ZBYGd5hyYRGj7OSS-jpAJ7xr4WOl4qu-Bu4-qByEiwVeP2gBy0HnnCdjgA"));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpContext ctx = application.getHttpContext();
            if (ctx == null)
            {
                ctx = new BasicHttpContext();
                application.setHttpContext(ctx);
            }
            ctx.setAttribute(ClientContext.COOKIE_STORE, store);

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost, ctx);
            responseBody = EntityUtils.toString(response.getEntity());

            // Save session on cookies
            List<Cookie> cookies = store.getCookies();
            if (cookies != null)
            {
                for (Cookie c : cookies)
                {
                    store.addCookie(c);
                }
            }


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (responseBody.equals("\n\n\nAndroid-Snitch Interface: User Logged in"))
            return true;
        else return false;
    }

    public void doLogout (Context context)
    {
        SnitchApplication application = (SnitchApplication)context.getApplicationContext();
        application.setCookieStore(null);
        application.setHttpContext(null);
        setLogout(context);
    }

    public boolean checkLogin(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(SNITCH_LOGIN_FILE, Context.MODE_PRIVATE);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        return hasLoggedIn;
    }

    public void saveLogin (Context context, String username, String mobileNumber, String password)
    {
        // Save Login Data
        SharedPreferences settings = context.getSharedPreferences(SNITCH_LOGIN_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean("hasLoggedIn", true);		// indica que j� logou
        editor.putString("username", username);
        editor.putString("mobilenumber", mobileNumber);
        editor.putString("password", password);

        // Commit the edits!
        editor.commit();
    }

    public void setLogout (Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(SNITCH_LOGIN_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean("hasLoggedIn", false);

        editor.commit();
    }

    public String getUsername (Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(SNITCH_LOGIN_FILE, Context.MODE_PRIVATE);
        String username = settings.getString("username", "");
        return username;
    }

    public String getPassword (Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(SNITCH_LOGIN_FILE, Context.MODE_PRIVATE);
        String password = settings.getString("password", "");
        return password;
    }

    public String getMobileNumber (Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(SNITCH_LOGIN_FILE, Context.MODE_PRIVATE);
        String mobile = settings.getString("mobilenumber", "");
        return mobile;
    }

    public String getRegistrationId (Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(SNITCH_REGID_FILE, Context.MODE_PRIVATE);
        String registrationId = prefs.getString("registration_id", "");
        return registrationId;
    }

    public SharedPreferences getRegIdFile(Context context)
    {
        return context.getSharedPreferences(SNITCH_REGID_FILE, Context.MODE_PRIVATE);
    }

    public Bitmap downloadBitmap(Context context, String eventId)
    {
        try
        {
            int i = Integer.parseInt(eventId.replaceAll("[\\D]", ""));
            eventId = String.valueOf(i);
            //String url = SNITCH_URL + "/getImage/" + eventId;

            String url = getPreferences(context, "url");
            if (url.isEmpty()) return null;

            url += "/android/getImage/" + eventId;
            url = url.replaceAll("[^\\p{Print}]","");

            SnitchApplication application = (SnitchApplication)context.getApplicationContext();

            CookieStore store = application.getCookieStore();
            HttpClient httpclient = getNewHttpClient();
            HttpContext httpcontext = application.getHttpContext();

            if (store == null || httpcontext == null)
            {
                if (doLogin(context, getUsername(context), getPassword(context), getMobileNumber(context)))
                    return null;
                else
                {
                    httpcontext = application.getHttpContext();
                    store = application.getCookieStore();
                }
            }

            httpcontext.setAttribute(ClientContext.COOKIE_STORE, store);

            HttpGet get = new HttpGet(url);
            get.setHeader("User-Agent", SNITCH_USER_AGENT);

            HttpResponse httpResponse = httpclient.execute(get, httpcontext);
            HttpEntity entity = httpResponse.getEntity();
            InputStream inputStream = entity.getContent();

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            if (bitmap == null)
            {
                String s;
                get = new HttpGet(url);
                get.setHeader("User-Agent", SNITCH_USER_AGENT);

                httpResponse = httpclient.execute(get, httpcontext);
                // receive response as inputStream
                entity = httpResponse.getEntity();
                s = EntityUtils.toString(entity);

                if (s.contains("not logged in"))	// N�o est� logado
                {
                    if (doLogin(context, getUsername(context), getPassword(context), getMobileNumber(context)))	// Try Login
                        return downloadBitmap(context, eventId);
                    else
                        return null;
                }
            }

            return bitmap;

        } catch (Exception e) {
            //e.printStackTrace();

            ////// Esse tratamento � s� para quando expirar a sess�o
            if (doLogin(context, getUsername(context), getPassword(context), getMobileNumber(context)))	// Try Login
                return downloadBitmap(context, eventId);
            else
                return null;
        }
    }

    public void savePreferences(Context context, String preference, String value)
    {
        // Save Login Data
        SharedPreferences settings = context.getSharedPreferences(SNITCH_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(preference, value);

        editor.commit();
    }

    public String getPreferences(Context context, String preference)
    {
        SharedPreferences settings = context.getSharedPreferences(SNITCH_PREFERENCES_FILE, Context.MODE_PRIVATE);

        if (preference == "url")
        {
            String username = settings.getString("url", "");
            return username;
        }
        else return "";
    }

	/*public void sendRegistrationIdToBackend(Context context)
	{
		try
		{
			HttpClient httpclient = getNewHttpClient();
		    HttpPost httppost = new HttpPost("https://christianobelli.no-ip.org:8082/android/updateRegistrationId");


			SnitchApplication application = (SnitchApplication)context.getApplicationContext();
			CookieStore store = application.getCookieStore();

			if (store == null)
			{
			   	store = ((DefaultHttpClient)httpclient).getCookieStore();
			   	application.setCookieStore(store);
			}

	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	        nameValuePairs.add(new BasicNameValuePair("username", getUsername(context)));
	        nameValuePairs.add(new BasicNameValuePair("password", getPassword(context)));
			nameValuePairs.add(new BasicNameValuePair("regid", getRegistrationId(context)));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        HttpContext ctx = application.getHttpContext();
	        if (ctx == null)
	        {
	        	ctx = new BasicHttpContext();
	        	application.setHttpContext(ctx);
	        }
			ctx.setAttribute(ClientContext.COOKIE_STORE, store);

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost, ctx);

	        // Save session on cookies
			List<Cookie> cookies = store.getCookies();
			if (cookies != null)
			{
				for (Cookie c : cookies)
				{
					store.addCookie(c);
				}
			}
		}
		catch (Exception e) {

		}

	}*/
}
