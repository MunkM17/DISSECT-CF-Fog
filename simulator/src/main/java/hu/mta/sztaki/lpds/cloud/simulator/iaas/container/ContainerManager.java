package hu.mta.sztaki.lpds.cloud.simulator.iaas.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ResourceConstraints;

/**
 * Manages containers and their resource allocations inside a virtual machine.
 */
public class ContainerManager {

    /**
     * Virtual machine hosting the managed containers.
     */
    private final VirtualMachine host;

    /**
     * Containers currently managed by this manager.
     */
    private final List<Container> containers = new ArrayList<>();

    /**
     * Number of CPU cores currently allocated to containers.
     */
    private double usedCpus = 0;

    /**
     * Total processing power currently allocated to containers.
     */
    private double usedProcessingPower = 0;

    /**
     * Memory currently allocated to containers.
     */
    private long usedMemory = 0;

    /**
     * Creates a container manager for a virtual machine.
     *
     * @param host virtual machine hosting the containers
     */
    public ContainerManager(final VirtualMachine host) {
        if (host == null) {
            throw new IllegalArgumentException(
                    "Container manager requires a host virtual machine");
        }

        if (host.getResourceAllocation() == null) {
            throw new IllegalStateException(
                    "Host virtual machine has no resource allocation");
        }

        this.host = host;
    }

    /**
     * Returns the host virtual machine.
     *
     * @return host virtual machine
     */
    public VirtualMachine getHost() {
        return host;
    }

    /**
     * Returns the currently managed containers.
     *
     * @return unmodifiable list of containers
     */
    public List<Container> getContainers() {
        return Collections.unmodifiableList(containers);
    }

    /**
     * Checks whether the requested resources fit into the host VM.
     *
     * @param resources requested container resources
     * @return true if the resources can be allocated
     */
    public boolean canAllocate(final ResourceConstraints resources) {
        validateResources(resources);

        ResourceConstraints hostResources =
                host.getResourceAllocation().allocated;

        if (resources.getRequiredProcessingPower()
                > hostResources.getRequiredProcessingPower()) {
            return false;
        }

        double freeCpus =
                hostResources.getRequiredCPUs() - usedCpus;

        double freeProcessingPower =
                hostResources.getTotalProcessingPower()
                        - usedProcessingPower;

        long freeMemory =
                hostResources.getRequiredMemory() - usedMemory;

        return resources.getRequiredCPUs() <= freeCpus
                && resources.getTotalProcessingPower()
                <= freeProcessingPower
                && resources.getRequiredMemory() <= freeMemory;
    }

    /**
     * Creates a new container and assigns resources to it.
     *
     * @param image container image
     * @param resources requested resources
     * @return newly created container
     */
    public Container createContainer(final ContainerImage image,
                                     final ResourceConstraints resources) {

        if (image == null) {
            throw new IllegalArgumentException(
                    "Container image cannot be null");
        }

        if (!canAllocate(resources)) {
            throw new IllegalStateException(
                    "Not enough resources on the host virtual machine");
        }

        ContainerAllocation allocation =
                new ContainerAllocation(host, resources);

        Container container = new Container(image);
        container.setAllocation(allocation);

        containers.add(container);

        usedCpus += resources.getRequiredCPUs();
        usedProcessingPower += resources.getTotalProcessingPower();
        usedMemory += resources.getRequiredMemory();

        return container;
    }

    /**
     * Destroys a managed container and releases its resources.
     *
     * @param container container to destroy
     */
    public void destroyContainer(final Container container) {
        if (container == null || !containers.contains(container)) {
            throw new IllegalArgumentException(
                    "Container is not managed by this manager");
        }

        ResourceConstraints resources =
                container.getAllocation().getAllocated();

        usedCpus -= resources.getRequiredCPUs();
        usedProcessingPower -= resources.getTotalProcessingPower();
        usedMemory -= resources.getRequiredMemory();

        container.releaseAllocation();
        container.setState(Container.State.DESTROYED);

        containers.remove(container);
    }

    /**
     * Validates a container resource request.
     *
     * @param resources resources to validate
     */
    private void validateResources(final ResourceConstraints resources) {
        if (resources == null) {
            throw new IllegalArgumentException(
                    "Container resources cannot be null");
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
    }
}