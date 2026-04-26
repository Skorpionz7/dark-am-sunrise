package io.github.skorpionz7.darkamsunrise;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Button;
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
import java.util.List;
import java.util.Objects;


public class World {
    private final State state;
    private final Download download;

    private final TextBox commandLine = new TextBox(new TerminalSize(36, 1));
    private final Label chat = new Label("");
    private final Button inventoryButton;
    private final Button tradeButton;

    private final BasicWindow window = new BasicWindow("Dark AM Sunrise");
    private final Panel gameRoot;
    private final Panel loginRoot;

    private final TextBox passField;
    private final TextBox userField;

    World(State state, Download download) {
        this.state = state;
        this.download = download;

        try {
            String text = wrapText("Navigate the interface with your arrow keys to begin your adventure.", 36);
            StringBuilder chatText = new StringBuilder(text + "\n" + "Welcome to Dark AM Sunrise!");
            for (int i =1; i<27; i++) {
                chatText.insert(0, i+"\n");
            }
            chat.setText(chatText.toString());

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
            window.setHints(List.of(Window.Hint.EXPANDED));

            window.setTheme(LanternaThemes.getRegisteredTheme("defrost"));

            gameRoot = new Panel(new BorderLayout());

            Panel invTrade = new Panel();
            invTrade.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            inventoryButton = new Button("Inventory");
            tradeButton = new Button("Trade");
            invTrade.addComponent(inventoryButton);
            invTrade.addComponent(tradeButton);

            Panel stack = new Panel(new GridLayout(1));

            stack.addComponent(invTrade);
            stack.addComponent(chat);
            stack.addComponent(commandLine);

            commandLine.setInputFilter((interactable, keyStroke) -> {
                if (keyStroke.getKeyType() == KeyType.Enter) {
                    if (Objects.equals(commandLine.getText(), "")) return false;
                    String newLine = wrapText("<"+download.getUsername()+"> " + commandLine.getText(), 36);

                    String[] lines = (chat.getText() + "\n" + newLine).split("\n");

                    int start = Math.max(0, lines.length - 29);
                    StringBuilder trimmed = new StringBuilder();

                    for (int i = start; i < lines.length; i++) {
                        trimmed.append(lines[i]).append("\n");
                    }

                    chat.setText(trimmed.toString().trim());
                    commandLine.setText("");
                    inventoryButton.takeFocus();
                    return false;
                }
                return true;
            });

            Panel bottom = new Panel(new BorderLayout());
            bottom.addComponent(stack, BorderLayout.Location.LEFT);

            gameRoot.addComponent(bottom, BorderLayout.Location.BOTTOM);
            window.setComponent(gameRoot);

            gui.addWindow(window);

            loginRoot = new Panel();
            loginRoot.setLayoutManager(new LinearLayout(Direction.VERTICAL));

            String asciiTitle = "▓█████▄  ▄▄▄       ██▀███   ██ ▄█▀    ▄▄▄       ███▄ ▄███▓     ██████  █    ██  ███▄    █  ██▀███   ██▓  ██████ ▓█████ \n" +
                    "▒██▀ ██▌▒████▄    ▓██ ▒ ██▒ ██▄█▒    ▒████▄    ▓██▒▀█▀ ██▒   ▒██    ▒  ██  ▓██▒ ██ ▀█   █ ▓██ ▒ ██▒▓██▒▒██    ▒ ▓█   ▀ \n" +
                    "░██   █▌▒██  ▀█▄  ▓██ ░▄█ ▒▓███▄░    ▒██  ▀█▄  ▓██    ▓██░   ░ ▓██▄   ▓██  ▒██░▓██  ▀█ ██▒▓██ ░▄█ ▒▒██▒░ ▓██▄   ▒███   \n" +
                    "░▓█▄   ▌░██▄▄▄▄██ ▒██▀▀█▄  ▓██ █▄    ░██▄▄▄▄██ ▒██    ▒██      ▒   ██▒▓▓█  ░██░▓██▒  ▐▌██▒▒██▀▀█▄  ░██░  ▒   ██▒▒▓█  ▄ \n" +
                    "░▒████▓  ▓█   ▓██▒░██▓ ▒██▒▒██▒ █▄    ▓█   ▓██▒▒██▒   ░██▒   ▒██████▒▒▒▒█████▓ ▒██░   ▓██░░██▓ ▒██▒░██░▒██████▒▒░▒████▒\n" +
                    " ▒▒▓  ▒  ▒▒   ▓▒█░░ ▒▓ ░▒▓░▒ ▒▒ ▓▒    ▒▒   ▓▒█░░ ▒░   ░  ░   ▒ ▒▓▒ ▒ ░░▒▓▒ ▒ ▒ ░ ▒░   ▒ ▒ ░ ▒▓ ░▒▓░░▓  ▒ ▒▓▒ ▒ ░░░ ▒░ ░\n" +
                    " ░ ▒  ▒   ▒   ▒▒ ░  ░▒ ░ ▒░░ ░▒ ▒░     ▒   ▒▒ ░░  ░      ░   ░ ░▒  ░ ░░░▒░ ░ ░ ░ ░░   ░ ▒░  ░▒ ░ ▒░ ▒ ░░ ░▒  ░ ░ ░ ░  ░\n" +
                    " ░ ░  ░   ░   ▒     ░░   ░ ░ ░░ ░      ░   ▒   ░      ░      ░  ░  ░   ░░░ ░ ░    ░   ░ ░   ░░   ░  ▒ ░░  ░  ░     ░   \n" +
                    "   ░          ░  ░   ░     ░  ░            ░  ░       ░            ░     ░              ░    ░      ░        ░     ░  ░\n" +
                    " ░                                                                                                                     ";
            Label title = new Label(asciiTitle);
            title.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            loginRoot.addComponent(title);

            loginRoot.addComponent(new EmptySpace(new TerminalSize(1, 1)));

            Panel loginBox = new Panel();
            loginBox.setLayoutManager(new LinearLayout(Direction.VERTICAL));

            Panel userRow = new Panel();
            userRow.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            userRow.addComponent(new Label("Name:     "));
            userField = new TextBox(new TerminalSize(30,1));
            userRow.addComponent(userField);
            userRow.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            loginBox.addComponent(userRow);

            Panel passRow = new Panel();
            passRow.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            passRow.addComponent(new Label("Password: "));
            passField = new TextBox(new TerminalSize(30,1)).setMask('*');
            passRow.addComponent(passField);
            passRow.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            loginBox.addComponent(passRow);

            loginBox.addComponent(new EmptySpace(new TerminalSize(1, 1)));

            Panel buttons = new Panel();
            buttons.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            buttons.addComponent(new Button("Login", () -> {login();}));
            buttons.addComponent(new EmptySpace(TerminalSize.ONE));
            buttons.addComponent(new Button("Quit",() -> {quit();}));
            buttons.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            loginBox.addComponent(buttons);

            loginBox.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            loginRoot.addComponent(loginBox);

            window.setComponent(loginRoot);

            window.setTitle("Enter the Realm: Where Magic Meets Machine");

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

    private void login() {
        window.setTitle("Gateway Window to the Living Realm");
        download.setUsername(userField.getText());
        window.setComponent(gameRoot);
    }

    private void quit() {
        window.close();
        System.exit(0);
    }
}