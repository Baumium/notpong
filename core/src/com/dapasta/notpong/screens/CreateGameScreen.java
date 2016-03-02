package com.dapasta.notpong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dapasta.notpong.Application;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateGameScreen implements Screen {

    private Application app;

    private Stage stage;
    private Skin skin;

    private Table container;
    private TextField nameField;
    private SelectBox<Integer> sizeBox;
    private TextButton createButton;
    private TextButton backButton;

    public CreateGameScreen(Application app) {
        this.app = app;

        stage = new Stage(new FitViewport(Application.SCREEN_WIDTH, Application.SCREEN_HEIGHT, app.camera));
        skin = new Skin();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        initUI();

        app.socket.on("gameCreated", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject gameObject = new JSONObject(args[0].toString());
                    app.gameScreen.createGame(gameObject.getInt("gameId"), gameObject.getString("userId"), gameObject.getInt("size"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                app.setScreen(app.gameScreen);
            }
        });
    }

    private void initUI() {
        skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        skin.add("default-font", app.font30);
        skin.load(Gdx.files.internal("ui/uiskin.json"));

        nameField = new TextField("Game Name", skin);

        sizeBox = new SelectBox<Integer>(skin);
        sizeBox.setItems(2, 4);

        createButton = new TextButton("Create Game", skin);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createGame();
            }
        });

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.gameListScreen);
            }
        });

        container = new Table(skin);
        container.setFillParent(true);
        container.add("Game Details").fillX().prefWidth(stage.getWidth() / 2).colspan(2);
        container.row();
        container.add(nameField).fillX().colspan(2);
        container.row();
        container.add(sizeBox).fillX().colspan(2);
        container.row();
        container.add(backButton).fillX().uniform();
        container.add(createButton).fillX().uniform();

        stage.addActor(container);
    }

    private void createGame() {
        JSONObject gameData = new JSONObject();
        gameData.put("name", nameField.getText());
        gameData.put("size", sizeBox.getSelected());

        app.socket.emit("createGame", gameData);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        app.socket.off("gameCreated");
    }

    @Override
    public void dispose() {
        stage.dispose();
//        skin.dispose();
    }
}
