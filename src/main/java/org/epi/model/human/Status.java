package org.epi.model.human;

import javafx.scene.paint.Color;

/** All the status types which individuals in the population can have and their corresponding color.*/
public enum Status {
    HEALTHY (Color.DODGERBLUE),
    SICK(Color.CRIMSON),
    RECOVERED (Color.DARKORCHID);

    /** The color indicating the status type in the simulation view.*/
    public final Color color;

    /**
     * Status type constructor.
     *
     * @param color the color indicating the status type in the simulation view
     */
    Status(Color color) {
        this.color = color;
    }

}
