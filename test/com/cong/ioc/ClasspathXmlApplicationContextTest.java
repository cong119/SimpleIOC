package com.cong.ioc;

import static org.junit.Assert.*;

import com.cong.ioc.context.ClasspathXmlApplicationContext;
import org.junit.Before;
import org.junit.Test;

public class ClasspathXmlApplicationContextTest {

    private Computer expectComputer;

    @Before
    public void initComputer() {
        expectComputer = new Computer();
        expectComputer.setId("cong123");

        Keyboard keyboard = new Keyboard();
        keyboard.setButtonNum("26");
        keyboard.setColor("Black");

        expectComputer.setKeyboard(keyboard);

        Screen screen = new Screen();
        screen.setLength("1360");
        screen.setWidth("750");

        expectComputer.setScreen(screen);
    }

    @Test
    public void test() throws Exception {
        ClasspathXmlApplicationContext applicationContext = new ClasspathXmlApplicationContext("classpath:BeanConfig.xml");

        Computer computer = (Computer) applicationContext.getBean("computer");

        assertEquals(computer.getId(), expectComputer.getId());
        assertEquals(computer.getKeyboard().getButtonNum(), expectComputer.getKeyboard().getButtonNum());
        assertEquals(computer.getKeyboard().getColor(), expectComputer.getKeyboard().getColor());
        assertEquals(computer.getScreen().getLength(), expectComputer.getScreen().getLength());
        assertEquals(computer.getScreen().getWidth(), expectComputer.getScreen().getWidth());
    }


}
