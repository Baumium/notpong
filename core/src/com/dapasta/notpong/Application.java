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
import com.dapasta.notpong.screens.GameListScreen;
import com.dapasta.notpong.screens.GameScreen;
import com.dapasta.notpong.screens.LoadingScreen;
import com.dapasta.notpong.screens.MainMenuScreen;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Application extends com.badlogic.gdx.Game {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    public OrthographicCamera camera;
	public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;

    public BitmapFont font30;
    public AssetManager assets;

    public Network network;
    public String sessionId;

    //Screens
    public LoadingScreen loadingScreen;
    public MainMenuScreen mainMenuScreen;
    public GameListScreen gameListScreen;
    public CreateGameScreen createGameScreen;
    public GameScreen gameScreen;

    @Override
	public void create () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        initFonts();

        assets = new AssetManager();

        network = new Network(5000, "192.168.1.132", 54555, 54777);
        network.connect();

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
    }
}
