package com.cong.ioc;

import com.cong.ioc.annotation.Autowired;

public class Computer {

    private String id;

    private Keyboard keyboard;

    @Autowired
    private Screen screen;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}
