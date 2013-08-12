package au.com.planetinnovation.marconi.interfaces;

public interface IEasyObservable<T> 
{
	void addListener( IOnChangeListener<T> listener ) ;
	void removeListener( IOnChangeListener<T> listener ) ;
}
