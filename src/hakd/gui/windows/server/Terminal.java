package hakd.gui.windows.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import hakd.game.Command;
import hakd.gui.Assets;
import hakd.internet.Internet;
import hakd.networks.devices.Device;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Terminal implements ServerWindow {
	private final ServerWindowStage window;

	private final Table table;

	private final TextField input;
	private final Label display;
	private final ScrollPane scroll;

	private boolean firstTab = true;
	private String tabString = "";
	private int tabIndex;
	private final List<String> history; // command history
	private int line = 0; // holds the position of the history
	private final Device device;
	private Command command;

	private final Terminal terminal;

	public Terminal(ServerWindowStage w) {
		terminal = this;
		window = w;
		device = window.getDevice();

		Skin skin = Assets.skin;
		history = new ArrayList<String>();

		table = new com.badlogic.gdx.scenes.scene2d.ui.Window("Terminal", skin);
		table.setSize(window.getCanvas().getWidth() * .9f, window.getCanvas().getHeight() * .9f);

		ImageButton close = new ImageButton(new TextureRegionDrawable(Assets.linearTextures.findRegion("close")));
		close.setPosition(table.getWidth() - close.getWidth(), table.getHeight() - close.getHeight() - 20);

		input = new TextField("", skin.get("console", TextFieldStyle.class));
		display = new Label("", skin.get("console", LabelStyle.class));
		scroll = new ScrollPane(display, skin);

		display.setWrap(false);
		display.setAlignment(10, Align.left);
		display.setText("Terminal [Version 0." + ((int) (Math.random() * 100)) / 10 + "]" + "\nroot @ " + Internet.ipToString(device.getIp()) + "\nMemory: " + device.getTotalMemory() + "MB\nStorage: " + device.getTotalStorage() + "GB");

		table.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// touch up will not work without this returning true
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if(y >= table.getHeight() - 20) {
					if(table.getX() < 0) {
						table.setX(0);
					}
					if(table.getY() < 0) {
						table.setY(0);
					}
					if(table.getX() + table.getWidth() > Gdx.graphics.getWidth()) {
						table.setX(Gdx.graphics.getWidth() - table.getWidth());
					}
					if(table.getY() + table.getHeight() > Gdx.graphics.getHeight()) {
						table.setY(Gdx.graphics.getHeight() - table.getHeight());
					}
				}
			}
		});

		close.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				close();
			}
		});

		input.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Keys.ENTER && command == null) {
					System.out.println(input.getText());
					display.setText(display.getText() + "\n\nroot @ " + Internet.ipToString(device.getIp()) + "\n>" + input.getText());
					history.add(input.getText());
					command = new Command(input.getText(), device, terminal);

					line = history.size();
					input.setText("");
				} else if(keycode == Keys.C && (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) && command != null) {
					addText("Program Stopped");
					command.stop();
				} else if(keycode == Keys.TAB && command == null) {
					String s = "<";
					File[] files = new File("python/programs/").listFiles();
					List<File> filesFiltered = new ArrayList<File>();

					if(firstTab) {
						tabString = input.getText();
					}

					assert files != null;
					for(File f : files) {
						if(f.getName().startsWith(tabString)) {
							filesFiltered.add(f);
						}
					}

					for(File f : filesFiltered) {
						s += f.getName().substring(0, f.getName().length() - 3);
						if(filesFiltered.lastIndexOf(f) != filesFiltered.size() - 1) {
							s += ", ";
						}
					}
					if(!filesFiltered.isEmpty()) {
						if(firstTab) {
							addText(s + ">");
							firstTab = false;
						}

						String name = filesFiltered.get(tabIndex).getName();
						input.setText(name.substring(0, name.length() - 3));
						input.setCursorPosition(input.getText().length());
						// change this to insert text for completion of
						// parameters,
						// instead of overwriting the input box
					} else {
						addText("<There are no programs with that name>");
					}

					tabIndex++;
					if(tabIndex >= filesFiltered.size()) {
						tabIndex = 0;
					}
				} else if(keycode == Keys.DOWN && line < history.size() - 1) {
					line++;
					input.setText(history.get(line));
					input.setCursorPosition(input.getText().length());
				} else if(keycode == Keys.UP && line > 0) {
					line--;
					input.setText(history.get(line));
					input.setCursorPosition(input.getText().length());
				}

				if(keycode != Keys.TAB && keycode != Keys.LEFT && keycode != Keys.RIGHT) {
					tabIndex = 0;
					tabString = "";
					firstTab = true;
				}

				if(command != null) {
					command.getUserInputBuffer().offer(keycode);
				}
				return true;
			}

			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if(keycode == Keys.ENTER || (keycode == Keys.C && (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) && command != null)) {
					scroll.setScrollY(display.getHeight());
				}
				return super.keyUp(event, keycode);
			}
		});

		table.add(scroll).expand().fill();
		table.row();
		table.add(input).left().fillX();

		table.addActor(close);
	}

	public void addText(String text) {
		display.setText(display.getText() + "\n   " + text);
		scroll.setScrollY(display.getHeight());
		// I doubt that set text is thread safe, although only two threads, that
		// I know of, can access it; the main and command threads.
	}

	public void replaceText(String newText, int lineFromBottom) {
		String t = display.getText().toString();
		int n = t.length();
		int m = n;
		lineFromBottom++;

		if(lineFromBottom < 1) {
			return;
		}

		for(int i = 0; i < lineFromBottom; i++) {
			m = n;
			n = t.lastIndexOf("\n", n - 2);
		}

		String s = t.substring(0, n);
		s += "\n   " + newText;
		s += t.substring(m, t.length());

		display.setText(s);
		scroll.setScrollY(display.getHeight());
	}

	@Override
	public void open() {
		window.getCanvas().addActor(table);
	}

	@Override
	public void close() {
		window.getCanvas().removeActor(table);
	}

	public Label getDisplay() {
		return display;
	}

	public ScrollPane getScroll() {
		return scroll;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
}
