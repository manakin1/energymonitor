package au.com.planetinnovation.marconi.widgets ;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import au.com.planetinnovation.marconi.R;
import au.com.planetinnovation.marconi.activities.DetailsActivity;

public class CylinderScrollView extends ScrollView 
{

	private int itemMinHeight = 45 ;
	private int itemMaxHeight = 125 ;
	private int maxDistance = 85 ;
	private int containerMaxHeight = 480 ;
	private int centerpoint = 240 ;
	private int retryDelay = 350 ;
	
	private int currentItem ;
	private int featuredItem ;
	
	private LinearLayout container ;
	private DetailsActivity activity ;
	
	
    // PUBLIC METHODS
    
    
    public CylinderScrollView( Context context ) 
    {
        super( context ) ;
    }

    public CylinderScrollView( Context context, AttributeSet attrs ) 
    {
        super( context, attrs ) ;
    }

    public CylinderScrollView( Context context, AttributeSet attrs, int defStyle ) 
    {
        super( context, attrs, defStyle ) ;
    }
    
    public void reset( )
    {
    	int h = container.getHeight( ) - containerMaxHeight ;
		int pos = ( featuredItem - 4 ) * itemMinHeight ;
		
		if( pos == 0 ) pos = 1 ;
    	if( h > 0 ) scrollTo( 0, pos ) ;

		else
		{
			Handler handler = new Handler( ) ;
			handler.postDelayed( retryResetTask, retryDelay ) ;
		}
    }
    
    public void init( DetailsActivity activity, int featured )
    {
    	this.activity = activity ;
    	this.featuredItem = featured ;
    	container = ( LinearLayout ) findViewById( R.id.scroll_container ) ;
    	
    	reset( ) ;
    }
    
    public void snapToClosestItem( )
    {
    	RelativeLayout item = ( RelativeLayout ) container.getChildAt( currentItem ) ;
    	item.setLayoutParams( new LinearLayout.LayoutParams( item.getWidth( ) , itemMaxHeight ) ) ;
    	int itempos = ( currentItem - 4 ) * 45 ;
    	this.scrollTo( 0, itempos ) ;
    	
    	update( ) ;
    }
    
    public void updateScrollPosition( )
    {
    	update( ) ;
    }
    
    
    // PRIVATE METHODS


    @Override
    protected void onScrollChanged( int l, int t, int oldl, int oldt ) 
    {
    	super.onScrollChanged( l, t, oldl, oldt ) ;
    	
        int h = container.getHeight( ) - containerMaxHeight ;
        if( t < h ) update( ) ; 
    }
    
    private int getHighlightedItem( )
    {
    	int index = 0 ;
    	int max_h = 0 ;
    	
    	for( int i = 0 ; i < container.getChildCount( ) ; i++ )
        {
    		int h = container.getChildAt( i ).getHeight( ) ;
    		
    		if( h > max_h )
    		{
    			index = i ;
    			max_h = h ;
    		}
        }
    	
    	currentItem = index ;
    	
    	return index ;
    }

    private void update( )    
    {    
    	int[] height_arr = new int[ container.getChildCount( ) ] ;

        for( int i = 0 ; i < container.getChildCount( ) ; i++ )
        {
        	int[] pos = new int[2] ;
        	
        	RelativeLayout item = ( RelativeLayout ) container.getChildAt( i ) ;
        	item.getLocationOnScreen( pos ) ;
        	
        	// resize bars
        	
        	int max_distance_shadow = centerpoint + 20 ;
        	int min_distance_shadow = 50 ;
        	int itemPos = pos[1] + ( item.getHeight( ) / 2 ) ;
        	int w = item.getWidth( ) ;

           	double distance_scale = Math.abs( itemPos - centerpoint ) ;
           	double distance_shadow = Math.abs( itemPos - centerpoint ) ;
           	if( distance_shadow < min_distance_shadow ) distance_shadow = 0 ;
           	if( distance_shadow > centerpoint ) distance_shadow = centerpoint ;
           	
           	if( distance_scale > maxDistance ) distance_scale = maxDistance ;
           	if( distance_shadow > max_distance_shadow ) distance_shadow = max_distance_shadow ;
           	double distance_ratio = 1.0 - ( distance_scale / maxDistance ) ;
           	double distance_ratio_shadow = 1.0 - ( distance_shadow / max_distance_shadow ) ;
           	
           	int h = ( int ) Math.round( itemMinHeight + ( distance_ratio * ( itemMaxHeight - itemMinHeight ) ) ) ;
           	height_arr[i] = h ;
           	
           	item.setLayoutParams( new LinearLayout.LayoutParams( w, h ) ) ;
           	 
           	// resize text views 
           	
           	ImageView shadow = ( ImageView ) item.getChildAt( 3 ) ;
           	int alpha1 = 150 - ( int ) Math.round( ( 255 * distance_ratio_shadow ) ) ;
           	if( alpha1 > 255 ) alpha1 = 255 ;
           	if( alpha1 < 0 ) alpha1 = 0 ;
           	
           	shadow.setAlpha( alpha1 ) ;
           	
           	ImageView hl = ( ImageView ) item.getChildAt( 0 ) ;
           	int alpha2 = ( int ) Math.round( 255 * distance_ratio ) ;
        	hl.setAlpha( alpha2 ) ;
        	
           	TextView tv1 = ( TextView ) item.getChildAt( 1 ) ;
        	TextView tv2 = ( TextView ) item.getChildAt( 2 ) ;
        	
           	float textSize1 = Math.round( 12 + ( distance_ratio * ( 10 ) ) ) ;
           	float textSize2 = Math.round( 12 + ( distance_ratio * ( 14 ) ) ) ;
           	tv1.setTextSize( textSize1 ) ;
           	tv2.setTextSize( textSize2 ) ;
        }
        
        currentItem = getHighlightedItem( ) ;
        if( currentItem > 0 ) activity.onScrollUpdated( getScrollY( ), currentItem, height_arr ) ;
    	
    	else
    	{
    		Handler retryHandler = new Handler( ) ;
        	retryHandler.postDelayed( retryUpdateTask, retryDelay ) ; 
    	}
    }
    
    
    // TASKS
    
    
    public Runnable retryResetTask = new Runnable( )
    {
    	public void run( )
    	{
    		reset( ) ;
    	}
    } ;
    
    public Runnable retryUpdateTask = new Runnable( )
    {
    	public void run( )
    	{
    		update( ) ;
    	}
    } ;

}