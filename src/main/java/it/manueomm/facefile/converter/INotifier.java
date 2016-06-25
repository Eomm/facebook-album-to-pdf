package it.manueomm.facefile.converter;

public interface INotifier {
	
	public enum Type{INFO, ERROR, MESSAGE};
	
	public void update(Type notifyType, String message);

}
