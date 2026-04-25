package io.github.skorpionz7.darkamsunrise;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

public class World {
    private final State state;
    private final Download download;

    World(State stateIn, Download downloadIn) {
        state = stateIn;
        download = downloadIn;

        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            factory.setForceTextTerminal(false);
            factory.setPreferTerminalEmulator(true);
            factory.setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.getDefaultOfSize(14));
            factory.setInitialTerminalSize(new TerminalSize(140,40));

            SwingTerminalFrame terminal = (SwingTerminalFrame) factory.createTerminal();
            terminal.setTitle("Dark AM Sunrise");

            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            TextGraphics tg = screen.newTextGraphics();

            tg.putString(0, 0, "Hello World");

            TextColor customColor = new TextColor.RGB(200, 28, 110); // Orange
            tg.setForegroundColor(customColor);

            tg.putString(0, 0, "┌──────────┐");
            tg.putString(0, 1, "│⁋Unicode!Ʊ│");
            tg.putString(0, 2, "└──────────┘");
            tg.putString(0,3, "ࢺ");
            tg.putString(0,4, "ߏ");
            screen.refresh();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}