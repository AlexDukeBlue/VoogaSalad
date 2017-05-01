package tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;

import backend.cell.ModifiableCell;
import backend.cell.ModifiableTerrain;
import backend.cell.Terrain;
import backend.grid.CoordinateTuple;
import backend.grid.GridPattern;
import backend.grid.Shape;
import backend.player.Player;
import backend.player.Team;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.unit.properties.ModifiableUnitStat;

public class GenericTests {

	/**
	 * Test the instantiation of a ModifiableUnit
	 */
	@Test
	public void ModifiableUnitInstantiationTest() {
		assertEquals("Name", new ModifiableUnit("Name").getName());
	}

	/**
	 * Test copying the name of a ModifiableUnit
	 */
	@Test
	public void ModifiableUnitCopyTest() {
		assertEquals(true,
				new ModifiableUnit("Name", Collections.emptyList(), null,
						new GridPattern("name", "description", "imgpath", Collections.emptyList()),
						new GridPattern("name", "description", "imgpath", Collections.emptyList()), new HashMap<>(),
						Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
						Collections.emptyList(), "description", "imgpath").addUnitStats(ModifiableUnitStat.HITPOINTS)
								.addUnitStats(ModifiableUnitStat.MOVEPOINTS).copy().getName().startsWith("Name"));
	}

	/**
	 * Test adding stats of a ModifiableUnit
	 */
	@Test
	public void ModifiableUnitStatAddTest() {
		assertEquals(3,
				new ModifiableUnit("Name", Collections.emptyList(), null,
						new GridPattern("name", "description", "imgpath", Collections.emptyList()),
						new GridPattern("name", "description", "imgpath", Collections.emptyList()), new HashMap<>(),
						Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
						Collections.emptyList(), "description", "imgpath").addUnitStats(ModifiableUnitStat.HITPOINTS)
								.addUnitStats(ModifiableUnitStat.MOVEPOINTS)
								.addUnitStats(ModifiableUnitStat.ABILITYPOINTS).getUnitStats().size());
	}

	/**
	 * Test copying stats of a ModifiableUnit
	 */
	@Test
	public void ModifiableUnitStatCopyTest() {
		assertEquals(3,
				new ModifiableUnit("Name", Collections.emptyList(), null,
						new GridPattern("name", "description", "imgpath", Collections.emptyList()),
						new GridPattern("name", "description", "imgpath", Collections.emptyList()), new HashMap<>(),
						Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
						Collections.emptyList(), "description", "imgpath").addUnitStats(ModifiableUnitStat.HITPOINTS)
								.addUnitStats(ModifiableUnitStat.MOVEPOINTS)
								.addUnitStats(ModifiableUnitStat.ABILITYPOINTS).copy().getUnitStats().size());
	}

	/**
	 * Test changing the name of a ModifiableUnit
	 */
	@Test
	public void ModifiableUnitNameChangeTest() {
		assertEquals("Second Name", new ModifiableUnit("First Name").setName("Second Name").getName());
	}

	/**
	 * Test creating gridpattern
	 */
	@Test
	public void gridPatternInstantiationTest() {
		GridPattern gridPattern = new GridPattern("name", "description", "imgpath", Arrays
				.asList(new CoordinateTuple(0, 0, 0), new CoordinateTuple(1, 2, 3), new CoordinateTuple(1, 1, 1)));
		assertEquals(3, gridPattern.getCoordinates().size());
		assertEquals("name", gridPattern.getName());
		assertEquals("description", gridPattern.getDescription());
		assertEquals("imgpath", gridPattern.getImgPath());
	}

	/**
	 * Test creating teams
	 */
	@Test
	public void teamTest() {
		Player p = new Player("p", null, null);
		Team t1 = new Team("t1", "This is a team", "BLUE", "imgPath", p);
		Unit unit = new ModifiableUnit("Unit");
		unit.setTeam(t1);
		assertEquals(unit.getTeam().get(), t1);
		assertEquals(t1.getColorString(), "BLUE");
	}
	/**
	 * Test creating modifiable cell
	 */
	@Test
	public void modifiableCellTest() {
		ModifiableCell mc = new ModifiableCell(new CoordinateTuple(5, 5), Shape.SQUARE,
				(Terrain) new ModifiableTerrain("Terrain"));
		Unit u = new ModifiableUnit("Unit");
		mc.addOccupants(u);
		assertEquals(mc.getOccupantByName("Unit"), u);
		Unit u2 = new ModifiableUnit("Unit2");
		mc.addOccupants(u2);
		mc.removeOccupants(u);
		assertEquals(mc.getName(), "Terrain");
		assertEquals((mc.getOccupants()).toString(), "[" + u2.getName() + "]");
		assertEquals(mc.getShape(), Shape.SQUARE);
	}

}
