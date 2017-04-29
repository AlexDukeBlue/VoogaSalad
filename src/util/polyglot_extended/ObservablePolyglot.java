package util.polyglot_extended;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import util.polyglot.Polyglot;
import util.polyglot.PolyglotException;

public class ObservablePolyglot extends Polyglot {
	public static final EventType<Event> LANGUAGE_CHANGE_EVENT = new EventType<>("Polyglot is changing languages.");

	private Collection<EventHandler<Event>> languageChangeHandlers;
	private String language;

	public ObservablePolyglot(String APIKey, String pathToResourceBundle) throws PolyglotException {
		super(APIKey, pathToResourceBundle);
		initialize();
	}

	public ObservablePolyglot(String APIKey, String pathToResourceBundle, Locale locale) throws PolyglotException {
		super(APIKey, pathToResourceBundle, locale);
		initialize();
	}

	public void setOnLanguageChange(EventHandler<Event> eventHandler) {
		languageChangeHandlers.clear();
		addLanguageChangeHandler(eventHandler);
	}
	
	public void addLanguageChangeHandler(EventHandler<Event> eventHandler){
		if (!languageChangeHandlers.contains(eventHandler)){
			languageChangeHandlers.add(eventHandler);
		}
	}
	
	public void removeLanguageChangeHandler(EventHandler<Event> eventHandler){
		if (languageChangeHandlers.contains(eventHandler)){
			languageChangeHandlers.remove(eventHandler);
		}
	}

	@Override
	public void setLanguage(String language) throws PolyglotException {
		super.setLanguage(language);
		this.language = language;
		languageChangeHandlers.stream().forEach(handler -> handler.handle(new Event(LANGUAGE_CHANGE_EVENT)));
	}

	public String getLanguage() {
		return language;
	}

	private void initialize() {
		languageChangeHandlers = new ArrayList<>();
	}

}
