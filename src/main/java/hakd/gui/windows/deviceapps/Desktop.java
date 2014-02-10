package hakd.gui.windows.deviceapps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import hakd.gui.Assets;
import hakd.gui.windows.actors.DesktopAppIcon;
import hakd.networks.devices.Device;

import java.util.HashSet;
import java.util.Set;

public final class Desktop {
    private final ServerWindowStage window;
    private final Group desktop;
    private final Device device;
    private final Set<Button> desktopApps;

    private final Image background;

    public Desktop(ServerWindowStage w) {
        window = w;
        device = window.getDevice();

        desktop = new Group();
        desktopApps = new HashSet<Button>();

        background = new Image(Assets.linearTextures.findRegion("wallpaper"));
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

    private void defaultApps() {
        Sprite s;
        Color c = new Color(.7f, .7f, .7f, 1f);

        s = Assets.linearTextures.createSprite("console0Up");
        s.setColor(c);
        ImageButton terminal0 = new DesktopAppIcon(new TextureRegionDrawable(Assets.linearTextures.findRegion("console0Up")), new SpriteDrawable(s), window.getTerminal0());
        terminal0.setPosition(20, 20);
        desktopApps.add(terminal0);
        desktop.addActor(terminal0);

        s = Assets.linearTextures.createSprite("console1Up");
        s.setColor(c);
        ImageButton terminal1 = new DesktopAppIcon(new TextureRegionDrawable(Assets.linearTextures.findRegion("console1Up")), new SpriteDrawable(s), window.getTerminal1());
        terminal1.setPosition(60, 20);
        desktopApps.add(terminal1);
        desktop.addActor(terminal1);

        s = Assets.linearTextures.createSprite("console2Up");
        s.setColor(c);
        ImageButton terminal2 = new DesktopAppIcon(new TextureRegionDrawable(Assets.linearTextures.findRegion("console2Up")), new SpriteDrawable(s), window.getTerminal2());
        terminal2.setPosition(100, 20);
        desktopApps.add(terminal2);
        desktop.addActor(terminal2);

        s = Assets.linearTextures.createSprite("console3Up");
        s.setColor(c);
        ImageButton terminal3 = new DesktopAppIcon(new TextureRegionDrawable(Assets.linearTextures.findRegion("console3Up")), new SpriteDrawable(s), window.getTerminal3());
        terminal3.setPosition(140, 20);
        desktopApps.add(terminal3);
        desktop.addActor(terminal3);

        s = Assets.linearTextures.createSprite("infoUp");
        s.setColor(c);
        ImageButton info = new DesktopAppIcon(new TextureRegionDrawable(Assets.linearTextures.findRegion("infoUp")), new SpriteDrawable(s), window.getInfo());
        info.setPosition(40, 400);
        desktopApps.add(info);
        desktop.addActor(info);

        s = Assets.linearTextures.createSprite("shutdownUp");
        s.setColor(c);
        ImageButton shutdown = new DesktopAppIcon(new TextureRegionDrawable(Assets.linearTextures.findRegion("shutdownUp")), new SpriteDrawable(s), window.getShutdown());
        shutdown.setPosition(40, 350);
        desktopApps.add(shutdown);
        desktop.addActor(shutdown);
    }

    public ServerWindowStage getWindow() {
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
