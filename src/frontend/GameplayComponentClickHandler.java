package frontend;

import frontend.util.SelectableUIComponent;

/**
 * @author Created by th174 on 4/22/17.
 */
public class GameplayComponentClickHandler extends ComponentClickHandler {
	@Override
	protected void triggerAction(SelectableUIComponent selectedComponent, ClickableUIComponent actionTarget, Object additionalInfo) {
		selectedComponent.actInGameplayMode(actionTarget, additionalInfo);
	}
}
