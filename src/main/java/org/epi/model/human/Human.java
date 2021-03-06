package org.epi.model.human;

import org.epi.model.world.Location;
import org.epi.util.Error;

import java.util.Objects;
import java.util.Set;

/** A simple model of a human.
 * The main actors in the simulator.*/
public class Human {

    /** The location of this human.*/
    private Location location;

    /** The graphical view of this human.*/
    private final Model model;

    /** The health status of this human.*/
    private Status status;

    /** The immune system protecting this human.*/
    private final ImmuneSystem immuneSystem;

    /** The pathogen this human is sick with.*/
    private Pathogen pathogen;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a human at a given location.
     *
     * @param behaviour the behaviour of this human
     * @param location the location of this human
     * @throws NullPointerException if the given parameter is null
     */
    public Human(Location location, Behaviour behaviour) {
        Behaviour.requireNonNull(behaviour);
        Objects.requireNonNull(location, Error.getNullMsg("location"));

        this.immuneSystem = new ImmuneSystem(this);

        this.status = Status.HEALTHY;

        this.model = new Model(this, behaviour);

        this.location = location;
        location.getPopulation().add(this);
    }

    //---------------------------- Helper methods ----------------------------

    /**
     * Check if this human is sick with a pathogen.
     *
     * @return true if this human is sick, otherwise false
     */
    public boolean isSick() {
        return pathogen != null;
    }

    /**
     * Update the health status of this human.
     */
    public void status() {
        if (immuneSystem.isImmune()) {
            status = Status.RECOVERED;
        } else if (isSick()) {
            status = Status.SICK;
        } else {
            status = Status.HEALTHY;
        }
    }

    /**
     * Get all humans which in this human's vicinity.
     */
    public Set<Human> getNearby() {
        return location.getSpatialHash().getNearby(this);
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * If this human is sick, spread the pathogen and let the pathogen live.
     *
     * @param elapsedSeconds the number of seconds elapsed since the pathogen was last updated
     * @throws IllegalArgumentException if the given parameter is negative
     */
    public void pathogen(double elapsedSeconds) {
        Error.nonNegativeCheck(elapsedSeconds);
        pathogen.infect();
        pathogen.live(elapsedSeconds);
    }

    /**
     * Let the immune system live and defend this human.
     *
     * @param elapsedSeconds the number of seconds elapsed since the immune system was last updated
     * @throws IllegalArgumentException if the given parameter is negative
     */
    public void immuneSystem(double elapsedSeconds) {
        Error.nonNegativeCheck(elapsedSeconds);

        immuneSystem.live(elapsedSeconds);

        if (isSick()) {
            immuneSystem.defend();
        }
    }

    /**
     * Set the correct fill for the model and move the model.
     *
     * @param elapsedSeconds the number of seconds elapsed since this human's model was last updated
     * @throws IllegalArgumentException if the given parameter is negative
     */
    public void model(double elapsedSeconds) {
        Error.nonNegativeCheck(elapsedSeconds);
        status();
        model.fill();
        model.move(elapsedSeconds);
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link #immuneSystem}.
     *
     * @return {@link #immuneSystem}
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Setter for {@link #location}.
     * Removes the human from their previous location's population and
     * adds the human to the given location's population, given that neither are null.
     *
     * @param location {@link #location}
     */
    public void setLocation(Location location) {
        if (this.location != null) {
            this.location.getPopulation().remove(this);
        }

        this.location = location;

        if (location != null) {
            location.getPopulation().add(this);
        }
    }

    /**
     * Getter for {@link #model}.
     *
     * @return {@link #model}
     */
    public Model getModel() {
        return model;
    }

    /**
     * Getter for {@link #status}.
     *
     * @return {@link #status}
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Getter for {@link #immuneSystem}.
     *
     * @return {@link #immuneSystem}
     */
    public ImmuneSystem getImmuneSystem() {
        return immuneSystem;
    }

    /**
     * Getter for {@link #pathogen}.
     *
     * @return {@link #pathogen}
     */
    public Pathogen getPathogen() {
        return pathogen;
    }

    /**
     * Setter for {@link #pathogen}.
     *
     * @param pathogen {@link #pathogen}
     */
    public void setPathogen(Pathogen pathogen) {
        this.pathogen = pathogen;

        if (pathogen != null) {
            pathogen.setHost(this);
        }
    }

}
