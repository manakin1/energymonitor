package au.com.planetinnovation.marconi.models;

import java.util.ArrayList;

import au.com.planetinnovation.marconi.vos.SimpleObservable;
import au.com.planetinnovation.marconi.vos.UsagePeriodVO;

public class HistoryModel extends SimpleObservable
{
	
	private ArrayList<UsagePeriodVO> data ;
	
	private UsagePeriodVO[] years ;
	private UsagePeriodVO[] seasons ;
	private UsagePeriodVO[] months ;
	private UsagePeriodVO[] weeks ;
	private UsagePeriodVO[] days ;
	
	
	// ACCESSOR METHODS
	
	
	public ArrayList<UsagePeriodVO> getData( )
	{
		return data ;
	}
	
	public void setData( ArrayList<UsagePeriodVO> arr )
	{
		data = arr ;
		notifyListeners( this ) ;
	}
	
	
	// METHODS
	

	
}
