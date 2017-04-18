package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import backend.unit.ModifiableUnit;

public class GenericTests {

//    @Test
//    public void ObservableQueueTest() {
//        ObservableQueue<String> queue1 = new ObservableQueue<>();
//        ObservableQueue<String> queue2 = new ObservableQueue<>();
//        String string = "Test";
//        queue1.passTo(queue2);
//        queue1.add(string);
//        assertEquals(string, queue2.poll());
//    }
    
    @Test
    public void ModifiableUnitInstantiationTest(){
    	assertEquals("Name", new ModifiableUnit("Name").getName());
    }

}
