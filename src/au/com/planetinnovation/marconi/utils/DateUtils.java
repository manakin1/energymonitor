package au.com.planetinnovation.marconi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;
import au.com.planetinnovation.marconi.vos.UsageCategoryVO;
import au.com.planetinnovation.marconi.vos.UsagePeriodVO;

public class DateUtils 
{
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" ) ;
	public static final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" } ;
	public static final String[] monthsShort = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" } ;
	public static final String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" } ;

	
	// PUBLIC METHODS
	
	
	public static Date getDateFromString( int year, int month, int day )
	{
	    String date_str = day + "/" + month + "/" + year ;
	    Date date = null ;

	    try 
	    {
	    	SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" ) ;
	        date = formatter.parse( date_str ) ;
	    } 
	    
	    catch( ParseException e ) 
	    {
	    	e.printStackTrace( ) ;
	    }
		
		return date ;
	}
	
	public static Date parseTimestamp( String datestr )
	{
		Date d = new Date( ) ;
		
		try { d = dateFormat.parse( datestr ) ; }
		catch( Exception e ) { e.printStackTrace( ) ; }
		
		return d ;
	}
	
	public static String zeroFormat( int num )
	{
		String str = "" ;
		
		if( num < 10 ) str = "0" + Integer.toString( num ) ;
		else str = Integer.toString( num ) ;
		
		return str ; 
	}
	
	public static ArrayList<UsageCategoryVO> categorize( int index, ArrayList<UsagePeriodVO> data )
	{
		ArrayList<UsageCategoryVO> arr = new ArrayList<UsageCategoryVO>( ) ;
		
		int items_per_category = 0 ;
		int num_categories = 0 ;
		UsagePeriodVO first_item = data.get( 0 ) ;
		Date first_date = first_item.getTimestampStart( ) ;
		
		//Log.i( "DateUtils", "Starting from date: " + first_date.toString( ) ) ;
		
		switch( index )
		{
			// seasons in year
			case 0 :
			{
				items_per_category = 4 ;
				num_categories = ( int ) Math.ceil( ( double ) data.size( ) / ( double ) items_per_category ) ;
				int year = first_date.getYear( ) + 1900 ;
				ArrayList<UsagePeriodVO> items = new ArrayList<UsagePeriodVO>( ) ;
				
				for( int i = 0 ; i < num_categories ; i++ )
				{
					items = new ArrayList<UsagePeriodVO>( ) ;
					UsageCategoryVO cat = new UsageCategoryVO( ) ;
					String name = Integer.toString( year + i ) ;
					cat.setName( name ) ;
					
					for( int j = 0 ; j < items_per_category ; j++ )
					{
						int item_index = ( i * items_per_category ) + j ;
						items.add( data.get( item_index ) ) ;
					}
					
					cat.setData( items ) ;
					arr.add( cat ) ;
				}
				
				break ;
			}
			
			// months in season
			case 1 :
			{
				items_per_category = 3 ;
				num_categories = ( int ) Math.ceil( ( double ) data.size( ) / ( double ) items_per_category ) ;
				
				int month = first_date.getMonth( ) ;
				ArrayList<UsagePeriodVO> items = new ArrayList<UsagePeriodVO>( ) ;
				
				for( int i = 0 ; i < num_categories ; i++ )
				{
					items = new ArrayList<UsagePeriodVO>( ) ;
					UsageCategoryVO cat = new UsageCategoryVO( ) ;
					String name = getSeasonName( month ) ;
					cat.setName( name ) ;
					
					month += 3 ;
					if( month > 11 ) month = month - 12 ;
					
					for( int j = 0 ; j < items_per_category ; j++ )
					{
						int item_index = ( i * items_per_category ) + j ;
						items.add( data.get( item_index ) ) ;
					}
					
					cat.setData( items ) ;
					arr.add( cat ) ;
				}
				
				break ;
			}
			
			// weeks in month
			case 2 :
			{
				items_per_category = 4 ;
				num_categories = ( int ) Math.ceil( ( double ) data.size( ) / ( double ) items_per_category ) ;
				
				int first_month = first_date.getMonth( ) ;
				int month = 0 ;
				ArrayList<UsagePeriodVO> items = new ArrayList<UsagePeriodVO>( ) ;
				
				for( int i = 0 ; i < num_categories ; i++ )
				{
					month = first_month + i ;
					if( month > 11 ) month = month - 12 ;
					items = new ArrayList<UsagePeriodVO>( ) ;
					UsageCategoryVO cat = new UsageCategoryVO( ) ;
					String name = months[ month ] ;
					cat.setName( name ) ;
					
					for( int j = 0 ; j < items_per_category ; j++ )
					{
						int item_index = ( i * items_per_category ) + j ;
						items.add( data.get( item_index ) ) ;				
					}
					
					cat.setData( items ) ;
					arr.add( cat ) ;
				}
				
				break ;
			}
			
			// days in week
			case 3 :
			{
				items_per_category = 7 ;
				num_categories = ( int ) Math.ceil( ( double ) data.size( ) / ( double ) items_per_category ) ;
				
				Calendar cal = Calendar.getInstance( ) ;
			    cal.set( first_date.getYear( ), first_date.getMonth( ), first_date.getDate( ) ) ;
			    
			    Date date1 = cal.getTime( ) ;
			    cal.add( Calendar.DATE, 6 ) ;
			    Date date2 = cal.getTime( ) ;
				
				ArrayList<UsagePeriodVO> items = new ArrayList<UsagePeriodVO>( ) ;
				
				for( int i = 0 ; i < num_categories ; i++ )
				{
					items = new ArrayList<UsagePeriodVO>( ) ;
					UsageCategoryVO cat = new UsageCategoryVO( ) ;
					String name = "" ;
					name = monthsShort[ date1.getMonth( ) ] + " " + date1.getDate( ) + "-" + date2.getDate( ) ;
					if( date1.getMonth( ) != date2.getMonth( ) ) name = monthsShort[ date1.getMonth( ) ] + " " + date1.getDate( ) + " - " + monthsShort[ date2.getMonth( ) ] + " " + date2.getDate( ) ;
					cat.setName( name ) ;
					
					cal.add( Calendar.DATE, 1 ) ;
					date1 = cal.getTime( ) ;
					cal.add( Calendar.DATE, 6 ) ;
				    date2 = cal.getTime( ) ;
					
					for( int j = 0 ; j < items_per_category ; j++ )
					{
						int item_index = ( i * items_per_category ) + j ;
						items.add( data.get( item_index ) ) ;
					}
					
					cat.setData( items ) ;
					arr.add( cat ) ;
				}
				
				break ;
			}
			
			// hours in day
			case 4  :
			{
				items_per_category = 24 ;
				num_categories = 1 ;
				
				ArrayList<UsagePeriodVO> items = new ArrayList<UsagePeriodVO>( ) ;
				
				for( int i = 0 ; i < num_categories ; i++ )
				{
					items = new ArrayList<UsagePeriodVO>( ) ;
					UsageCategoryVO cat = new UsageCategoryVO( ) ;
					String name = monthsShort[ first_date.getMonth( ) ] + " " + first_date.getDate( ) ;
					cat.setName( name ) ;
					
					for( int j = 0 ; j < items_per_category ; j++ )
					{
						int item_index = ( i * items_per_category ) + j ;
						items.add( data.get( item_index ) ) ;
					}
					
					cat.setData( items ) ;
					arr.add( cat ) ;
				}
				
				break ;
			}
			
			// minutes in hour
			case 5 :
			{
				items_per_category = 60 ;
				num_categories = 1 ;
				
				Calendar cal = Calendar.getInstance( ) ;
			    cal.set( first_date.getYear( ), first_date.getMonth( ), first_date.getDate( ), first_date.getHours( ), first_date.getMinutes( ) ) ;
			    
			    Date date1 = cal.getTime( ) ;
			    cal.add( Calendar.HOUR, 1 ) ;
			    Date date2 = cal.getTime( ) ;
				
				ArrayList<UsagePeriodVO> items = new ArrayList<UsagePeriodVO>( ) ;
				
				for( int i = 0 ; i < num_categories ; i++ )
				{
					items = new ArrayList<UsagePeriodVO>( ) ;
					UsageCategoryVO cat = new UsageCategoryVO( ) ;
					String name = monthsShort[ date1.getMonth( ) ] + " " + date1.getDate( ) + " " + zeroFormat( date1.getHours( ) ) + ":00 - " + zeroFormat( date2.getHours( ) ) + ":00" ;
					cat.setName( name ) ;
					
					for( int j = 0 ; j < items_per_category ; j++ )
					{
						int item_index = ( i * items_per_category ) + j ;
						
						items.add( data.get( item_index ) ) ;			
					}
					
					cat.setData( items ) ;
					arr.add( cat ) ;
				}
				
				break ;
			}
			
		}
		
		return arr ;
	}
	
	public static String getSeasonName( int month )
	{
		String season = "" ;
		
		switch( month )
		{
			case 2 : case 3 : case 4 :
			{
				season = "Autumn" ;
				break ;
			}
			
			case 5 : case 6 : case 7 :
			{
				season = "Winter" ;
				break ;
			}
			
			case 8 : case 9 : case 10 :
			{
				season = "Spring" ;
				break ;
			}
			
			case 11 : case 0 : case 1 :
			{
				season = "Summer" ;
				break ;
			}
		}
		
		return season ;
	}
	
	public static String getDayString( int date )
	{
		String str = "" ;
		
		switch( date )
		{
			case 1 : case 21 : case 31 :
			{
				str = "st" ;
				break ;
			}
			
			case 2 : case 22 :
			{
				str = "nd" ;
				break ;
			}
			
			case 3 : case 23 :
			{
				str = "rd" ;
				break ;
			}
			
			default :
			{
				str = "th" ;
				break ;
			}
		}
		
		return Integer.toString( date ) + str ;
	}

	
}
