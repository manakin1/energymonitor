package au.com.planetinnovation.marconi.models;

import java.util.ArrayList;

import au.com.planetinnovation.marconi.vos.SimpleObservable;
import au.com.planetinnovation.marconi.vos.UsageCategoryVO;
import au.com.planetinnovation.marconi.vos.UsagePeriodVO;


public class CurrentUsageModel extends SimpleObservable
{
	
	private ArrayList<UsagePeriodVO> data ;
	
	private int category ;
	
	private ArrayList<UsageCategoryVO> categories = new ArrayList<UsageCategoryVO>( ) ;
	
	
	// ACCESSOR METHODS
	
	
	public ArrayList<UsagePeriodVO> getData( )
	{
		return data ;
	}
	
	public void setData( ArrayList<UsagePeriodVO> arr )
	{
		data = arr ;
		notifyListeners( this ) ;
	}
	
	public int getCategory( )
	{
		return category ;
	}

	public void setCategory( int cat )
	{
		this.category = cat ;
	}
	
	public ArrayList<UsageCategoryVO> getCategories( )
	{
		return categories ;
	}
	
	public void setCategories( ArrayList<UsageCategoryVO> data )
	{
		this.categories = data ;
	}
	
	public int getNumItemsPerCategory( )
	{
		UsageCategoryVO cat = this.categories.get( 0 ) ;
		return cat.getItems( ).size( ) ;
	}
	
	public int getNumCategories( )
	{
		return this.categories.size( ) ;
	}
	
}
