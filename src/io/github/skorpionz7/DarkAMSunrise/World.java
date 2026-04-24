package io.github.skorpionz7.DarkAMSunrise;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class World {
    private final State state;
    private final Download download;

    World(State stateIn, Download downloadIn) {
        state = stateIn;
        download = downloadIn;

        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            factory.setPreferTerminalEmulator(true);
            Terminal terminal = factory.createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();
            TextGraphics tg = screen.newTextGraphics();

            tg.putString(0, 0, "Hello World");
            screen.refresh();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}