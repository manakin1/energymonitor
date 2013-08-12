package au.com.planetinnovation.marconi.controllers ;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context ;
import android.content.Intent;
import android.util.Log;

import au.com.planetinnovation.marconi.MainApplication ;
import au.com.planetinnovation.marconi.activities.DetailsActivity;
import au.com.planetinnovation.marconi.models.DetailsModel;
import au.com.planetinnovation.marconi.utils.DataLoader;
import au.com.planetinnovation.marconi.utils.DateUtils;
import au.com.planetinnovation.marconi.vos.SimpleObservable;
import au.com.planetinnovation.marconi.vos.UsageCategoryVO;
import au.com.planetinnovation.marconi.vos.UsagePeriodVO;

public class TotalUsageLoaderController extends LoaderController 
{

	public static final String TAG = "TotalUsageLoaderController" ;
	public static final int MESSAGE_DISPLAY_DETAILS = 1 ;
	public static final int MESSAGE_ANIMATE_OUT = 2 ;
	
	private int blockIndex ;
	
	private DetailsModel model ;
	
	
	public TotalUsageLoaderController( Context context, SimpleObservable model, Activity activity, int index )
	{
		super( context, model, activity ) ;

		this.blockIndex = index ;
		setModel( model ) ;
		this.context = context ;
		this.activity = activity ;
	}
	
	
	// ACCESSOR METHODS
	
	
	// PUBLIC METHODS
	
	
	@Override
	public boolean handleMessage( int what, Object data )
	{
		switch( what )
		{	
		}
		
		return false ;
	}
	
	
	// PRIVATE METHODS
	
	
	@Override
	protected void setModel( SimpleObservable model )
	{
		this.model = ( DetailsModel ) model ;
	}
	
	@Override
	protected void launchIfReady( )
	{
		if( readyToLaunch && model.getData( ).size( ) > 0 )
		{
			notifyOutboxHandlers( MESSAGE_ANIMATE_OUT, 0, 0, null ) ;
			launchHandler.postDelayed( launchTask, 1000 ) ;	
		}
	}

	@Override
	protected void loadHistoricalData( )
	{
		ArrayList<UsagePeriodVO> arr = new ArrayList<UsagePeriodVO>( ) ;
		DataLoader loader = new DataLoader( ) ;	
		MainApplication app = ( MainApplication ) context.getApplicationContext( ) ;
		
		model.setBackground( app.getBackground( blockIndex ) ) ;
		model.setShadow( app.getShadow( blockIndex ) ) ;
		model.setHighlight( app.getHighlight( blockIndex ) ) ;
		model.setParentColor( app.getColor( blockIndex ) ) ;
		
		int featured = 0 ;
		Date featured_date = new Date( ) ;
	
		ArrayList<JSONObject> data = new ArrayList<JSONObject>( ) ;
		ArrayList<UsageCategoryVO> categories = new ArrayList<UsageCategoryVO>( ) ;
		
		switch( blockIndex )
		{
			case 0 : default :
			{
				featured = 5 ;
				featured_date = new Date( 2009, 2, 12 ) ;
				data = loader.getSeasons( 2009, 2, 12, 1 ) ;		
				break ;
			}
			
			case 1 :
			{
				featured = 5 ;
				featured_date = new Date( 2010, 5, 12 ) ;
				data = loader.getMonths( 2010, 5, 12, 1 ) ;
				break ;
			}
			
			case 2 :
			{
				featured = 5 ;
				featured_date = new Date( 2011, 0, 1, 12, 0 ) ;
				data = loader.getWeeks( 2011, 0, 1, 12, 1 ) ;
				break ;
			}
			 
			case 3 :
			{
				Date date = new Date( ) ; 
				featured = 14 ;
				featured_date = new Date( date.getYear( ), date.getMonth( ), date.getDate( ) - 1 ) ;
				data = loader.getDays( date.getYear( ), date.getMonth( ), date.getDate( ) - 1, 21, -1 ) ;
				break ;
			}
			
			case 4 :
			{
				Date date = new Date( ) ; 
				featured = 12 ;
				featured_date = new Date( date.getYear( ), date.getMonth( ), date.getDate( ) + 1, 0, 0 ) ;
				data = loader.getHours( date.getYear( ), date.getMonth( ), date.getDate( ) + 1, 0, 24, -1 ) ;
				break ;
			}
		}
		
		arr = loader.loadHistoricalData( blockIndex, data ) ;
		categories = DateUtils.categorize( blockIndex, arr ) ;
				
		model.setCategory( blockIndex ) ;
		model.setData( arr ) ;
		model.setCategories( categories ) ;
		model.setFeaturedItem( featured ) ;
		
		app.setCategoryTime( blockIndex, featured_date ) ;
		app.setCurrentDetailsModel( model ) ;
		
		app.setDetailsModel( blockIndex, model ) ;
		launchIfReady( ) ;
	}
	
	@Override
	protected void launch( )
	{
		Intent i = new Intent( context, DetailsActivity.class ) ;
		i.setFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION ) ;
		activity.startActivity( i ) ;
		
		activity.finish( ) ;
	}
	
	private void markResourceLoaded( JSONArray entries, int index )
	{
		/*
		float power = 0 ;
		float average = 0 ;
		
		for( int i = 0 ; i < entries.length( ) ; i++ )
		{
			try
			{
				JSONObject obj = entries.getJSONObject( i ) ;
				power += Float.parseFloat( obj.getString( "power" ) ) * 60 ;
			}
			
			catch( Exception e )
			{
				e.printStackTrace( ) ;
			}
		}
		
		average = power / entries.length( ) ;
		
		model.setTotalUsage( index, power ) ;
		model.setAverageUsage( index, average ) ;
		
		if( model.getTotalUsage( ).size( ) == blocks.length )
		{
			MainApplication app = ( MainApplication ) context.getApplicationContext( ) ;
			app.setOverviewModel( model ) ;
			
			launchIfReady( ) ;
		}
		*/
	}
	
	
}
