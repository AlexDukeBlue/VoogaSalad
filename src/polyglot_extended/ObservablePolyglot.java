package polyglot_extended;

import java.util.Locale;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import polyglot.Polyglot;
import polyglot.PolyglotException;

public class ObservablePolyglot extends Polyglot {
	public static final EventType<Event> LANGUAGE_CHANGE_EVENT = new EventType<>("Polyglot is changing languages.");

	private EventHandler<Event> languageChangeHandler;
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
		languageChangeHandler = eventHandler;
	}

	@Override
	public void setLanguage(String language) throws PolyglotException {
		super.setLanguage(language);
		this.language = language;
		languageChangeHandler.handle(new Event(LANGUAGE_CHANGE_EVENT));
	}

	public String getLanguage() {
		return language;
	}

	private void initialize() {
		languageChangeHandler = event -> {
		};
	}

}
