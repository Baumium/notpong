package com.dapasta.notpong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dapasta.notpong.Application;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class MainMenuScreen implements Screen {

    private final Application app;

    private Stage stage;

    private Skin skin;
    private Table container;
    private TextButton buttonPlay;
    private TextButton buttonQuit;

    private Image logoImage;

    public MainMenuScreen(final Application app) {
        this.app = app;

        stage = new Stage(new FitViewport(Application.SCREEN_WIDTH, Application.SCREEN_HEIGHT, app.camera));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        initUI();
    }

    private void initUI() {
        skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        skin.add("default-font", app.font30);
        skin.load(Gdx.files.internal("ui/uiskin.json"));

        logoImage = new Image(app.assets.get("img/circle.png", Texture.class));
        logoImage.addAction(sequence(alpha(0f), fadeIn(1.5f)));


        float buttonWidth = stage.getWidth() / 4f;
        float buttonHeight = stage.getHeight() / 8f;
        float buttonOffset = buttonHeight / 32f;

        buttonPlay = new TextButton("Play", skin, "default");
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.gameListScreen);
            }
        });

        buttonQuit = new TextButton("Quit", skin, "default");
        buttonQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        container = new Table(skin);
        container.setFillParent(true);
        container.add("Main Menu");
        container.add(logoImage).prefSize(stage.getHeight() / 10f, stage.getHeight() / 10f);
        container.row();
        container.add(buttonPlay).colspan(2).uniform().fillX().prefSize(buttonWidth, buttonHeight).spaceTop(buttonOffset);
        container.row();
        container.add(buttonQuit).colspan(2).uniform().fillX().prefSize(buttonWidth, buttonHeight).spaceTop(buttonOffset);
        stage.addActor(container);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
