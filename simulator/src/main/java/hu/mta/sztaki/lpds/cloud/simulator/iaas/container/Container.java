package hu.mta.sztaki.lpds.cloud.simulator.iaas.container;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.MaxMinConsumer;

/**
 * Represents a container in the DISSECT-CF-Fog simulator.
 *
 * A container is created from a {@link ContainerImage}. Its actual resource
 * allocation and execution will be handled separately.
 */
public class Container extends MaxMinConsumer {

    /**
     * Possible lifecycle states of a container.
     */
    public enum State {

        /**
         * The container object exists but has not been started yet.
         */
        CREATED,

        /**
         * The container image is being transferred to the host.
         */
        PULLING,

        /**
         * The container is performing its startup processing.
         */
        STARTING,

        /**
         * The container is running and can execute workloads.
         */
        RUNNING,

        /**
         * The container has been terminated and cannot execute workloads.
         */
        DESTROYED
    }

    /**
     * Image used by this container.
     */
    private final ContainerImage image;

    /**
     * Current lifecycle state of the container.
     */
    private State state = State.CREATED;

    /**
     * Creates a new container based on the given image.
     *
     * @param image container image used by this container
     */
    public Container(final ContainerImage image) {
        super(0);

        if (image == null) {
            throw new IllegalStateException(
                    "Cannot create a container without a container image");
        }

        this.image = image;
    }

    /**
     * Returns the container image.
     *
     * @return image used by the container
     */
    public ContainerImage getImage() {
        return image;
    }

    /**
     * Returns the current lifecycle state.
     *
     * @return current state of the container
     */
    public State getState() {
        return state;
    }

    /**
     * Changes the current state of the container.
     *
     * This method has package visibility intentionally so lifecycle changes
     * can later be controlled by the container management classes.
     *
     * @param newState new lifecycle state
     */
    void setState(final State newState) {
        if (newState == null) {
            throw new IllegalArgumentException(
                    "Container state cannot be null");
        }

        state = newState;
    }

    @Override
    public String toString() {
        return "Container(image:"
                + image.id
                + " state:"
                + state
                + ")";
    }
}