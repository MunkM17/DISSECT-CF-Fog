package hu.mta.sztaki.lpds.cloud.simulator.iaas.container;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ConstantConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for container resource allocations.
 */
public class ContainerAllocationTest {

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
    public void allocationShouldStoreHostAndResources() {
        VirtualMachine vm = createVirtualMachine();

        ConstantConstraints resources =
                new ConstantConstraints(
                        2,
                        1000,
                        2_000_000_000L);

        ContainerAllocation allocation =
                new ContainerAllocation(vm, resources);

        assertSame(vm, allocation.getHost());

        assertEquals(
                2,
                allocation.getAllocated().getRequiredCPUs());

        assertEquals(
                1000,
                allocation.getAllocated().getRequiredProcessingPower());

        assertEquals(
                2_000_000_000L,
                allocation.getAllocated().getRequiredMemory());
    }

    @Test
    public void allocationShouldRejectNullHost() {
        ConstantConstraints resources =
                new ConstantConstraints(
                        1,
                        1000,
                        1_000_000_000L);

        assertThrows(
                IllegalArgumentException.class,
                () -> new ContainerAllocation(null, resources));
    }

    @Test
    public void allocationShouldRejectNullResources() {
        VirtualMachine vm = createVirtualMachine();

        assertThrows(
                IllegalArgumentException.class,
                () -> new ContainerAllocation(vm, null));
    }
}