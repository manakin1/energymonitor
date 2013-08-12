package au.com.planetinnovation.marconi.activities ;

import android.app.Activity ;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import au.com.planetinnovation.marconi.R ;
import au.com.planetinnovation.marconi.controllers.CurrentUsageLoaderController;
import au.com.planetinnovation.marconi.controllers.TotalUsageLoaderController;
import au.com.planetinnovation.marconi.models.CurrentUsageModel;


public class CurrentUsageLoaderActivity extends Activity implements Handler.Callback
{

	private static final String TAG = CurrentUsageLoaderActivity.class.getSimpleName( ) ;
	
	private CurrentUsageModel model ;
	private CurrentUsageLoaderController controller ;
	
	private boolean initialized = false ;
	
	
	// PUBLIC METHODS
	
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState ) ;
		setContentView( R.layout.usagecurrent ) ;
		
		model = new CurrentUsageModel( ) ;
		controller = new CurrentUsageLoaderController( getApplicationContext( ), model, this ) ;
		controller.addOutboxHandler( new Handler( this ) ) ;
		controller.init( ) ;
		
		if( savedInstanceState != null ) { initialized = savedInstanceState.getBoolean( "initialized" ) ; }
		
		if( !initialized )
		{
			animateIn( ) ;
			initialized = true ;
		}
	}
	
	public boolean handleMessage( Message msg ) 
	{
		switch( msg.what ) 
		{
			case TotalUsageLoaderController.MESSAGE_ANIMATE_OUT:
				runOnUiThread( new Runnable( ) 
				{
					public void run( ) 
					{
						animateOut( ) ;
					}
				} ) ;
				return true;
		}
		return false ;
	}
	
	@Override
	public void onSaveInstanceState( Bundle savedInstanceState ) 
	{
		super.onSaveInstanceState( savedInstanceState ) ;
	    savedInstanceState.putBoolean( "initialized", initialized ) ;
	}
		
		
	// PRIVATE METHODS
	
	
	private void animateIn( )
	{
		final Animation a = AnimationUtils.loadAnimation( this, R.anim.title_animation_in ) ;
		a.setFillAfter( true ) ;
		final ImageView titleImage = ( ImageView ) findViewById( R.id.titleimage ) ;
		titleImage.startAnimation( a ) ;	
	}
	
	private void animateOut( )
	{
		final Animation a = AnimationUtils.loadAnimation( this, R.anim.title_animation_out ) ;
		a.setFillAfter( true ) ;
		final ImageView titleImage = ( ImageView ) findViewById( R.id.titleimage ) ;
		titleImage.startAnimation( a ) ;	
	}
	
}
