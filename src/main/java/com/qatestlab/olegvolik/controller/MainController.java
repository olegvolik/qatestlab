package com.qatestlab.olegvolik.controller;

import com.qatestlab.olegvolik.controller.form.EventForm;
import com.qatestlab.olegvolik.controller.form.SearchForm;
import com.qatestlab.olegvolik.domain.Event;
import com.qatestlab.olegvolik.domain.MyEventType;
import com.qatestlab.olegvolik.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** The controller class for handling general HTTP requests
 * @author Oleg Volik
 */
@Controller
public class MainController {

    // The basic service for interaction with domain objects
    @Autowired
    private EventService eventService;

    /** Handling events list page requests
     *
     * @param model - Model object for transferring data between view and controller
     * @return view template name
     */
    @GetMapping(value = "/eventslist")
    public String eventsList (Model model) {
        model.addAttribute("events", eventService.getList());
        System.out.println(eventService.getList());
        return "eventslist";
    }

    /** Handling new event creation requests
     *
     * @param model - Model object for transferring data between view and controller
     * @return view template name
     */
    @GetMapping(value = "/newevent")
    public String newEvent (Model model) {
        model.addAttribute("eventForm", new EventForm());
        return "newevent";
    }

    /** Processing new event creation
     *
     * @param model - Model object for transferring data between view and controller
     * @param eventForm - contains general event data
     * @return
     */
    @PostMapping(value = "/newevent")
    public String addNewEvent (Model model, @ModelAttribute @Valid EventForm eventForm, Errors errors) {

        // Checking validation errors
        if (errors.hasErrors()) {
            return "newevent";
        }

        // Fetching new event data
        String name = eventForm.getName();
        MyEventType type = eventForm.getType();
        LocalDate date = LocalDate.parse(eventForm.getDate());
        LocalTime startTime = LocalTime.parse(eventForm.getStartTime());

        // Creating new event object and persisting it to the DB
        Event event = new Event();
        event.setName(name);
        event.setType(type);
        event.setDate(date);
        event.setStartTime(startTime);

        eventService.save(event);

        return "redirect:/eventslist";
    }

    /** Handling requests for event removal
     *
     * @param name - event name transferred as URI path variable
     * @return view template name
     */
    @PostMapping(value  = "/evdelete/{evName}")
    public String deleteEvent (Model model, @PathVariable("evName") String name) {
        eventService.delete(eventService.findByName(name));
        return "redirect:/eventslist";
    }

    /** Handling requests for event editing page
     *
     * @param model - Model object for transferring data between view and controller
     * @param evName - event name transferred as request parameter
     * @return view template name
     */
    @GetMapping(value = "/editevent")
    public String showEditForm (Model model, @RequestParam("evName") String evName) {
        model.addAttribute("currentName", evName);
        model.addAttribute("updForm", new EventForm());
        return "editevent";
    }

    /** Processing event modification requests.
     * Updating event fields and saving changes to the DB.
     *
     * @param evName - event name transferred as request parameter
     * @param updForm - contains updated event fields
     * @return
     */
    @PostMapping(value = "/editevent")
    public String processChanges (@RequestParam("evName") String evName,
                                  @ModelAttribute("updForm") EventForm updForm) {

        Event updatedEvent = new Event();
        Event currentEvent = eventService.findByName(evName);

         /* Fetching data from filled form fields and setting the respective
         values for the new object fields. Empty field value is considered as
         to keep current Event field value */
        updatedEvent.setId(currentEvent.getId());
        if (updForm.getName() != null && !updForm.getName().isEmpty()) {
            updatedEvent.setName(updForm.getName());
        } else {
            updatedEvent.setName(currentEvent.getName());
        }
        if (updForm.getType() != null) {
            updatedEvent.setType(updForm.getType());
        } else {
            updatedEvent.setType(currentEvent.getType());
        }
        try {
            updatedEvent.setDate(LocalDate.parse(updForm.getDate()));
        }
        catch (DateTimeParseException e) {
            updatedEvent.setDate(currentEvent.getDate());
        }
        try {
            updatedEvent.setStartTime(LocalTime.parse(updForm.getStartTime()));
        }
        catch (DateTimeParseException e) {
            updatedEvent.setStartTime(currentEvent.getStartTime());
        }
        /* Saving new event object with the previous object's ID so that
        * to overwrite the existing one*/
        updatedEvent.setCreatedAt(LocalDateTime.now());
        updatedEvent.setDateTime(LocalDateTime.of(updatedEvent.getDate(), updatedEvent.getStartTime()));
        eventService.save(updatedEvent);

        return "redirect:/eventslist";
    }

    /** Handling events search page requests
     *
     * @param model - Model object for transferring data between view and controller
     * @return view template name
     */
    @GetMapping(value = "/searchpage")
    public String search (Model model) {
        model.addAttribute("searchForm", new SearchForm());
        return "searchpage";
    }

    /** Processing events search
     *
     * @param model - Model object for transferring data between view and controller
     * @param searchForm - contains search criteria
     * @return
     */
    @PostMapping(value = "/searchpage")
    public String executeSearch (Model model, @ModelAttribute SearchForm searchForm) {

        System.out.println(searchForm);

        List<Event> results = new ArrayList<>();

        if (searchForm.getName() != null && !searchForm.getName().isEmpty()) {
            results.add(eventService.findByName(searchForm.getName()));
        } else if (searchForm.getDate() != null && !searchForm.getDate().isEmpty() && searchForm.getType() != null) {
            results = eventService.findByTypeAndDate(searchForm.getType(), LocalDate.parse(searchForm.getDate()));
        } else if (searchForm.getDate() != null && !searchForm.getDate().isEmpty()) {
            results = eventService.findByDate(LocalDate.parse(searchForm.getDate()));
        } else if (searchForm.getType() != null) {
            results = eventService.findByType(searchForm.getType());
        } else {
            return "searchpage";
        }

        model.addAttribute("results", results);
        return "/searchresults";
    }

    /** Handling search results page requests
     *
     * @param model - Model object for transferring data between view and controller
     * @return view template name
     */
    @GetMapping(value = "searchresults")
    public String showResults (Model model, @ModelAttribute("results") ArrayList<Event> results) {
        model.addAttribute("results", results);
        return "searchresults";
    }
}
