package com.dapasta.notpong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dapasta.notpong.screens.CreateGameScreen;
import com.dapasta.notpong.screens.GameScreen;
import com.dapasta.notpong.screens.LoadingScreen;
import com.dapasta.notpong.screens.MainMenuScreen;
import com.dapasta.notpong.screens.GameListScreen;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class Application extends com.badlogic.gdx.Game {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    public OrthographicCamera camera;
	public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;

    public BitmapFont font30;
    public AssetManager assets;

    //Screens
    public LoadingScreen loadingScreen;
    public MainMenuScreen mainMenuScreen;
    public GameListScreen gameListScreen;
    public CreateGameScreen createGameScreen;
    public GameScreen gameScreen;

    public Socket socket;
    {
        try {
            socket = IO.socket("http://50.1.179.232:8080");
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
	public void create () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
		batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        initFonts();

        assets = new AssetManager();

        loadingScreen = new LoadingScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        gameListScreen = new GameListScreen(this);
        createGameScreen = new CreateGameScreen(this);
        gameScreen = new GameScreen(this);
        this.setScreen(loadingScreen);
    }

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Thin.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 30;
        params.color = Color.BLACK;

        font30 = generator.generateFont(params);

    }

	@Override
	public void render () {
        super.render();

        //If escape pressed, quite game
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        loadingScreen.dispose();
        mainMenuScreen.dispose();
        gameListScreen.dispose();
        createGameScreen.dispose();
        gameScreen.dispose();
        assets.dispose();
//        font30.dispose(); Commented because it is disposed when stages in other screens are disposed

        socket.off();
        socket.disconnect();
    }
}
