package frontend.factory.statistics;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import backend.unit.Unit;
import backend.unit.properties.UnitStat;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.interfaces.detailpane.DetailPaneExternal;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public class StatsPane extends ClickableUIComponent {
	private HBox fullpane;
	private ScrollPane scrollpane;
	private Map<String, List<TextField>> unitStats;
	private Unit currentUnit;
	private boolean authorMode;
	private ResourceBundle resources;
	
	
	public StatsPane(Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		resources = ResourceBundle.getBundle("frontend/factory/statistics/resources");
		fullpane = new HBox();
		getPolyglot().setOnLanguageChange(change -> {
			if (currentUnit != null) {
				getContent(currentUnit);
			}
		});
		//setAuthorMode();
	}

	@Override
	public Region getNode() {
		return fullpane;
	}
	
	private void getStats(){
		
	}

	
	private void getContent(Unit unit){
		//for (UnitStat stat : unit.getUnitStats()){
			//System.out.println(stat);
//			fullpane.getChildren().add((Node) stat);
			fullpane.getChildren().add(new Rectangle(20,20));
		//}
	}
	
	
//	public void setAuthorMode() {
//		authorMode = false;
//	}
//
//	
//	public void setPlayMode() {
//		authorMode = true;
//	}
	


}
