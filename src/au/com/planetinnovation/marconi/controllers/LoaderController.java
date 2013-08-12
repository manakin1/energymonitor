package au.com.planetinnovation.marconi.controllers ;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context ;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import au.com.planetinnovation.marconi.MainApplication ;
import au.com.planetinnovation.marconi.R ;
import au.com.planetinnovation.marconi.activities.OverviewActivity;
import au.com.planetinnovation.marconi.models.OverviewModel;
import au.com.planetinnovation.marconi.vos.SimpleObservable;
import au.com.planetinnovation.marconi.vos.UsagePeriodVO;

public class LoaderController extends Controller 
{

	public static final String TAG = "LoaderController" ;
	public static final int MESSAGE_DISPLAY_OVERVIEW = 1 ;
	public static final int MESSAGE_ANIMATE_OUT = 2 ;
	
	private int[] blocks = { R.raw.year, R.raw.season, R.raw.month, R.raw.week, R.raw.today } ;
	
	protected Context context ;
	protected Activity activity ;
	private OverviewModel model ;
	
	protected Handler loadHandler = new Handler( ) ;
	protected Handler launchHandler = new Handler( ) ;
	
	protected int screenDelay = 4000 ;
	protected Boolean readyToLaunch = false ;
	
	
	public LoaderController( Context context, SimpleObservable model, Activity activity )
	{
		setModel( model ) ;
		this.context = context ;
		this.activity = activity ;
	}
	
	
	// ACCESSOR METHODS
	
	
	public SimpleObservable getModel( ) 
	{
		return model ;
	}
	
	
	// PUBLIC METHODS
	
	
	public void init( )
	{
		loadHandler.postDelayed( loadTask, 1000 ) ;
		launchHandler.postDelayed( launchIfReadyTask, screenDelay ) ;
	}
	
	@Override
	public boolean handleMessage( int what, Object data )
	{
		Log.i( TAG, "Handling message: " + what ) ;
		switch( what )
		{
			
		}
		
		return false ;
	}
	
	
	// PRIVATE METHODS
	
	
	protected void setModel( SimpleObservable model )
	{
		this.model = ( OverviewModel ) model ;
	}
	
	
	protected void launchIfReady( )
	{
		if( readyToLaunch )
		{
			notifyOutboxHandlers( MESSAGE_ANIMATE_OUT, 0, 0, null ) ;
			launchHandler.postDelayed( launchTask, 800 ) ;	
		}
	}

	protected void loadHistoricalData( )
	{
		for( int i = 0 ; i < blocks.length ; i++ )
		{
			loadJSONFile( blocks[i], i ) ;
		}
	}
	
	protected void launch( )
	{
		Intent i = new Intent( context, OverviewActivity.class ) ;
		i.setFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION ) ;
		activity.startActivity( i ) ;
		
		activity.finish( ) ;
	}
	
	private void loadJSONFile( int resource, int index )
	{
		JSONArray entries ;
		
		try
        {
            InputStream is = context.getResources( ).openRawResource( resource ) ;
            byte [] buffer = new byte[ is.available( ) ] ;
            while( is.read( buffer ) != -1 ) ;
            String jsontext = new String( buffer ) ;
            
            entries = new JSONArray( jsontext ) ;
            markResourceLoaded( entries, index ) ;
        }
		
        catch( Exception e )
        {
            e.printStackTrace( ) ;
        }
	}
	
	private void markResourceLoaded( JSONArray entries, int index )
	{
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
	}
	
	
	// TASKS
	
	
	protected Runnable loadTask = new Runnable( )
	{
		public void run( )
		{
			loadHistoricalData( ) ;
		}
	} ;
	
	protected Runnable launchIfReadyTask = new Runnable( )
	{
		public void run( )
		{
			// minimum time has passed, launch next activity if data has been loaded
			readyToLaunch = true ;
			launchIfReady( ) ;
		}
	} ;
	
	protected Runnable launchTask = new Runnable( )
	{
		public void run( )
		{
			launch( ) ;
		}
	} ;
	
}
