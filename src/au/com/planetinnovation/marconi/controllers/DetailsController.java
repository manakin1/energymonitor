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
import au.com.planetinnovation.marconi.activities.OptionsActivity;
import au.com.planetinnovation.marconi.models.DetailsModel;
import au.com.planetinnovation.marconi.utils.DataLoader;
import au.com.planetinnovation.marconi.utils.DateUtils;
import au.com.planetinnovation.marconi.vos.UsageCategoryVO;
import au.com.planetinnovation.marconi.vos.UsagePeriodVO;

public class DetailsController extends Controller 
{
	
	public static final int MESSAGE_HANDLE_TOUCH = 1 ;
	public static final int MESSAGE_ANIMATE_OUT = 2 ;
	public static final int MESSAGE_ANIMATE_IN = 3 ;
	public static final int MESSAGE_UPDATE_DISPLAY = 4 ;
	public static final int MESSAGE_GO_HOME = 5 ;
	public static final int MESSAGE_GO_TO_PREVIOUS = 6 ;
	public static final int MESSAGE_SHOW_OPTIONS = 7 ;
	
	private int blockIndex ;
	private int currentCategory ;
	
	private MainApplication application ;
	
	private Handler updateHandler = new Handler( ) ;
	private DataLoader loader ;
	
	private Context context ;
	private DetailsModel model ;
	private Activity activity ;
	
	
	public DetailsController( DetailsModel model, Context context, Activity activity )
	{
		this.model = model ;
		this.context = context ;
		this.activity = activity ; 
		
		this.application = ( MainApplication ) context.getApplicationContext( ) ;
	}
	
	
	// ACCESSOR METHODS
	
	
	public DetailsModel getModel( ) 
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
				blockIndex = ( Integer ) data ;
				notifyOutboxHandlers( MESSAGE_ANIMATE_OUT, 0, 0, null ) ;
				updateHandler.postDelayed( updateTask, 3000 ) ;
				updateHandler.postDelayed( animateInTask, 3500 ) ;
				break ;
			}
			
			case MESSAGE_GO_HOME :
			{
				goHome( ) ;
				break ;
			}
			
			case MESSAGE_GO_TO_PREVIOUS :
			{
				loadPrevious( ) ;
				break ;
			}
			
			case MESSAGE_SHOW_OPTIONS :
			{
				Intent i = new Intent( context, OptionsActivity.class ) ;
				activity.startActivity( i ) ;
				activity.finish( ) ;
				break ;
			}
		}
		
		return false ;
	}
	
	
	// PRIVATE METHODS
	
	
	private void loadPrevious( )
	{
		currentCategory-- ;
		
		try 
		{ 
			model = application.getDetailsModel( currentCategory ) ; 
			notifyOutboxHandlers( MESSAGE_ANIMATE_OUT, 0, 0, null ) ;
			application.setCurrentDetailsModel( model ) ;
			updateHandler.postDelayed( updateDisplayTask, 3000 ) ;
			updateHandler.postDelayed( animateInTask, 3500 ) ;
		}
		
		catch( Exception e ) { goHome( ) ; }

	}
	
	private void goHome( )
	{
		Intent i = new Intent( context, AverageUsageLoaderActivity.class ) ;
		activity.startActivity( i ) ;
		activity.overridePendingTransition ( R.anim.slide_in_left, R.anim.slide_out_right ) ;
		activity.finish( ) ;
	}
	
	private void loadPeriodData( )
	{
		ArrayList<UsagePeriodVO> arr = new ArrayList<UsagePeriodVO>( ) ;	
		ArrayList<JSONObject> data = new ArrayList<JSONObject>( ) ;
		ArrayList<UsageCategoryVO> categories = new ArrayList<UsageCategoryVO>( ) ;
		
		if( loader == null ) loader = new DataLoader( ) ;	
		
		int featured = 0 ;
		Date featured_date = new Date( ) ;
		int cat = model.getCategory( ) ;
		
		if( cat > 4 )
		{
			launch( ) ;
			return ;
		}
		
		cat++ ;
		
		UsagePeriodVO obj = model.getData( ).get( blockIndex ) ;
		
		model = new DetailsModel( ) ;
		model.setCategory( cat ) ;
		currentCategory = cat ;
		
		model.setBackground( application.getBackground( cat ) ) ;
		model.setShadow( application.getShadow( cat ) ) ;
		model.setHighlight( application.getHighlight( cat ) ) ;
		model.setParentColor( application.getColor( cat ) ) ;
		
		Date start_date = obj.getTimestampStart( ) ;
		Date new_date = new Date( ) ;
		
		if( start_date.getYear( ) < 1000 ) start_date.setYear( start_date.getYear( ) + 1900 ) ;
		
		int year = start_date.getYear( ) ;
		int month = start_date.getMonth( ) ;
		int day = start_date.getDate( ) ;
		int hours = start_date.getHours( ) ;
		int minutes = start_date.getMinutes( ) ;
		
		Calendar cal = Calendar.getInstance( ) ;
	    cal.set( year, month, day, hours, minutes ) ;
			
		switch( cat )
		{
			case 1 :
			{
				cal.add( Calendar.MONTH, -6 ) ;
		    	new_date = cal.getTime( ) ;
		    	featured = 7 ;
		    	featured_date = new Date( new_date.getYear( ), new_date.getMonth( ), 15 ) ;
				data = loader.getMonths( new_date.getYear( ), new_date.getMonth( ), 15, 1 ) ;
				break ;
			}
			
			case 2 :
			{
				cal.add( Calendar.DATE, -28 ) ;
		    	new_date = cal.getTime( ) ;
		    	featured = 5 ;
		    	featured_date = new Date( new_date.getYear( ), new_date.getMonth( ), 1 ) ;
				data = loader.getWeeks( new_date.getYear( ), new_date.getMonth( ), 1, 16, 1 ) ;
				break ;
			}
			
			case 3 :
			{
				cal.add( Calendar.DATE, -7 ) ;
		    	new_date = cal.getTime( ) ;
		    	featured = 7 ;
		    	featured_date = new Date( new_date.getYear( ), new_date.getMonth( ), new_date.getDate( ) ) ;
				data = loader.getDays( new_date.getYear( ), new_date.getMonth( ), new_date.getDate( ), 28, 1 ) ;
				break ;
			}
			
			case 4 :
			{
		    	new_date = cal.getTime( ) ;
		    	featured = 12 ;
		    	featured_date = new Date( new_date.getYear( ), new_date.getMonth( ), new_date.getDate( ), 0, 0 ) ;
				data = loader.getHours( new_date.getYear( ), new_date.getMonth( ), new_date.getDate( ), 0, 24, 1 ) ;
				break ;
			}
			
			case 5 :
			{
		    	new_date = cal.getTime( ) ;
		    	featured = 30 ;
		    	featured_date = new Date( new_date.getYear( ), new_date.getMonth( ), new_date.getDate( ), new_date.getHours( ), new_date.getMinutes( ), 0 ) ;
				data = loader.getMinutes( new_date.getYear( ), new_date.getMonth( ), new_date.getDate( ), new_date.getHours( ), new_date.getMinutes( ), 60, 1 ) ;
				break ;
			}
		}
			
		arr = loader.loadHistoricalData( cat, data ) ;
		categories = DateUtils.categorize( cat, arr ) ;
			
		model.setData( arr ) ;
		model.setCategories( categories ) ;
		model.setFeaturedItem( featured ) ;
		application.setCategoryTime( cat, featured_date ) ;
		application.setDetailsModel( cat, model ) ;
		application.setCurrentDetailsModel( model ) ;
	}
	
	private void launch( )
	{
		Intent i = new Intent( context, CurrentUsageLoaderActivity.class ) ;
		activity.startActivity( i ) ;
		activity.finish( ) ;
	}
	
	
	// TASKS
	
	
	private Runnable updateDisplayTask = new Runnable( )
	{
		public void run( )
		{
			notifyOutboxHandlers( MESSAGE_UPDATE_DISPLAY, 0, 0, null ) ;
		}
	} ;
	
	private Runnable updateTask = new Runnable( )
	{
		public void run( )
		{
			loadPeriodData( ) ;
			notifyOutboxHandlers( MESSAGE_UPDATE_DISPLAY, 0, 0, null ) ;
		}
	} ;
	
	private Runnable animateInTask = new Runnable( )
	{
		public void run( )
		{
			notifyOutboxHandlers( MESSAGE_ANIMATE_IN, 0, 0, null ) ;
		}
	} ;
	
}

