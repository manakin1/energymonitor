package au.com.planetinnovation.marconi.activities ;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.view.View.OnTouchListener;
import android.widget.TextView ;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import au.com.planetinnovation.marconi.controllers.OverviewController;
import au.com.planetinnovation.marconi.models.OverviewModel;
import au.com.planetinnovation.marconi.MainApplication;
import au.com.planetinnovation.marconi.R ;

public class OverviewActivity extends Activity implements OnTouchListener, Handler.Callback 
{

	private static final String TAG = OverviewActivity.class.getSimpleName( ) ;
	
	private OverviewModel model ;
	private OverviewController controller ;
	private RelativeLayout[] bars = new RelativeLayout[5] ;
	
	private int[] layouts = { R.id.bar1, R.id.bar2, R.id.bar3, R.id.bar4, R.id.bar5 } ;
	private int[] values = { R.id.value1, R.id.value2, R.id.value3, R.id.value4, R.id.value5 } ;
	private int[] prices = { R.id.price1, R.id.price2, R.id.price3, R.id.price4, R.id.price5 } ;

	private int display = 0 ;
	private int blockWidth = 160 ;
	private int animDuration = 800 ;
	
	private Boolean initialized = false ;	
	
	private AlphaAnimation switchAnimation1 ;
	private AlphaAnimation switchAnimation2 ;
	private TranslateAnimation inAnimation ;

	
	// PUBLIC METHODS
	
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState ) ;
		setContentView( R.layout.overview ) ;
		
		if( savedInstanceState != null ) { initialized = savedInstanceState.getBoolean( "initialized" ) ; }
		
		MainApplication app = ( MainApplication ) this.getApplicationContext( ) ;
		model = app.getOverviewModel( ) ;
		controller = new OverviewController( getApplicationContext( ), model, this ) ;
		controller.addOutboxHandler( new Handler( this ) ) ;
		controller.init( ) ;
		
		render( ) ;
	}
	
	public boolean onTouch( View v, MotionEvent e ) 
	{
		if( inAnimation == null || inAnimation.hasEnded( ) )
		{
			int index = 0 ;
			
			for( int i = 0 ; i < bars.length ; i++ )
			{
				if( bars[i].getId( ) == v.getId( ) )
			    {
			       index = i ;
			       break;
			    }
			}
			
			controller.handleMessage( OverviewController.MESSAGE_HANDLE_TOUCH, index ) ;
		}
		
		return false ;
	}
	
	public boolean handleMessage( Message msg ) 
	{
		switch( msg.what ) 
		{
			case OverviewController.MESSAGE_SWITCH_DISPLAY :
			{
				runOnUiThread( new Runnable( ) 
				{
					public void run( ) 
					{
						switchDisplay( ) ;
					}
				} ) ;
				return true ;
			}
			
			case OverviewController.MESSAGE_ANIMATE_OUT :
			{
				animateOut( ) ;
				return true ;
			}
		}
		
		return false ;
	}

	
	// PRIVATE METHODS
	

	private void render( ) 
	{
		TextView value_tv = new TextView( this ) ;
		TextView price_tv = new TextView( this ) ;
		TextView price_tv2 = new TextView( this ) ;
		
		int h = 0 ;
		int max_height = 480 ;
		double value = 0 ;
		int display_value = 0 ;
		double max_value = 0 ;
		
		for( int i = 0 ; i < bars.length ; i++ )
		{
			if( model.getAverageUsage( ).get( i ) > max_value )
			{
				max_value = model.getAverageUsage( ).get( i ) ;
			}
		}
		
		for( int i = 0 ; i < bars.length ; i++ )
		{
			RelativeLayout value_container = ( RelativeLayout ) findViewById( values[i] ) ;
			LinearLayout price_container = ( LinearLayout ) findViewById( prices[i] ) ;
			
			value_tv = ( TextView ) value_container.getChildAt( 0 ) ;
			price_tv = ( TextView ) price_container.getChildAt( 1 ) ;
			price_tv2 = ( TextView ) price_container.getChildAt( 2 ) ;
			
			value = ( int ) Math.round( model.getAverageUsage( ).get( i ) ) ;
			display_value = ( int ) value ;
			h = ( int ) Math.round( ( value / max_value ) * max_height ) ;
			
			if( value < 100 ) value_tv.setText( "0" + display_value ) ;
			else value_tv.setText( Double.toString( value ) ) ;
			
			double price = Math.round( model.getAverageUsage( ).get( i ) * 0.20 ) ;
			int dollars = ( int ) price ;
			double cents = ( price - dollars ) * 100 ;
			price_tv.setText( Integer.toString( dollars ) ) ;
			
			price_tv2.setText( ".00" ) ;
			
			//Log.i( TAG, "PRICE " + price + " " + cents ) ;
			
			bars[i] = ( RelativeLayout ) findViewById( layouts[i] ) ;
			bars[i].setLayoutParams( new LinearLayout.LayoutParams( blockWidth, h ) ) ;
			
			bars[i].setOnTouchListener( ( OnTouchListener ) this ) ; 
			
			// animate view in
			
		    if( !initialized ) 
		    {
		    	inAnimation = new TranslateAnimation( 0, 0, max_height, 0 ) ;
				
		    	inAnimation.setDuration( animDuration ) ;
		    	inAnimation.setStartOffset( i * ( animDuration / 2 ) ) ;
		    	inAnimation.setFillAfter( true ) ;
				
		    	bars[i].startAnimation( inAnimation ) ;	 
		    	
		    	AlphaAnimation b = new AlphaAnimation( 0, 0.9F ) ;
		    	b.setDuration( animDuration ) ;
		    	b.setStartOffset( i * ( animDuration / 2 ) + 400 ) ;
		    	findViewById( values[i] ).startAnimation( b ) ;
		    	b.setFillAfter( true ) ;
		    }
		}
		
    	initialized = true ;
	}
	
	private void switchDisplay( )
	{
			for( int i = 0 ; i < values.length ; i++ )
			{
				// usage text
				
				RelativeLayout value = ( RelativeLayout ) findViewById( values[i] ) ;
				
				switchAnimation1 = new AlphaAnimation( 0.9F, 0 ) ;
				if( display == 1 ) 
				{ 
					switchAnimation1 = new AlphaAnimation( 0, 0.9F ) ;
					switchAnimation1.setStartOffset( animDuration / 2 ) ;
				}
				
				switchAnimation1.setDuration( animDuration / 2 ) ;
		    	value.startAnimation( switchAnimation1 ) ;
		    	switchAnimation1.setFillAfter( true ) ;
		    	
		    	// price text
		    	
		    	LinearLayout price = ( LinearLayout ) findViewById( prices[i] ) ;
		    	
		    	switchAnimation2 = new AlphaAnimation( 0, 0.9F ) ;
				if( display == 1 ) 
				{ 
					switchAnimation2 = new AlphaAnimation( 0.9F, 0 ) ; 	
				}
				
				else switchAnimation2.setStartOffset( animDuration / 2 ) ;
				switchAnimation2.setDuration( animDuration / 2 ) ;
		    	
		    	price.startAnimation( switchAnimation2 ) ;
		    	switchAnimation2.setFillAfter( true ) ;
			}
			
			display = Math.abs( display - 1 ) ;
	} ;
	
	private void animateOut( )
	{
		if( switchAnimation1 != null ) switchAnimation1.cancel( ) ;
		if( switchAnimation2 != null ) switchAnimation2.cancel( ) ;
		
		controller.handleMessage( OverviewController.MESSAGE_STOP_ANIMATION ) ;
		
		for( int i = 0 ; i < bars.length ; i++ )
		{
			TranslateAnimation a = new TranslateAnimation( 0, 0, 0, 480 ) ;
				
			a.setDuration( 800 ) ;
			a.setFillAfter( true ) ;
				
		    bars[i].startAnimation( a ) ;	 
		}
	}
	
	@Override
	protected void onDestroy( ) 
	{
		super.onDestroy( ) ;
		controller.dispose( ) ;
	}
	
	@Override
	public void onSaveInstanceState( Bundle savedInstanceState ) 
	{
		super.onSaveInstanceState( savedInstanceState ) ;
	    savedInstanceState.putBoolean( "initialized", initialized ) ;
	}
	
}
