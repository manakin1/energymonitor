package au.com.planetinnovation.marconi.activities ;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView ;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import au.com.planetinnovation.marconi.controllers.DetailsController;
import au.com.planetinnovation.marconi.models.DetailsModel;
import au.com.planetinnovation.marconi.vos.* ;
import au.com.planetinnovation.marconi.widgets.CylinderScrollView;
import au.com.planetinnovation.marconi.MainApplication;
import au.com.planetinnovation.marconi.R ;

public class DetailsActivity extends Activity implements OnTouchListener, Handler.Callback
{

	private static final String TAG = DetailsActivity.class.getSimpleName( ) ;
	
	private int animDuration = 2000 ;
	private Boolean initialized = false ;	
	
	private ArrayList<RelativeLayout> bars = new ArrayList<RelativeLayout>( ) ;
	private ArrayList<LinearLayout> sections = new  ArrayList<LinearLayout>( ) ;
	private ArrayList<TextView> labels = new  ArrayList<TextView>( ) ;
	
	private int numCategories ;
	private int numVisibleCategories ;
	private int numItemsInCategory ;
	private int categoryDefaultHeight ;
	private int firstVisibleCategory ;
	private int primaryCategory ;
	private int[] categoryHeights ;
	private int[] visibleCategories ;
	
	private int valueSizeSmall = 40 ;
	private int valueSizeLarge = 50 ;
	private int scrollPosition = 0 ;
	
	private CylinderScrollView scroll ;
	private LinearLayout container ;
	private TextView valueCurrent ;
	
	private GestureDetector swipeDetector ;
	private GestureDetector mGestureDetector ;
	private OnTouchListener mGestureListener ;
	
	private Handler clearHandler = new Handler( ) ;
	
	private DetailsModel model ;
	private DetailsController controller ;
	
	private MainApplication application ;

	
	// PUBLIC METHODS
	
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState ) ;
		setContentView( R.layout.details ) ;

		application = ( MainApplication ) this.getApplicationContext( ) ;
		
		model = application.getCurrentDetailsModel( ) ;
		controller = new DetailsController( model, getApplicationContext( ), this ) ;
		controller.addOutboxHandler( new Handler( this ) ) ;
		
		sections.add( ( LinearLayout ) findViewById( R.id.column_section1 ) ) ;
		sections.add( ( LinearLayout ) findViewById( R.id.column_section2 ) ) ;
		sections.add( ( LinearLayout ) findViewById( R.id.column_section3 ) ) ;
		sections.add( ( LinearLayout ) findViewById( R.id.column_section4 ) ) ;
		
		labels.add( ( TextView ) findViewById( R.id.column_section1_text ) ) ;
		labels.add( ( TextView ) findViewById( R.id.column_section2_text ) ) ;
		labels.add( ( TextView ) findViewById( R.id.column_section3_text ) ) ;
		labels.add( ( TextView ) findViewById( R.id.column_section4_text ) ) ;
		
		valueCurrent = ( TextView ) findViewById( R.id.column_section_value ) ;
		container = ( LinearLayout ) findViewById( R.id.scroll_container ) ;
		scroll = ( CylinderScrollView ) findViewById( R.id.scrollview ) ;
		
		updateValues( ) ;
		render( ) ;
		setControls( ) ;
		
		swipeDetector = new GestureDetector( swipeListener ) ;
		scroll.init( this, model.getFeaturedItem( ) ) ;
		
		if( savedInstanceState != null ) 
		{ 
			initialized = savedInstanceState.getBoolean( "initialized" ) ;
		}
		
		if( !initialized ) animateIn( ) ;
	}
	
	@Override
	public void onSaveInstanceState( Bundle savedInstanceState ) 
	{
		super.onSaveInstanceState( savedInstanceState ) ;
	    savedInstanceState.putBoolean( "initialized", initialized ) ;
	    savedInstanceState.putInt( "scrollPosition", scroll.getScrollY( ) ) ;
	}
	
	@Override
	public void onRestoreInstanceState( Bundle savedInstanceState ) 
	{
		initialized = savedInstanceState.getBoolean( "initialized" ) ;
		scrollPosition = savedInstanceState.getInt( "scrollPosition" ) ; 
		scroll.scrollTo( 0, scrollPosition ) ;
	}
	
	public boolean onCreateOptionsMenu( Menu menu ) 
	{
	      MenuInflater inflater = getMenuInflater( ) ;
	      inflater.inflate( R.menu.options_menu, menu ) ;
	      return true ;
	}
	
	public boolean onOptionsItemSelected( MenuItem item ) 
	{
	      switch( item.getItemId( ) ) 
	      {
		      case R.id.menu_options :
		      {
		    	  controller.handleMessage( DetailsController.MESSAGE_SHOW_OPTIONS ) ;
		          return true ;
		      }
		     
		      default:
		          return super.onOptionsItemSelected( item ) ;
	      }
	}

	@Override
	public boolean onTouchEvent( MotionEvent event ) 
	{
		Log.i( TAG, "----------------------------TOUCH" ) ;
	    return swipeDetector.onTouchEvent( event ) ;
	}
	
	public boolean onTouch( View v, MotionEvent event )
	{
		return false ;
	}
	
	SimpleOnGestureListener swipeListener = new SimpleOnGestureListener( )
	{
		 @Override
		 public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY ) 
		 {
			 Log.i( TAG, "----------------SWIPED" ) ;
			 float sensitivity = 50 ;
			 if( ( e2.getX( ) - e1.getX( ) ) > sensitivity ) { controller.handleMessage( DetailsController.MESSAGE_GO_HOME ) ; }
			 return super.onFling( e1, e2, velocityX, velocityY ) ;
		 }
	} ;
	
	public boolean handleMessage( Message msg ) 
	{
		switch( msg.what ) 
		{
			case DetailsController.MESSAGE_ANIMATE_IN :
			{
				animateIn( ) ;
				return true ;
			}
			
			case DetailsController.MESSAGE_ANIMATE_OUT :
			{
				animateOut( ) ;
				return true ;
			}
			
			case DetailsController.MESSAGE_UPDATE_DISPLAY :
			{
				clear( ) ;
				model = application.getCurrentDetailsModel( ) ;
				updateValues( ) ;
				render( ) ;
				scroll.init( this, model.getFeaturedItem( ) ) ;
				return true ;
			}
		}
		
		return false ;
	}
	
	public void onScrollUpdated( int pos, int highlighted_item, int[] heights )
	{
		categoryHeights = new int[ numCategories ] ;
		primaryCategory = ( int ) Math.floor( highlighted_item / numItemsInCategory ) ;
		
		// make array of category heights
		
		for( int i = 0 ; i < numCategories ; i++ )
		{
			int h = 0 ;
			int index = 0 ;
			
			for( int j = 0 ; j < numItemsInCategory ; j++ )
			{
				index = ( i * numItemsInCategory ) + j ;
				h += heights[ index ] ;
			}
			
			categoryHeights[i] = h ;
		}
		
		// determine visible categories
		
		visibleCategories = new int[ numVisibleCategories ] ;
		firstVisibleCategory = ( int ) Math.floor( pos / categoryDefaultHeight ) ;
		
		for( int i = 0 ; i < numVisibleCategories ; i++ )
		{
			if( i == 0 ) visibleCategories[i] = firstVisibleCategory ;
			else visibleCategories[i] = visibleCategories[i-1] + 1 ;
		}
		
		// set section heights
		
		int h = 0 ;
		int accumulated_h = 0 ;
		
		for( int i = 0 ; i < sections.size( ) ; i++ )
		{
			if( i == 0 ) { h = categoryHeights[ visibleCategories[0] ] - ( pos ) + ( firstVisibleCategory * categoryDefaultHeight ) ; }
			else if( i > numVisibleCategories - 1 ) h = 0 ;
			else if( i == numVisibleCategories - 1 ) h = 480 - accumulated_h ;
			
			else
			{
				h = categoryHeights[ visibleCategories[i] ] ;
				if( h > 480 - accumulated_h ) h = 480 - accumulated_h ;
			}
			
			if( h > 480 ) h = 480 ;
			else if( h < 0 ) h = 0 ;
			accumulated_h += h ;
	
			sections.get( i ).setLayoutParams( new LinearLayout.LayoutParams( 180, h ) ) ;
		}
		
		// set section labels 
		
		for( int i = 0 ; i < visibleCategories.length ; i++ )
		{
			String label = "" ;
			
			try { label = model.getCategories( ).get( visibleCategories[ i ] ).getName( ) ; }
			catch( Exception e ) { }
			
			labels.get( i ).setText( label ) ;
		}
		
		updateHighlightedSection( primaryCategory ) ;
	}
	
	public void initGestureDetection( ) 
	{
        // gesture detection
		mGestureDetector = new GestureDetector( new SimpleOnGestureListener( ) 
		{
	        @Override
		    public boolean onDown( MotionEvent e ) 
		    {
		        return true;
		    }
		} ) ;
		
		mGestureListener = new View.OnTouchListener( ) 
		{
			public boolean onTouch( View v, MotionEvent e ) 
			{
				if( mGestureDetector.onTouchEvent( e ) ) { return true; }
	            if( e.getAction( ) == MotionEvent.ACTION_UP ) 
	            { 
	            	scroll.updateScrollPosition( ) ;
	            	scroll.snapToClosestItem( ) ; 
	            }

	            return false ;
	        }
	    } ;
	    
	    scroll.setOnTouchListener( mGestureListener ) ;
    } 
	
	
	// PRIVATE METHODS
	
	
	private void setControls( )
	{
		LinearLayout column = ( LinearLayout ) findViewById( R.id.column ) ;
		column.setOnClickListener( new View.OnClickListener( ) 
		{
		    public void onClick( View v ) 
		    {			    	
		    	controller.handleMessage( DetailsController.MESSAGE_GO_TO_PREVIOUS ) ;
		    }  
		} ) ;
		
		initGestureDetection( ) ;
	}
	
	private void updateHighlightedSection( int cat )
	{
		double value = 0 ;
		int highest = 0 ;
		int max_height = 0 ;
		
		// get the highlighted (highest) section
		
		for( int i = 0 ; i < sections.size( ) ; i++ )
		{
			if( sections.get( i ).getLayoutParams( ).height > max_height ) 
			{
				max_height = sections.get( i ).getLayoutParams( ).height ;
				highest = i ;
			}
		}

		// show/hide text views and effect layers of highlighted/non highlighted sections

		for( int i = 0 ; i < sections.size( ) ; i++ ) 
		{
			// highlighted section
			if( i == highest ) 
			{
				sections.get( i ).setBackgroundResource( R.drawable.column_bg ) ;
				
				for( int j = 0 ; j < sections.get( i ).getChildCount( ) ; j++ )
				{
					sections.get( i ).getChildAt( j ).setVisibility( View.GONE ) ;
				}
			}
			
			// non highlighted section
			else 
			{
				sections.get( i ).setBackgroundDrawable( null ) ;
				
				for( int j = 0 ; j < sections.get( i ).getChildCount( ) - 1 ; j++ )
				{
					sections.get( i ).getChildAt( j ).setVisibility( View.GONE ) ;
				}
				
				if( Math.abs( i - highest ) > 1 )
				{
					// darken background color of the section furthest away from the highlighted one
					float[] hsv = new float[3] ;
					int color = model.getParentColor( ) ;
					Color.colorToHSV( color, hsv ) ;
					hsv[2] *= 0.9f ;
					color = Color.HSVToColor( hsv ) ;
					sections.get( i ).setBackgroundColor( color ) ;
				}
				
				TextView tv = ( TextView ) sections.get( i ).getChildAt( sections.get( i ).getChildCount( ) - 1 ) ;
				tv.setVisibility( View.VISIBLE ) ;
			}
		}
		
		// calculate the combined value of all items in a section
		
		for( int i = 0 ; i < numItemsInCategory ; i++ )
		{
			value += model.getCategories( ).get( primaryCategory ).getItems( ).get( i ).getUsageTotal( ) ;
		}
			
		int usage = ( int ) Math.round( value ) ;
		valueCurrent.setText( Integer.toString( usage ) ) ;
		
		if( usage > 999 ) valueCurrent.setTextSize( valueSizeSmall ) ;
		else valueCurrent.setTextSize( valueSizeLarge ) ;
	}
	
	private void updateValues( )
	{
		numCategories = model.getNumCategories( ) ;
		numItemsInCategory = model.getNumItemsPerCategory( ) ;
		categoryDefaultHeight = 45 * numItemsInCategory ;
		
		// determine maximum number of categories visible on screen
		
		double d = ( 12.0 / ( double ) numItemsInCategory ) ;
		numVisibleCategories = ( int ) Math.ceil( d ) ;
		if( numItemsInCategory == 7 ) numVisibleCategories = 3 ;
		if( numCategories == 1 ) numVisibleCategories = 1 ;
		
	}
	
	private void clear( )
	{
		container.removeAllViews( ) ;
		bars = new ArrayList<RelativeLayout>( ) ;
	}

	private void render( ) 
	{
		LinearLayout column = ( LinearLayout ) findViewById( R.id.column ) ;
		column.setBackgroundColor( model.getParentColor( ) ) ;
		
		double max_value = 0 ;
		
		for( int i = 0 ; i < model.getData( ).size( ) ; i++ )
		{
			if( model.getData( ).get( i ).getUsageTotal( ) > max_value ) { max_value = model.getData( ).get( i ).getUsageTotal( ) ; }
		}

		for( int i = 0 ; i < model.getData( ).size( ) ; i++ )
		{
			UsagePeriodVO item = ( UsagePeriodVO ) model.getData( ).get( i ) ;
			
			int value = ( int ) Math.round( item.getUsageTotal( ) ) ;
			int w = ( int ) Math.round( ( value / max_value ) * 600 ) ;
			
			RelativeLayout bar = new RelativeLayout( this ) ;
			bar.setBackgroundResource( model.getBackground( ) ) ;
			bar.setLayoutParams( new RelativeLayout.LayoutParams( w, 45 ) ) ;
			bar.setId( 500 + i ) ;
			
			// image views
			
			ImageView v1 = new ImageView( this ) ;
			RelativeLayout.LayoutParams vParams1 = new RelativeLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT ) ;
			v1.setImageDrawable( v1.getResources( ).getDrawable( model.getShadow( ) ) ) ;
			v1.setScaleType( ScaleType.FIT_XY ) ;
			
			ImageView v2 = new ImageView( this ) ;
			RelativeLayout.LayoutParams vParams2 = new RelativeLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT ) ;
			v2.setImageDrawable( v2.getResources( ).getDrawable( model.getHighlight( ) ) ) ;
			v2.setScaleType( ScaleType.FIT_XY ) ;
				
			// text views
			
			TextView t = new TextView( this ) ;
			RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT ) ;
			tParams.setMargins( 70, 0, 0, 0 ) ;
			tParams.addRule( RelativeLayout.CENTER_VERTICAL ) ;
			
			t.setText( item.getName( ) ) ;
			t.setTextColor( Color.WHITE ) ;
			
			TextView t2 = new TextView( this ) ;
			RelativeLayout.LayoutParams tParams2 = new RelativeLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) ;
			tParams2.addRule( RelativeLayout.CENTER_VERTICAL ) ;
			tParams2.addRule( RelativeLayout.ALIGN_PARENT_RIGHT ) ;
			tParams2.setMargins( 0, 0, 10, 0 ) ;
		
			t2.setText( Integer.toString( value ) + "kWh" ) ;
			t2.setTextColor( Color.WHITE ) ;
		
			// add children
			
			bar.addView( v2, vParams2 ) ;
			bar.addView( t, tParams ) ;
			bar.addView( t2, tParams2 ) ;
			bar.addView( v1, vParams1 ) ;
			
			bar.setOnClickListener( new View.OnClickListener( ) 
			{
			    public void onClick( View v ) 
			    {			    	
			    	controller.handleMessage( DetailsController.MESSAGE_HANDLE_TOUCH, v.getId( ) - 500 ) ;
 			    }
			} ) ;
			
			bars.add( bar ) ;
			container.addView( bar ) ;
		}	
	}
	
	private void animateIn( )
	{
		TranslateAnimation a = new TranslateAnimation( -300, 0, 0, 0 ) ;
			
		a.setDuration( animDuration / 2 ) ;
		a.setFillAfter( true ) ;
		a.setStartOffset( 250 ) ;
				
		findViewById( R.id.column ).startAnimation( a ) ;	
		findViewById( R.id.shadow ).startAnimation( a ) ;	
		findViewById( R.id.value_container ).startAnimation( a ) ;	
			
		TranslateAnimation b = new TranslateAnimation( -700, 0, 0, 0 ) ;
				
		b.setDuration( animDuration ) ;
		b.setStartOffset( animDuration / 2 ) ;
		b.setFillAfter( true ) ;
				
		findViewById( R.id.scroll_container ).startAnimation( b ) ;	
		findViewById( R.id.arrow ).startAnimation( b ) ;
		
		initialized = true ;
	} 
	
	private void animateOut( )
	{
		TranslateAnimation a = new TranslateAnimation( 0, -700, 0, 0 ) ;
		
		a.setDuration( animDuration ) ;
		a.setFillAfter( true ) ;
				
		findViewById( R.id.scroll_container ).startAnimation( a ) ;	
		findViewById( R.id.arrow ).startAnimation( a ) ;
		
		TranslateAnimation b = new TranslateAnimation( 0, -300, 0, 0 ) ;
		
		b.setDuration( animDuration / 2 ) ;
		b.setStartOffset( animDuration ) ;
		b.setFillAfter( true ) ;
		
		findViewById( R.id.shadow ).startAnimation( b ) ;
		findViewById( R.id.column ).startAnimation( b ) ;	
		findViewById( R.id.value_container ).startAnimation( b ) ;	
		
		clearHandler.postDelayed( clearTask, animDuration ) ;
	}
	
	@Override
	protected void onDestroy( ) 
	{
		super.onDestroy( ) ;
		controller.dispose( ) ;
	}
	
	
	// TASKS
	

	private Runnable clearTask = new Runnable( )
	{
		public void run( )
		{
			clear( ) ;
		}
	} ;

}
