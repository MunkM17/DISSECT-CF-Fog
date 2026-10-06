package hu.mta.sztaki.lpds.cloud.simulator.iaas.container;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ConstantConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
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
    private VirtualMachine createVirtualMachine() {
        VirtualAppliance appliance =
                new VirtualAppliance(
                        "test-va",
                        100,
                        0,
                        false,
                        1000);

        return new VirtualMachine(appliance);
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
    @Test
    public void containerShouldAcceptAllocation() {
        ContainerImage image =
                new ContainerImage("test-image", 1000, 100);

        Container container = new Container(image);

        VirtualMachine vm = createVirtualMachine();

        ConstantConstraints resources =
                new ConstantConstraints(
                        2,
                        1000,
                        2_000_000_000L);

        ContainerAllocation allocation =
                new ContainerAllocation(vm, resources);

        container.setAllocation(allocation);

        assertSame(allocation, container.getAllocation());

        assertEquals(
                2000.0,
                container.getPerTickProcessingPower(),
                0.0001);
    }
    @Test
    public void containerShouldReleaseAllocation() {
        ContainerImage image =
                new ContainerImage("test-image", 1000, 100);

        Container container = new Container(image);

        VirtualMachine vm = createVirtualMachine();

        ConstantConstraints resources =
                new ConstantConstraints(
                        1,
                        1000,
                        1_000_000_000L);

        ContainerAllocation allocation =
                new ContainerAllocation(vm, resources);

        container.setAllocation(allocation);
        container.releaseAllocation();

        assertEquals(null, container.getAllocation());

        assertEquals(
                0.0,
                container.getPerTickProcessingPower(),
                0.0001);
    }
}