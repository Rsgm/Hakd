package hakd.other;

import hakd.networks.devices.Router;
import ai.pathfinder.Node;

public class RouterNode extends Node {
    private final Router router;

    public RouterNode(Router router) {
	super();
	this.router = router;
    }

    public Router getRouter() {
	return router;
    }

}
