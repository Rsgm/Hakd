package gui.windows.device;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import game.Hakd;
import gui.windows.actors.DesktopAppIcon;
import networks.devices.Device;
import other.Util;

import java.util.HashSet;
import java.util.Set;

public class Desktop { // why not just combine this with deviceScene?
    private final DeviceScene window;
    private final Group desktop;
    private final Device device;
    private final Set<Button> desktopApps;

    private final TextureAtlas linearTextures = Hakd.assets.get("lTextures.txt", TextureAtlas.class);

    private final Image background;

    public Desktop(DeviceScene w) {
        window = w;
        device = window.getDevice();

        desktop = new Group();
        desktopApps = new HashSet<Button>();

        background = new Image(linearTextures.findRegion("wallpaper"));
        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        desktop.addActor(background);

        defaultApps();
    }

    public void open() {
        window.getCanvas().addActor(desktop);
    }

    public void close() {
        window.getCanvas().removeActor(desktop);
    }

    private void defaultApps() { // TODO automate this
        Sprite s;
        Color c = new Color(.7f, .7f, .7f, 1f);

        s = linearTextures.createSprite("console0Up");
        s.setColor(c);
        ImageButton terminal0 = new DesktopAppIcon(new TextureRegionDrawable(linearTextures.findRegion("console0Up")), new SpriteDrawable(s), window.getTerminal0());
        terminal0.setPosition(20, 20);
        desktopApps.add(terminal0);
        desktop.addActor(terminal0);

        s = linearTextures.createSprite("console1Up");
        s.setColor(c);
        ImageButton terminal1 = new DesktopAppIcon(new TextureRegionDrawable(linearTextures.findRegion("console1Up")), new SpriteDrawable(s), window.getTerminal1());
        terminal1.setPosition(60, 20);
        desktopApps.add(terminal1);
        desktop.addActor(terminal1);

        s = linearTextures.createSprite("console2Up");
        s.setColor(c);
        ImageButton terminal2 = new DesktopAppIcon(new TextureRegionDrawable(linearTextures.findRegion("console2Up")), new SpriteDrawable(s), window.getTerminal2());
        terminal2.setPosition(100, 20);
        desktopApps.add(terminal2);
        desktop.addActor(terminal2);

        s = linearTextures.createSprite("console3Up");
        s.setColor(c);
        ImageButton terminal3 = new DesktopAppIcon(new TextureRegionDrawable(linearTextures.findRegion("console3Up")), new SpriteDrawable(s), window.getTerminal3());
        terminal3.setPosition(140, 20);
        desktopApps.add(terminal3);
        desktop.addActor(terminal3);

        s = linearTextures.createSprite("infoUp");
        s.setColor(c);
        ImageButton info = new DesktopAppIcon(new TextureRegionDrawable(linearTextures.findRegion("infoUp")), new SpriteDrawable(s), window.getInfo());
        info.setPosition(40, 400);
        desktopApps.add(info);
        desktop.addActor(info);

        s = linearTextures.createSprite("shutdownUp");
        s.setColor(c);
        ImageButton shutdown = new DesktopAppIcon(new TextureRegionDrawable(linearTextures.findRegion("shutdownUp")), new SpriteDrawable(s), window.getShutdown());
        shutdown.setPosition(40, 350);
        desktopApps.add(shutdown);
        desktop.addActor(shutdown);

        s = linearTextures.createSprite("infoUp");
        s.setColor(c);
        ImageButton textEdit = new DesktopAppIcon(new TextureRegionDrawable(linearTextures.findRegion("infoUp")), new SpriteDrawable(s), window.getTextEdit());
        shutdown.setPosition(40, 300);
        desktopApps.add(textEdit);
        desktop.addActor(textEdit);
    }

    public DeviceScene getWindow() {
        return window;
    }

    public Group getDesktop() {
        return desktop;
    }

    public Device getDevice() {
        return device;
    }

    public Set<Button> getDesktopApps() {
        return desktopApps;
    }
}
