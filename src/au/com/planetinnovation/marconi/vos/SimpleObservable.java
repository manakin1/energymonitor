package au.com.planetinnovation.marconi.vos ;

import java.util.ArrayList ;

import au.com.planetinnovation.marconi.interfaces.IEasyObservable;
import au.com.planetinnovation.marconi.interfaces.IOnChangeListener;

public class SimpleObservable<T> implements IEasyObservable<T> 
{
	
	private final ArrayList<IOnChangeListener<T>> listeners = new ArrayList<IOnChangeListener<T>>( ) ;
	
	
	public void addListener( IOnChangeListener<T> listener ) 
	{
		synchronized( listeners ) 
		{
			listeners.add( listener ) ;
		}
	}
	
	public void removeListener( IOnChangeListener<T> listener ) 
	{
		synchronized( listeners ) 
		{
			listeners.remove( listener ) ;
		}
	}
	
	protected void notifyListeners( final T model ) 
	{
		synchronized( listeners ) 
		{
			for( IOnChangeListener<T> listener:listeners ) 
			{
				listener.onChange( model ) ;
			}
		}
	}
	
}