package au.com.planetinnovation.marconi;

import java.util.Date;

import android.app.Application ;
import au.com.planetinnovation.marconi.models.CurrentUsageModel;
import au.com.planetinnovation.marconi.models.DetailsModel;
import au.com.planetinnovation.marconi.models.OverviewModel;

public class MainApplication extends Application 
{

		private OverviewModel overviewModel ;
		private DetailsModel detailsModel ;
		private CurrentUsageModel currentUsageModel ;
		
		private DetailsModel[] detailsModels = new DetailsModel[7] ;
		
		private Date[] categoryTimes = new Date[6] ;
		
		private String[][] names = { { "Spring", "Summer", "Autumn", "Winter" },
									 { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" },
									 { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" } } ;
		
		private int[] backgrounds = { R.drawable.barbg1, R.drawable.barbg2, R.drawable.barbg3, R.drawable.barbg4, R.drawable.barbg5, R.drawable.barbg6 } ; 
		private int[] highlights = { R.drawable.barbg1_hl, R.drawable.barbg2_hl, R.drawable.barbg3_hl, R.drawable.barbg4_hl, R.drawable.barbg5_hl, R.drawable.barbg6_hl } ;
		private int[] shadows = { R.drawable.barbg1_shadow, R.drawable.barbg2_shadow, R.drawable.barbg3_shadow, R.drawable.barbg4_shadow, R.drawable.barbg5_shadow, R.drawable.barbg6_shadow } ;
		
		private int rootColor = 0x515e6b ;
		
		
		@Override
		public void onCreate( )
		{
			super.onCreate( ) ;
		}
		
		
		// ACCESSOR METHODS
		
		
		public OverviewModel getOverviewModel( )
		{
			return overviewModel ;
		}
		
		public DetailsModel getCurrentDetailsModel( )
		{
			return detailsModel ;
		}
		
		public DetailsModel getDetailsModel( int index )
		{
			return detailsModels[ index ] ;
		}
		
		public void setDetailsModel( int index, DetailsModel model )
		{
			detailsModels[ index ] = model ;
		}
		
		public CurrentUsageModel getCurrentUsageModel( )
		{
			return currentUsageModel ;
		}
		
		public void setOverviewModel( OverviewModel model )
		{
			overviewModel = model ;
		}
		
		public void setCurrentDetailsModel( DetailsModel model )
		{
			detailsModel = model ;
		}
		
		public void setCurrentUsageModel( CurrentUsageModel model )
		{
			currentUsageModel = model ;
		}
		
		public int getRootColor( )
		{
			return rootColor ;
		}
		
		public int getColor( int index )
		{
			int[] colors = { getResources( ).getColor( R.color.color1 ), getResources( ).getColor( R.color.color2 ), 
							 getResources( ).getColor( R.color.color3 ), getResources( ).getColor( R.color.color4 ), 
							 getResources( ).getColor( R.color.color5 ), getResources( ).getColor( R.color.color6 ), 
							 getResources( ).getColor( R.color.color7 ) } ;
			return colors[ index ] ;
		}
		
		public int getBackground( int index )
		{
			return backgrounds[ index ] ;
		}
		
		public int getShadow( int index )
		{
			return shadows[ index ] ;
		}
		
		public int getHighlight( int index )
		{
			return highlights[ index ] ;
		}
		
		public String[] getNames( int index )
		{
			return names[ index ] ;
		}
		
		public Date getCategoryTime( int index )
		{
			return categoryTimes[ index ] ;
		}
		
		public void setCategoryTime( int index, Date timestamp )
		{
			categoryTimes[ index ] = timestamp ;
		}

}
