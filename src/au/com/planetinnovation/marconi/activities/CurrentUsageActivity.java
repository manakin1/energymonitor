package au.com.planetinnovation.marconi.activities ;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.TextView ;
import android.widget.RelativeLayout;

import au.com.planetinnovation.marconi.controllers.CurrentUsageController;
import au.com.planetinnovation.marconi.interfaces.IOnChangeListener ;
import au.com.planetinnovation.marconi.models.CurrentUsageModel;
import au.com.planetinnovation.marconi.models.DetailsModel;
import au.com.planetinnovation.marconi.MainApplication;
import au.com.planetinnovation.marconi.R ;

public class CurrentUsageActivity extends Activity implements OnTouchListener
{

	private static final String TAG = CurrentUsageActivity.class.getSimpleName( ) ;
	
	private long delay = 1000 ;
	private double currentValue = 0 ;
	private double maxValue = 0 ;
	private int indicatorPosition = 400 ;
	private int maxIndicatorPosition = 400 ;
	private int containerWidth = 700 ;
	
	private Boolean initialized = false ;	
	
	private RelativeLayout indicator ;
	private RelativeLayout maxIndicator ;
	private TextView valueLabel ;
	private TextView maxValueLabel ;
	
	private Handler updateHandler = new Handler( ) ;
	private GestureDetector swipeDetector ;
	
	private TranslateAnimation animation ;
	private TranslateAnimation maxAnimation ;
	
	private CurrentUsageModel model ;
	private CurrentUsageController controller ;

	
	// PUBLIC METHODS
	
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState ) ;
		setContentView( R.layout.livedetails ) ;
		
		if( savedInstanceState != null ) { initialized = savedInstanceState.getBoolean( "initialized" ) ; }
		
		MainApplication app = ( MainApplication ) this.getApplicationContext( ) ;
		
		model = app.getCurrentUsageModel( ) ;
		controller = new CurrentUsageController( model, getApplicationContext( ), this ) ;
		
		indicator = ( RelativeLayout ) findViewById( R.id.indicator ) ;
		maxIndicator = ( RelativeLayout ) findViewById( R.id.max_indicator ) ;
		valueLabel = ( TextView ) findViewById( R.id.indicator_value ) ;
		maxValueLabel = ( TextView ) findViewById( R.id.max_indicator_value ) ;
		
		swipeDetector = new GestureDetector( swipeListener ) ;
		
		render( ) ;
		
		initTimer( ) ;
	}
	
	public boolean onTouch( View v, MotionEvent event )
	{
		return false ;
	}
	
	@Override
	public boolean onTouchEvent( MotionEvent event ) 
	{
	    return swipeDetector.onTouchEvent( event ) ;
	}
	
	SimpleOnGestureListener swipeListener = new SimpleOnGestureListener( )
	{
		 @Override
		 public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY ) 
		 {
			 float sensitivity = 50 ;
			 if( ( e2.getX( ) - e1.getX( ) ) > sensitivity ) { controller.handleMessage( CurrentUsageController.MESSAGE_GO_BACK ) ; }
			 return super.onFling( e1, e2, velocityX, velocityY ) ;
		 }
	} ;

	@Override
	public void onSaveInstanceState( Bundle savedInstanceState ) 
	{
		super.onSaveInstanceState( savedInstanceState ) ;
	    savedInstanceState.putBoolean( "initialized", initialized ) ;
	}
	
	
	// PRIVATE METHODS
	
	
	private void render( ) 
	{
		
	}
	
	private void initTimer( )
	{
		updateHandler.post( updateTask ) ;
	}
	
	@Override
	protected void onDestroy( ) 
	{
		super.onDestroy( ) ;
		controller.dispose( ) ;
	}

	private void animateIndicator( )
	{
		
		double ratio = currentValue / 80 ;
		int pos = ( int ) Math.round( ratio * containerWidth ) ;
		animation = new TranslateAnimation( indicatorPosition, pos, 0, 0 ) ;
		animation.setDuration( delay - 100 ) ;
		animation.setFillAfter( true ) ;
		indicator.startAnimation( animation ) ;	
		
		indicatorPosition = pos ;
	}
	
	private void animateMaxValueIndicator( )
	{	
		double ratio = maxValue / 80 ;
		int pos = ( int ) Math.round( ratio * containerWidth ) + 43 ;
		maxAnimation = new TranslateAnimation( maxIndicatorPosition, pos, 0, 0 ) ;
		maxAnimation.setDuration( delay - 100 ) ;
		maxAnimation.setFillAfter( true ) ;
		maxIndicator.startAnimation( maxAnimation ) ;	
		
		maxIndicatorPosition = pos ;
	}
	
	
	// TASKS
	
	
	private Runnable updateTask = new Runnable( )
	{
		public void run( )
		{
			double new_value = Math.round( 20 + ( Math.random( ) * 10 ) ) ;
			currentValue = new_value ;
			valueLabel.setText( Integer.toString( ( int ) currentValue ) + "kW" ) ;
			updateHandler.postDelayed( updateTask, delay ) ;
			
			if( new_value > maxValue ) 
			{
				maxValue = new_value ;
				maxValueLabel.setText( Integer.toString( ( int ) maxValue ) + "kW" ) ;
				animateMaxValueIndicator( ) ;
			}
			
			animateIndicator( ) ;
		}
	} ;
	

}
