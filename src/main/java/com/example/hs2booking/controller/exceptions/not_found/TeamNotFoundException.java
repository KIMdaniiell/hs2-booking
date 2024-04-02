package com.example.hs2booking.controller.exceptions.not_found;


public class TeamNotFoundException extends NotFoundException {
    public TeamNotFoundException(String filtersString) {
        super("team", filtersString);
    }
}
