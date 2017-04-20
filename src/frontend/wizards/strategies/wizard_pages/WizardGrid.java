package frontend.wizards.strategies.wizard_pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import backend.grid.CoordinateTuple;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WizardGrid {

	private Map<Rectangle, CoordinateTuple> map;
	private Collection<CoordinateTuple> coordinates;
	private Pane pane;

	protected WizardGrid(int n, Node node){
		map = new HashMap<Rectangle, CoordinateTuple>();
		coordinates = new ArrayList<CoordinateTuple>();
		int maxX = (int) node.getBoundsInLocal().getMaxX();
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				Rectangle rect = new Rectangle(((maxX/n) * i) + 100, ((maxX/n) * j) + 100, maxX/n, maxX/n);
				rect.setFill(Color.TRANSPARENT);
				CoordinateTuple coordinate = new CoordinateTuple(i, j);
				map.put(rect, coordinate);
				rect.setStroke(Color.BLACK);
				rect.setOnMouseClicked(event -> {
				if(rect.getFill().equals(Color.TRANSPARENT)){ 
					rect.setFill(Color.GREEN);
					coordinates.add(coordinate);
					System.out.println(coordinates.toString());
				}else{
					rect.setFill(Color.TRANSPARENT);
					coordinates.remove(coordinate);
					System.out.println(coordinates.toString());
				}
				});
			}
		}
		initialize();
	}

	private void initialize() {
		pane = new Pane();
		map.keySet().stream().forEach(e -> pane.getChildren().add(e));
	}

	public Pane getPane() {
		return pane;
	}
	
	public List<CoordinateTuple> getCoordinates(){
		return map.keySet().stream().map(e -> map.get(e)).collect(Collectors.toList());
	}

}
