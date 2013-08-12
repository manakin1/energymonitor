package au.com.planetinnovation.marconi.controllers ;

import android.app.Activity;
import android.content.Context ;
import android.content.Intent;
import android.util.Log;

import au.com.planetinnovation.marconi.activities.CurrentUsageActivity;
import au.com.planetinnovation.marconi.models.CurrentUsageModel;
import au.com.planetinnovation.marconi.vos.SimpleObservable;


public class CurrentUsageLoaderController extends LoaderController 
{

	public static final String TAG = "TotalUsageLoaderController" ;
	public static final int MESSAGE_DISPLAY_DETAILS = 1 ;
	public static final int MESSAGE_ANIMATE_OUT = 2 ;
	
	private CurrentUsageModel model ;
	
	
	public CurrentUsageLoaderController( Context context, SimpleObservable model, Activity activity )
	{
		super( context, model, activity ) ;

		setModel( model ) ;
		this.context = context ;
		this.activity = activity ;
	}
	
	
	// ACCESSOR METHODS
	
	
	// PUBLIC METHODS
	
	
	@Override
	public void init( )
	{
		launchHandler.postDelayed( launchIfReadyTask, screenDelay ) ;
	}
	
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
		this.model = ( CurrentUsageModel ) model ;
	}

	@Override
	protected void launch( )
	{
		Intent i = new Intent( context, CurrentUsageActivity.class ) ;
		i.setFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION ) ;
		activity.startActivity( i ) ;
		
		activity.finish( ) ;
	}
	
}
