package au.com.planetinnovation.marconi.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import au.com.planetinnovation.marconi.vos.UsagePeriodVO;


public class DataLoader 
{
	
	private String apiURL = "" ;
	
			
	public DataLoader( ) { }
	
	
	// PUBLIC METHODS
	
	
	public ArrayList<UsagePeriodVO> loadHistoricalData( int index, ArrayList<JSONObject> data )
	{
		ArrayList<UsagePeriodVO> arr = new ArrayList<UsagePeriodVO>( ) ;	
				
		for( int i = 0 ; i < data.size( ) ; i++ )
		{
			UsagePeriodVO item = new UsagePeriodVO( ) ;
			JSONObject obj = data.get( i ) ;
			double power = 0 ;
			String name = "" ;
			Date d1 = new Date( ) ;
			Date d2 = new Date( ) ;
					
			try 
			{ 
				power = Double.parseDouble( obj.getString( "power" ) ) ; 
				name = obj.getString( "name" ) ;
				
				d1 = DateUtils.parseTimestamp( obj.getString( "startTime" ) ) ;
				d2 = DateUtils.parseTimestamp( obj.getString( "endTime" ) ) ;
			}
			
			catch( Exception e ) { e.printStackTrace( ) ; }
					
			item.setUsageTotal( power ) ;
			item.setName( name ) ;
			item.setTimestampStart( d1 ) ;
			item.setTimestampEnd( d2 ) ;
					
			arr.add( item ) ;
		}
		
		return arr ;
	}
	
	
	public JSONObject loadUsageBetween( Date start, Date end )
	{
		 JSONObject obj = new JSONObject( ) ;
		 
		 double power = 70 + ( Math.random( ) * 30 ) ;
		 
		 try
		 {
			 obj.put( "power", Double.toString( power ) ) ;
		 }
		 
		 catch( Exception e ) { e.printStackTrace( ) ; }
		
		/*
		String url = apiURL + "/" ;
		
		InputStream is = null ;	
		String result = "" ;	
		JSONObject obj = null ;
		
		// http post
		
		try
		{
			HttpClient httpclient = new DefaultHttpClient( ) ;
			HttpPost httppost = new HttpPost( url ) ;
			HttpResponse response = httpclient.execute( httppost ) ;
			HttpEntity entity = response.getEntity( ) ;
			is = entity.getContent( ) ;		
		}
		
		catch( Exception e )
		{
			Log.e( "DataLoader", "Error in http connection: " + e.toString( ) ) ;
		}
		
		// convert response to string
		
		try
		{
			BufferedReader reader = new BufferedReader( new InputStreamReader( is,"iso-8859-1" ), 8 ) ;
			StringBuilder sb = new StringBuilder( ) ;
		
			String line = null ;
		
			while( ( line = reader.readLine( ) ) != null ) 
			{
				sb.append( line + "\n" ) ;
			}
		
			is.close( ) ;
			result = sb.toString( ) ;
		}
		
		catch( Exception e )
		{
			Log.e( "DataLoader", "Error converting result: " + e.toString( ) ) ;
		}
		
		// parse the string to a JSON object
		
		try
		{
			obj = new JSONObject( result ) ;
		}
		
		catch( JSONException e )
		{
			Log.e( "DataLoader", "Error parsing data: " + e.toString( ) ) ;
		}
		
		*/
		
		return obj ;
	}
	
	public ArrayList<JSONObject> getSeasons( int start_year, int start_month, int count, int direction )
	{
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>( ) ;
		
		// set month to beginning of season
		
		if( start_month > 2 && start_month < 5 ) start_month = 2 ;
		else if( start_month > 5 && start_month < 8 ) start_month = 5 ;
		else if( start_month > 8 && start_month < 11 ) start_month = 8 ;
		else if( start_month > 11 ) start_month = 11 ; 
		else if( start_month < 2 ) { start_month = 11 ; start_year-- ; }
		
		Calendar cal = Calendar.getInstance( ) ;
	    cal.set( start_year, start_month, 1 ) ;
	    
	    Date date1 ;
	    Date date2 ;
	    
	    for( int i = 0 ; i < count ; i++ )
	    {
	    	date1 = cal.getTime( ) ;
	    	if( direction >= 0 ) cal.add( Calendar.MONTH, 3 ) ;
	    	else cal.add( Calendar.MONTH, -3 ) ;
	    	date2 = cal.getTime( ) ;
	    	String name = DateUtils.getSeasonName( date1.getMonth( ) ) ;
	    	
	    	JSONObject obj = loadUsageBetween( date1, date2 ) ;
	    	
	    	try 
	    	{ 
	    		obj.put( "startTime", DateUtils.dateFormat.format( date1 ) ) ;
	    		obj.put( "endTime", DateUtils.dateFormat.format( date2 ) ) ;
	    		obj.put( "name", name ) ;
	    	}
	    	
	    	catch( Exception e ) { e.printStackTrace( ) ; }
	    
	    	arr.add( obj ) ;
	    }

	    
	    if( direction < 0 ) Collections.reverse( arr ) ;

		return arr ;
	}
	
	public ArrayList<JSONObject> getMonths( int start_year, int start_month, int count, int direction )
	{
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>( ) ;
		
		Calendar cal = Calendar.getInstance( ) ;
	    cal.set( start_year, start_month, 1 ) ;
	    
	    Date date1 ;
	    Date date2 ;
	    
	    for( int i = 0 ; i < count ; i++ )
	    {
	    	date1 = cal.getTime( ) ;
	    	if( direction >= 0 ) cal.add( Calendar.MONTH, 1 ) ;
	    	else cal.add( Calendar.MONTH, -1 ) ;
	    	date2 = cal.getTime( ) ;
	    	String name = DateUtils.months[ date1.getMonth( ) ] ;
	    	
	    	JSONObject obj = loadUsageBetween( date1, date2 ) ;
	    	
	    	try 
	    	{ 
	    		obj.put( "startTime", DateUtils.dateFormat.format( date1 ) ) ;
	    		obj.put( "endTime", DateUtils.dateFormat.format( date2 ) ) ;
	    		obj.put( "name", name ) ;
	    	}
	    	
	    	catch( Exception e ) { e.printStackTrace( ) ; }
	    	
	    	arr.add( obj ) ;
	    }
	    
	    if( direction < 0 ) Collections.reverse( arr ) ;
	    
		return arr ;
	}
	

	public ArrayList<JSONObject> getWeeks( int start_year, int start_month, int start_date, int count, int direction )
	{
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>( ) ;
		
		Calendar cal = Calendar.getInstance( ) ;
	    cal.set( start_year, start_month, start_date ) ;
	    
	    Date date1 ;
	    Date date2 ;
	    
	    for( int i = 0 ; i < count ; i++ )
	    {
	    	date1 = cal.getTime( ) ;
	    	if( direction >= 0 ) cal.add( Calendar.DATE, 7 ) ;
	    	else cal.add( Calendar.DATE, -7 ) ;
	    	date2 = cal.getTime( ) ;
	    	String name = DateUtils.getDayString( date1.getDate( ) ) + " - " + DateUtils.getDayString( date2.getDate( ) - 1 ) ;
	    	
	    	JSONObject obj = loadUsageBetween( date1, date2 ) ;
	    	
	    	try 
	    	{ 
	    		obj.put( "startTime", DateUtils.dateFormat.format( date1 ) ) ;
	    		obj.put( "endTime", DateUtils.dateFormat.format( date2 ) ) ;
	    		obj.put( "name", name ) ;
	    	}
	    	
	    	catch( Exception e ) { e.printStackTrace( ) ; }
	    	
	    	arr.add( obj ) ;
	    }
	    
	    if( direction < 0 ) Collections.reverse( arr ) ;
	    
		return arr ;
	}
	
	public ArrayList<JSONObject> getDays( int start_year, int start_month, int start_date, int count, int direction )
	{
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>( ) ;
		
		Calendar cal = Calendar.getInstance( ) ;
	    cal.set( start_year, start_month, start_date ) ;
	    
	    Date date1 ;
	    Date date2 ;
	    
	    for( int i = 0 ; i < count ; i++ )
	    {
	    	date1 = cal.getTime( ) ;
	    	if( direction >= 0 ) cal.add( Calendar.DATE, 1 ) ;
	    	
	    	else cal.add( Calendar.DATE, -1 ) ;
	    	date2 = cal.getTime( ) ;
	    	String name = DateUtils.months[ date1.getMonth( ) ] + " " + date1.getDate( ) ;
	    	
	    	JSONObject obj = loadUsageBetween( date1, date2 ) ;
	    	
	    	try 
	    	{ 
	    		obj.put( "startTime", DateUtils.dateFormat.format( date1 ) ) ;
	    		obj.put( "endTime", DateUtils.dateFormat.format( date2 ) ) ;
	    		obj.put( "name", name ) ;
	    	}
	    	
	    	catch( Exception e ) { e.printStackTrace( ) ; }
	    	
	    	arr.add( obj ) ;
	    }
	    
	    if( direction < 0 ) Collections.reverse( arr ) ;
	    
		return arr ;
	}
	
	public ArrayList<JSONObject> getHours( int start_year, int start_month, int start_date, int start_hour, int count, int direction )
	{
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>( ) ;
		
		Calendar cal = Calendar.getInstance( ) ;
	    cal.set( start_year, start_month, start_date, start_hour, 0 ) ;
	    
	    Date date1 ;
	    Date date2 ;
	    
	    for( int i = 0 ; i < count ; i++ )
	    {
	    	date1 = cal.getTime( ) ;
	    	if( direction >= 0 ) cal.add( Calendar.HOUR, 1 ) ;
	    	
	    	else cal.add( Calendar.HOUR, -1 ) ;
	    	date2 = cal.getTime( ) ;
	    	
	    	String name = "" ;
	    	
	    	if( direction >= 0 ) name = DateUtils.zeroFormat( date1.getHours( ) ) + ":00 - " + DateUtils.zeroFormat( date2.getHours( ) ) + ":00" ;
	    	else name = DateUtils.zeroFormat( date2.getHours( ) ) + ":00 - " + DateUtils.zeroFormat( date1.getHours( ) ) + ":00" ;
	    		
	    	JSONObject obj = loadUsageBetween( date1, date2 ) ;
	    	
	    	try 
	    	{ 
	    		obj.put( "startTime", DateUtils.dateFormat.format( date1 ) ) ;
	    		obj.put( "endTime", DateUtils.dateFormat.format( date2 ) ) ;
	    		obj.put( "name", name ) ;
	    	}
	    	
	    	catch( Exception e ) { e.printStackTrace( ) ; }
	    	
	    	arr.add( obj ) ;
	    }
	    
	    if( direction < 0 ) Collections.reverse( arr ) ;
	    
		return arr ;
	}
	
	public ArrayList<JSONObject> getMinutes( int start_year, int start_month, int start_date, int start_hour, int start_minute, int count, int direction )
	{
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>( ) ;
		
		Calendar cal = Calendar.getInstance( ) ;
	    cal.set( start_year, start_month, start_date, start_hour, start_minute ) ;
	    
	    Date date1 ;
	    Date date2 ;
	    
	    for( int i = 0 ; i < count ; i++ )
	    {
	    	date1 = cal.getTime( ) ;
	    	if( direction >= 0 ) cal.add( Calendar.MINUTE, 1 ) ;
	    	
	    	else cal.add( Calendar.MINUTE, -1 ) ;
	    	date2 = cal.getTime( ) ;
	    	String name = DateUtils.zeroFormat( date1.getHours( ) ) + ":" + DateUtils.zeroFormat( date1.getMinutes( ) ) + " - " + 
	    				  DateUtils.zeroFormat( date2.getHours( ) ) + ":" + DateUtils.zeroFormat( date2.getMinutes( ) ) ;
	    	
	    	JSONObject obj = loadUsageBetween( date1, date2 ) ;
	    	
	    	try 
	    	{ 
	    		obj.put( "startTime", DateUtils.dateFormat.format( date1 ) ) ;
	    		obj.put( "endTime", DateUtils.dateFormat.format( date2 ) ) ;
	    		obj.put( "name", name ) ;
	    	}
	    	
	    	catch( Exception e ) { e.printStackTrace( ) ; }
	    	
	    	arr.add( obj ) ;
	    }
	    
	    if( direction < 0 ) Collections.reverse( arr ) ;
	    
		return arr ;
	}
	
	
	// PRIVATE METHODS
		
}
