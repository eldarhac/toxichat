package factory;

import model_lib.SocketProtocol;
import model_lib.SystemLogger;
import type.ILogger;
import type.ISocketProtocol;

public class AbstractFactory {

	public static ILogger getLogger(){
		return new SystemLogger();
	}

	public static ISocketProtocol getProtocol() {
		return new SocketProtocol();
	}
}
