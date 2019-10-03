package com.qatestlab.olegvolik.controller.form;

import com.qatestlab.olegvolik.domain.MyEventType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/** The class which describes the form for new events.
 * This form is transferred from a controller to view in empty state
 * before view template rendering and then turns back to controller
 * after user fills it up with the required information.
 * The information retrieved from the form is processed by controller.
 * @author Oleg Volik
 */
@Data
public class EventForm {

    //New event attributes
    @NotNull(message = "Name must be not empty!!!")
    @NotBlank(message = "Name must be not empty!!!")
    private String name;

    private MyEventType type;

    @NotNull(message = "Date must be not empty!!!")
    private String date;

    @NotNull(message = "Time must be not empty!!!")
    private String startTime;
}
