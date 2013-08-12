package au.com.planetinnovation.marconi.controllers ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import au.com.planetinnovation.marconi.R;
import au.com.planetinnovation.marconi.activities.AverageUsageLoaderActivity;
import au.com.planetinnovation.marconi.models.CurrentUsageModel;

public class CurrentUsageController extends Controller 
{
	
	public static final int MESSAGE_HANDLE_TOUCH = 1 ;
	public static final int MESSAGE_GO_BACK = 2 ;
	
	private Handler updateHandler = new Handler( ) ;
	
	private Context context ;
	private CurrentUsageModel model ;
	private Activity activity ;
	
	
	public CurrentUsageController( CurrentUsageModel model, Context context, Activity activity )
	{
		this.model = model ;
		this.context = context ;
		this.activity = activity ;
	}
	
	
	// ACCESSOR METHODS
	
	
	public CurrentUsageModel getModel( ) 
	{
		return model ;
	}
	
	
	// PUBLIC METHODS
	
	
	@Override
	public boolean handleMessage( int what, Object data )
	{
		switch( what )
		{
			case MESSAGE_HANDLE_TOUCH :
			{
				break ;
			}
			
			case MESSAGE_GO_BACK :
			{
				Intent i = new Intent( context, AverageUsageLoaderActivity.class ) ;
				activity.startActivity( i ) ;
				activity.overridePendingTransition ( R.anim.push_right_in, R.anim.push_right_out ) ;
				activity.finish( ) ;
				
				break ;
			}
		}
		
		return false ;
	}
	
	
	// PRIVATE METHODS
	
	
	// TASKS

	
}

