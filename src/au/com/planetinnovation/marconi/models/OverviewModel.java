package au.com.planetinnovation.marconi.models;

import java.util.ArrayList;

import au.com.planetinnovation.marconi.vos.SimpleObservable;


public class OverviewModel extends SimpleObservable
{

	private ArrayList<Double> totalUsage = new ArrayList<Double>( ) ;
	private ArrayList<Double> averageUsage = new ArrayList<Double>( ) ;
	
	
	// ACCESSOR METHODS
	
	
	public ArrayList<Double> getTotalUsage( )
	{
		return totalUsage ;
	}
	
	public void setTotalUsage( int index, double usage )
	{
		totalUsage.add( index, usage ) ;
	}
	
	public ArrayList<Double> getAverageUsage( )
	{
		return averageUsage ;
	}
	
	public void setAverageUsage( int index, double usage )
	{
		averageUsage.add( index, usage ) ;
	}
	
	
	// METHODS
	

	
}
