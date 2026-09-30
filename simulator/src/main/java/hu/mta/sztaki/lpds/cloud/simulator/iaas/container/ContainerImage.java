package hu.mta.sztaki.lpds.cloud.simulator.iaas.container;

import hu.mta.sztaki.lpds.cloud.simulator.io.StorageObject;

/**
 * Represents a container image that can be stored in repositories
 * and used when starting containers.
 */
public class ContainerImage extends StorageObject {

    /**
     * Processing instructions required to start a container
     * from this image.
     */
    private final double startupProcessing;

    /**
     * Creates a container image with a fixed size.
     *
     * @param id identifier of the image
     * @param size image size in bytes
     * @param startupProcessing processing instructions needed during startup
     */
    public ContainerImage(final String id,
                          final long size,
                          final double startupProcessing) {

        super(id, size, false);

        if (startupProcessing < 0) {
            throw new IllegalArgumentException(
                    "Container startup processing cannot be negative");
        }

        this.startupProcessing = startupProcessing;
    }

    /**
     * Returns the processing instructions required
     * to start a container from this image.
     *
     * @return startup processing requirement
     */
    public double getStartupProcessing() {
        return startupProcessing;
    }

    /**
     * Creates a copy of this container image with a new identifier.
     *
     * @param newId identifier of the copied image
     * @return copied container image
     */
    @Override
    public ContainerImage newCopy(final String newId) {
        return new ContainerImage(newId, size, startupProcessing);
    }

    @Override
    public String toString() {
        return "ContainerImage("
                + super.toString()
                + " startupProcessing:"
                + startupProcessing
                + ")";
    }
}