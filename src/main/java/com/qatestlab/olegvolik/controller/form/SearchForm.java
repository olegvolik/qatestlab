package com.qatestlab.olegvolik.controller.form;

import com.qatestlab.olegvolik.domain.MyEventType;
import lombok.Data;

/** The class which describes the form for events search.
 * This form is transferred from a controller to view in empty state
 * before view template rendering and then turns back to controller
 * after user fills it up with the required information.
 * The information retrieved from the form is processed by controller.
 * @author Oleg Volik
 */
@Data
public class SearchForm {

    //Search attributes
    private String name;
    private MyEventType type;
    private String date;
}
