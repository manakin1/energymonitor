package au.com.planetinnovation.marconi.controllers ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import au.com.planetinnovation.marconi.activities.TotalUsageLoaderActivity;
import au.com.planetinnovation.marconi.models.OverviewModel;

public class OverviewController extends Controller 
{
	
	public static final int MESSAGE_SWITCH_DISPLAY = 1 ;
	public static final int MESSAGE_HANDLE_TOUCH = 2 ;
	public static final int MESSAGE_ANIMATE_OUT = 3 ;
	public static final int MESSAGE_STOP_ANIMATION = 4 ;
	
	private int blockIndex ; 
	
	private Context context ;
	private OverviewModel model ;
	private Activity activity ;
	
	private Handler switchDisplayHandler = new Handler( ) ;
	private Handler launchHandler = new Handler( ) ;
	
	private static final String TAG = OverviewController.class.getSimpleName( ) ;
	
	
	public OverviewController( Context context, OverviewModel model, Activity activity )
	{
		this.context = context ;
		this.model = model ;
		this.activity = activity ;
	}
	
	
	// ACCESSOR METHODS
	
	
	public OverviewModel getModel( ) 
	{
		return model ;
	}
	
	public void init( )
	{
		switchDisplayHandler.postDelayed( switchDisplayTask, 3500 ) ;
	}
	
	
	// PUBLIC METHODS
	
	
	@Override
	public boolean handleMessage( int what, Object data )
	{
		switch( what )
		{
			case MESSAGE_HANDLE_TOUCH :
			{
				blockIndex = ( Integer ) data ;
				notifyOutboxHandlers( MESSAGE_ANIMATE_OUT, 0, 0, null ) ;
				launchHandler.postDelayed( launchTask, 800 ) ;
				break ;
			}
			
			case MESSAGE_STOP_ANIMATION :
			{
				switchDisplayHandler.removeCallbacks( switchDisplayTask ) ;
				break ;
			}
		}
		
		return false ;
	}
	
	
	// PRIVATE METHODS
	
	
	protected void launch( )
	{
		Intent i = new Intent( context, TotalUsageLoaderActivity.class ) ;
		i.setFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION ) ;
		i.putExtra( "au.com.planetinnovation.marconi.blockindex", blockIndex ) ;
		activity.startActivity( i ) ;
		
		activity.finish( ) ;
	}
	
	
	// TASKS
	
	
	private Runnable switchDisplayTask = new Runnable( )
	{
		public void run( )
		{
			notifyOutboxHandlers( MESSAGE_SWITCH_DISPLAY, 0, 0, null ) ;
			switchDisplayHandler.postDelayed( switchDisplayTask, 3500 ) ;
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

