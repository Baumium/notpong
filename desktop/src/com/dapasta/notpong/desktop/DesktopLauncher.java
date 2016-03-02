package com.dapasta.notpong.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dapasta.notpong.Application;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = Application.SCREEN_HEIGHT;
        config.width = Application.SCREEN_WIDTH;
		new LwjglApplication(new Application(), config);
	}
}
