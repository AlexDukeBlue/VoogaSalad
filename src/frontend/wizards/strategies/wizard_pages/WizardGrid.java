package frontend.wizards.strategies.wizard_pages;

import java.util.HashMap;
import java.util.Map;

import backend.grid.CoordinateTuple;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class WizardGrid {

	private Map<CoordinateTuple, Rectangle> map;
	private Pane pane;
	
	protected WizardGrid(int n, Parent node){
		map = new HashMap<CoordinateTuple, Rectangle>();
		int maxX = (int) node.getBoundsInLocal().getMaxX();
		int maxY = (int) node.getBoundsInLocal().getMaxY();
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				map.put(new CoordinateTuple(i, j), new Rectangle(maxX/n, maxY/n, i * maxX, j * maxY));
			}
		}
		initialize();
	}

	private void initialize() {
		map.keySet().stream().forEach(e -> pane.getChildren().add(map.get(e)));
	}
	
	
}
