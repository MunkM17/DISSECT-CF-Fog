package hu.mta.sztaki.lpds.cloud.simulator.iaas.container;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ConstantConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ResourceConstraints;

/**
 * Represents a resource allocation assigned to a container inside
 * a virtual machine.
 */
public class ContainerAllocation {

    /**
     * Virtual machine hosting the container.
     */
    private final VirtualMachine host;

    /**
     * Resources allocated to the container.
     */
    private final ResourceConstraints allocated;

    /**
     * Creates a new container resource allocation.
     *
     * @param host virtual machine hosting the container
     * @param resources resources assigned to the container
     */
    ContainerAllocation(final VirtualMachine host,
                        final ResourceConstraints resources) {

        if (host == null) {
            throw new IllegalArgumentException(
                    "Container allocation requires a host virtual machine");
        }

        if (resources == null) {
            throw new IllegalArgumentException(
                    "Container allocation requires resource constraints");
        }

        if (resources.getRequiredCPUs() <= 0) {
            throw new IllegalArgumentException(
                    "Container CPU allocation must be positive");
        }

        if (resources.getRequiredProcessingPower() <= 0) {
            throw new IllegalArgumentException(
                    "Container processing power must be positive");
        }

        if (resources.getRequiredMemory() < 0) {
            throw new IllegalArgumentException(
                    "Container memory allocation cannot be negative");
        }

        this.host = host;
        this.allocated = new ConstantConstraints(resources);
    }

    /**
     * Returns the virtual machine hosting this allocation.
     *
     * @return host virtual machine
     */
    public VirtualMachine getHost() {
        return host;
    }

    /**
     * Returns the resources assigned to the container.
     *
     * @return allocated resources
     */
    public ResourceConstraints getAllocated() {
        return allocated;
    }

    @Override
    public String toString() {
        return "ContainerAllocation("
                + allocated
                + ")";
    }
}