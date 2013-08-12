package au.com.planetinnovation.marconi.controllers ;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import au.com.planetinnovation.marconi.MainApplication;
import au.com.planetinnovation.marconi.activities.AverageUsageLoaderActivity;
import au.com.planetinnovation.marconi.activities.CurrentUsageLoaderActivity;
import au.com.planetinnovation.marconi.activities.DetailsActivity;
import au.com.planetinnovation.marconi.models.DetailsModel;
import au.com.planetinnovation.marconi.utils.DataLoader;
import au.com.planetinnovation.marconi.utils.DateUtils;
import au.com.planetinnovation.marconi.vos.UsageCategoryVO;
import au.com.planetinnovation.marconi.vos.UsagePeriodVO;

public class OptionsController extends Controller 
{
	
	public static final int MESSAGE_GO_HOME = 1 ;
	
	private MainApplication application ;
	
	private Context context ;
	private Activity activity ;
	
	
	public OptionsController( Context context, Activity activity )
	{
		this.context = context ;
		this.activity = activity ; 
		
		this.application = ( MainApplication ) context.getApplicationContext( ) ;
	}
	
	
	// ACCESSOR METHODS
	
	
	// PUBLIC METHODS
	
	
	public void init( )
	{
		
	}
	
	@Override
	public boolean handleMessage( int what, Object data )
	{
		switch( what )
		{
			case MESSAGE_GO_HOME :
			{
				goHome( ) ;
				break ;
			}
		}
		
		return false ;
	}
	
	
	// PRIVATE METHODS

	
	private void goHome( )
	{
		Intent i = new Intent( context, AverageUsageLoaderActivity.class ) ;
		activity.startActivity( i ) ;
		activity.overridePendingTransition ( R.anim.slide_in_left, R.anim.slide_out_right ) ;
		activity.finish( ) ;
	}

	private void launch( )
	{
		Intent i = new Intent( context, DetailsActivity.class ) ;
		activity.startActivity( i ) ;
		activity.finish( ) ;
	}
	
	
	// TASKS
	
}

