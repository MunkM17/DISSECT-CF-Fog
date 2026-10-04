package hu.mta.sztaki.lpds.cloud.simulator.iaas.container;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the basic container model.
 */
public class ContainerTest {

    @Test
    public void containerShouldStartInCreatedState() {
        ContainerImage image =
                new ContainerImage("test-image", 1000, 100);

        Container container = new Container(image);

        assertEquals(Container.State.CREATED, container.getState());
    }

    @Test
    public void containerShouldStoreItsImage() {
        ContainerImage image =
                new ContainerImage("test-image", 1000, 100);

        Container container = new Container(image);

        assertSame(image, container.getImage());
    }

    @Test
    public void containerShouldRejectNullImage() {
        assertThrows(
                IllegalStateException.class,
                () -> new Container(null));
    }
}