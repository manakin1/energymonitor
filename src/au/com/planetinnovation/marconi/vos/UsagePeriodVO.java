package au.com.planetinnovation.marconi.vos;

import java.util.Date;

import org.json.JSONArray;

public class UsagePeriodVO extends SimpleObservable
{
	
	private JSONArray data ;
	
	private double priceTotal ;
	private double usageTotal ;
	private double priceAverage ;
	private double usageAverage ;
	
	private Date timestampStart ;
	private Date timestampEnd ;
	
	private String name ;

	
	// ACCESSOR METHODS
	
	
	public JSONArray getData( )
	{
		return data ;
	}
	
	public void setData( JSONArray json )
	{
		data = json ;
	}
	
	public double getUsageTotal( )
	{
		return usageTotal ;
	}
	
	public void setUsageTotal( double usage )
	{
		usageTotal = usage ;
		notifyListeners( this ) ;
	}
	
	public double getPriceTotal( )
	{
		return priceTotal ;
	}
	
	public void setPriceTotal( double price )
	{
		priceTotal = price ;
		notifyListeners( this ) ;
	}
	
	public double getUsageAverage( )
	{
		return usageAverage ;
	}
	
	public void setUsageAverage( double usage )
	{
		usageAverage = usage ;
		notifyListeners( this ) ;
	}
	
	public String getName( )
	{
		return name ;
	}
	
	public void setName( String name )
	{
		this.name = name ;
	}
	
	public Date getTimestampStart( )
	{
		return timestampStart ;
	}
	
	public Date getTimestampEnd( )
	{
		return timestampEnd ;
	}
	
	public void setTimestampStart( Date date )
	{
		timestampStart = date ;
	}
	
	public void setTimestampEnd( Date date )
	{
		timestampEnd = date ;
	}
	
	
}
