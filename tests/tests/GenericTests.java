package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import backend.unit.ModifiableUnit;

public class GenericTests {

	@Test
	public void ModifiableUnitInstantiationTest() {
		assertEquals("Name", new ModifiableUnit("Name").getName());
	}

}
