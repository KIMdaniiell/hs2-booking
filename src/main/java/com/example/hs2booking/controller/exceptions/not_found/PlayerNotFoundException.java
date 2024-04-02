package com.example.hs2booking.controller.exceptions.not_found;


public class PlayerNotFoundException extends NotFoundException {
    public PlayerNotFoundException(String filtersString) {
        super("player", filtersString);
    }
}
