package hakd.game.gameplay;

import hakd.gui.Room;
import hakd.gui.windows.device.DeviceScene;

public class Player extends Character {
    private Room room;
    private DeviceScene openWindow;

    // --------methods--------
    public Player(String name, City city) {
        super(null, name, city);
        this.name = name;
    }

    @Override
    public void update() {
    }

    public DeviceScene getOpenWindow() {
        return openWindow;
    }

    public void setOpenWindow(DeviceScene openWindow) {
        this.openWindow = openWindow;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
