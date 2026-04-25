package io.github.skorpionz7.darkamsunrise;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import com.googlecode.lanterna.gui2.Panel;

import java.awt.*;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class World {
    private final State state;
    private final Download download;

    private final TextBox commandLine = new TextBox(new TerminalSize(36, 1));
    private final Label chat = new Label("Welcome to Dark AM Sunrise");

    World(State state, Download download) {
        this.state = state;
        this.download = download;

        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            factory.setForceTextTerminal(false);
            factory.setPreferTerminalEmulator(true);

            InputStream is = getClass().getResourceAsStream("/JetBrainsMono-Regular.ttf");

            if (is == null) {
                throw new Exception("Could not find font in resources!");
            }
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            customFont = customFont.deriveFont(14f);
            factory.setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.newInstance(customFont));

            factory.setInitialTerminalSize(new TerminalSize(state.getW(), state.getH()));

            SwingTerminalFrame terminal = (SwingTerminalFrame) factory.createTerminal();
            terminal.setTitle("Dark AM Sunrise");

            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
            BasicWindow window = new BasicWindow("Dark AM Sunrise");
            window.setHints(List.of(Window.Hint.EXPANDED));

            Panel root = new Panel(new BorderLayout());

            Panel stack = new Panel(new GridLayout(1));
            stack.addComponent(chat);
            stack.addComponent(commandLine);

            commandLine.setInputFilter((interactable, keyStroke) -> {
                if (keyStroke.getKeyType() == KeyType.Enter) {
                    if (Objects.equals(commandLine.getText(), "")) return false;
                    String newLine = wrapText("<Skorpion> " + commandLine.getText(), 36);

                    String[] lines = (chat.getText() + "\n" + newLine).split("\n");

                    int start = Math.max(0, lines.length - 18);
                    StringBuilder trimmed = new StringBuilder();

                    for (int i = start; i < lines.length; i++) {
                        trimmed.append(lines[i]).append("\n");
                    }

                    chat.setText(trimmed.toString().trim());
                    commandLine.setText("");
                    return false;
                }
                return true;
            });

            Panel bottom = new Panel(new BorderLayout());
            bottom.addComponent(stack, BorderLayout.Location.LEFT);

            root.addComponent(bottom, BorderLayout.Location.BOTTOM);
            window.setComponent(root);

            gui.addWindowAndWait(window);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String wrapText(String text, int width) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            result.append(text.charAt(i));

            if ((i + 1) % width == 0) {
                result.append("\n");
            }
        }

        return result.toString();
    }
}