package au.com.planetinnovation.marconi.models;

import java.util.ArrayList;

import au.com.planetinnovation.marconi.vos.SimpleObservable;
import au.com.planetinnovation.marconi.vos.UsageCategoryVO;
import au.com.planetinnovation.marconi.vos.UsagePeriodVO;

public class DetailsModel extends SimpleObservable
{
	
	private ArrayList<UsagePeriodVO> data ;
	
	private int color ;
	private int parentColor ;
	private int drawableBackground ;
	private int drawableHighlight ;
	private int drawableShadow ;
	
	private int category ;
	private int featuredItem ;
	
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
	
	public int getColor( )
	{
		return color ;
	}
	
	public void setColor( int color )
	{
		this.color = color ;
	}
	
	public int getParentColor( )
	{
		return parentColor ;
	}
	
	public void setParentColor( int color )
	{
		this.parentColor = color ;
	}
	
	public int getBackground( )
	{
		return drawableBackground ;
	}
	
	public void setBackground( int res )
	{
		this.drawableBackground = res ;
	}
	
	public int getHighlight( )
	{
		return drawableHighlight ;
	}
	
	public void setHighlight( int res )
	{
		this.drawableHighlight = res ;
	}
	
	public int getShadow( )
	{
		return drawableShadow ;
	}
	
	public void setShadow( int res )
	{
		this.drawableShadow = res ;
	}
	
	public int getCategory( )
	{
		return category ;
	}

	public void setCategory( int cat )
	{
		this.category = cat ;
	}
	
	public int getFeaturedItem( )
	{
		return featuredItem ;
	}

	public void setFeaturedItem( int index )
	{
		this.featuredItem = index ;
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
