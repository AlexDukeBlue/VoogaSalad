package frontend;

import frontend.util.SelectableUIComponent;

/**
 * @author Created by th174 on 4/22/17.
 */
public class AuthoringClickHandler extends ClickHandler {
	@Override
	protected void triggerAction(SelectableUIComponent selectedComponent, ClickableUIComponent actionTarget, Object additionalInfo) {
		selectedComponent.actInAuthoringMode(actionTarget, additionalInfo);
	}
}
