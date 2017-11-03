package org.flippers.registry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class ObjectRegistryTest {

    private ObjectRegistry registry;

    @Before
    public void setUp() throws Exception {
        this.registry = ObjectRegistry.getInstance();
        this.registry.clear();
    }

    @Test
    public void shouldRegisterAnObject() throws Exception {
        MockInnerClass testObject = new MockInnerClass("yellow");
        registry.register(testObject);

        MockInnerClass retrievedClass = registry.instanceOf(MockInnerClass.class);

        assertSame(testObject, retrievedClass);
    }

    @Test(expected = RuntimeException.class)
    public void shouldAllowOnlyOneObjectOfATypeInRegistry() throws Exception {
        MockInnerClass yellow = new MockInnerClass("yellow");
        MockInnerClass red = new MockInnerClass("red");
        registry.register(yellow);
        registry.register(red);
    }

    @Test
    public void shouldAllowReregistrationOfSameObject() throws Exception {
        MockInnerClass testObject = new MockInnerClass("yellow");
        registry.register(testObject);
        registry.register(testObject);

        MockInnerClass retrievedClass = registry.instanceOf(MockInnerClass.class);

        assertSame(testObject, retrievedClass);
    }

    class MockInnerClass {

        private String testVar;

        public MockInnerClass(String testVar) {
            this.testVar = testVar;
        }
    }
}