package au.com.planetinnovation.marconi.vos;

import java.util.ArrayList;
import java.util.Date;


public class UsageCategoryVO extends SimpleObservable
{
	
	private ArrayList<UsagePeriodVO> items ;
	
	private Date timestampStart ;
	private Date timestampEnd ;
	
	private String name ;

	
	
	// ACCESSOR METHODS
	
	
	public ArrayList<UsagePeriodVO> getItems( )
	{
		return items ;
	}
	
	public void setData( ArrayList<UsagePeriodVO> data )
	{
		this.items = data ;
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
