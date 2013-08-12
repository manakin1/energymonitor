package au.com.planetinnovation.marconi.activities ;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import au.com.planetinnovation.marconi.controllers.OptionsController;
import au.com.planetinnovation.marconi.controllers.OverviewController;
import au.com.planetinnovation.marconi.models.DetailsModel;
import au.com.planetinnovation.marconi.MainApplication;
import au.com.planetinnovation.marconi.R ;

public class OptionsActivity extends Activity implements Handler.Callback 
{

	private static final String TAG = OptionsActivity.class.getSimpleName( ) ;
	
	private OptionsController controller ;
	private boolean[] switchOn = new boolean[3] ;

	
	// PUBLIC METHODS
	
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState ) ;
		setContentView( R.layout.options ) ;
		
		controller = new OptionsController( getApplicationContext( ), this ) ;
		controller.addOutboxHandler( new Handler( this ) ) ;
		controller.init( ) ;
		
		SharedPreferences sharedPrefs = getSharedPreferences( "preferences", MODE_PRIVATE ) ;
		switchOn[0] = sharedPrefs.getBoolean( "switch1On", true ) ;
		switchOn[1] = sharedPrefs.getBoolean( "switch2On", false ) ;
		switchOn[2] = sharedPrefs.getBoolean( "switch3On", false ) ;
		
		render( ) ;
		setControls( ) ;
	}
	
	public boolean handleMessage( Message msg ) 
	{
		switch( msg.what ) 
		{
			case OverviewController.MESSAGE_SWITCH_DISPLAY :
			{
			
			}
		}
		
		return false ;
	}
	
	@Override
	public void onSaveInstanceState( Bundle savedInstanceState ) 
	{
		super.onSaveInstanceState( savedInstanceState ) ;
		
		SharedPreferences sharedPrefs = getSharedPreferences( "preferences", MODE_PRIVATE ) ;
		SharedPreferences.Editor editor = sharedPrefs.edit( ) ;
		
		editor.putBoolean( "switch1On", switchOn[0] ) ;
		editor.putBoolean( "switch2On", switchOn[1] ) ;
		editor.putBoolean( "switch3On", switchOn[2] ) ;
		editor.commit( ) ;
		
	}

	
	// PRIVATE METHODS
	

	private void render( ) 
	{
		final ImageView switch1 = ( ImageView ) findViewById( R.id.switch1 ) ;
		final ImageView switch2 = ( ImageView ) findViewById( R.id.switch2 ) ;
		final ImageView switch3 = ( ImageView ) findViewById( R.id.switch3 ) ;
		
		final ImageView bluetooth1 = ( ImageView ) findViewById( R.id.bluetooth1 ) ;
		final ImageView bluetooth2 = ( ImageView ) findViewById( R.id.bluetooth2 ) ;
		final ImageView bluetooth3 = ( ImageView ) findViewById( R.id.bluetooth3 ) ;
		
		switch1.setImageResource( switchOn[0] == false ? R.drawable.switch_off : R.drawable.switch_on ) ;
		bluetooth1.setImageResource( switchOn[0] == false ? R.drawable.bluetooth_off : R.drawable.bluetooth_on ) ;
		switch2.setImageResource( switchOn[1] == false ? R.drawable.switch_off : R.drawable.switch_on ) ;
		bluetooth2.setImageResource( switchOn[1] == false ? R.drawable.bluetooth_off : R.drawable.bluetooth_on ) ;
		switch3.setImageResource( switchOn[2] == false ? R.drawable.switch_off : R.drawable.switch_on ) ;
		bluetooth3.setImageResource( switchOn[2] == false ? R.drawable.bluetooth_off : R.drawable.bluetooth_on ) ;
	}

	private void saveFile( )
	{
		MainApplication app = ( MainApplication ) getApplicationContext( ) ;
		DetailsModel model = app.getCurrentDetailsModel( ) ;
		String extStorageDirectory = Environment.getExternalStorageDirectory( ).toString( ) ;
		File file = new File( extStorageDirectory, "data.csv" ) ;
		
		String columnString = "\"StartTime\",\"Usage\"" ;
		String dataString = "" ;
		
		for( int i = 0 ; i < model.getData( ).size( ) ; i++ )
		{
			dataString += "\"" + model.getData( ).get( i ).getTimestampStart( ) +"\",\"" + model.getData( ).get( i ).getUsageTotal( ) + "\"" ;
		}
		
		String str = columnString + "\n" + dataString ;
		OutputStreamWriter osw ;

		try
		{
			FileOutputStream fOut = new FileOutputStream( file ) ;
			//FileOutputStream fOut = openFileOutput( file, MODE_WORLD_READABLE ) ;
			osw = new OutputStreamWriter( fOut ) ; 
			osw.write( str ) ;
			osw.flush( ) ;
			osw.close( ) ;
		}
		
		catch( Exception e ) { e.printStackTrace( ) ; } ;
	}
	
	private void readFile( )
	{
		StringBuilder inb = new StringBuilder( ) ;
		String extStorageDirectory = Environment.getExternalStorageDirectory( ).toString( ) ;
		File file = new File( extStorageDirectory, "data.csv" ) ;

		try
		{
			FileInputStream fis = new FileInputStream( file ) ;
			int ch ;
			while( ( ch = fis.read( ) ) != -1 )
		    inb.append( ( char ) ch ) ;
			
			Log.i( TAG, "READING " + inb ) ;
		}
		
		catch( Exception e ) { e.printStackTrace( ) ; }
	}
	
	private void sendFile( )
	{
		String extStorageDirectory = Environment.getExternalStorageDirectory( ).toString( ) ;
		File file = new File( extStorageDirectory, "data.csv" ) ; 
		
		Uri u1 = Uri.fromFile( file ) ;

		Intent i = new Intent( Intent.ACTION_SEND ) ;
		i.putExtra( Intent.EXTRA_SUBJECT, "Power Usage Data" ) ;
		i.putExtra( Intent.EXTRA_STREAM, u1 ) ; 
		i.setType( "plain/text" ) ;
		startActivity( i ) ;
	}
	
	private void setControls( )
	{
		ImageView button_home = ( ImageView ) findViewById( R.id.button_home ) ;
		ImageView button_send = ( ImageView ) findViewById( R.id.button_send ) ;
		
		final ImageView switch1 = ( ImageView ) findViewById( R.id.switch1 ) ;
		final ImageView switch2 = ( ImageView ) findViewById( R.id.switch2 ) ;
		final ImageView switch3 = ( ImageView ) findViewById( R.id.switch3 ) ;
		
		button_home.setOnClickListener( new View.OnClickListener( ) 
		{
			public void onClick( View v )
			{
				controller.handleMessage( OptionsController.MESSAGE_GO_HOME ) ;
			}
		} ) ;
		
		button_send.setOnClickListener( new View.OnClickListener( ) 
		{
			public void onClick( View v )
			{
				saveFile( ) ;
				//readFile( ) ;
				
				Handler sendHandler = new Handler( ) ;
				sendHandler.postDelayed( sendTask, 500 ) ;
			}
		} ) ;
		
		switch1.setOnClickListener( new View.OnClickListener( ) 
		{
			public void onClick( View v )
			{
				switchOn[0] = !switchOn[0] ;
				render( ) ;
			}
		} ) ;
		
		switch2.setOnClickListener( new View.OnClickListener( ) 
		{
			public void onClick( View v )
			{
				switchOn[1] = !switchOn[1] ;
				render( ) ;
			}
		} ) ;	
		
		switch3.setOnClickListener( new View.OnClickListener( ) 
		{
			public void onClick( View v )
			{
				switchOn[2] = !switchOn[2] ;
				render( ) ;
			}
		} ) ;
	}
	
	@Override
	protected void onDestroy( ) 
	{
		super.onDestroy( ) ;
		
		SharedPreferences sharedPrefs = getSharedPreferences( "preferences", MODE_PRIVATE ) ;
		SharedPreferences.Editor editor = sharedPrefs.edit( ) ;
		
		editor.putBoolean( "switch1On", switchOn[0] ) ;
		editor.putBoolean( "switch2On", switchOn[1] ) ;
		editor.putBoolean( "switch3On", switchOn[2] ) ;
		editor.commit( ) ;
		
		controller.dispose( ) ;
	}

	
	// TASKS
	
	
	public Runnable sendTask = new Runnable( )
	{
		public void run( )
		{
			sendFile( ) ;
		}
	} ;
	
}
