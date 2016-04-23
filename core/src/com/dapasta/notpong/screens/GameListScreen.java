package com.dapasta.notpong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dapasta.notpong.Application;
import com.dapasta.notpong.Game;
import com.dapasta.notpong.packets.client.GamesRequest;
import com.dapasta.notpong.packets.server.GamesResponse;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.ArrayList;
import java.util.List;

public class GameListScreen implements Screen {

    private Application app;

    private Listener gameListListener;

    private Stage stage;
    private Skin skin;


    private List<Game> games;
    private ScrollPane scrollPane;
    private Table container;
    private Table gameTable;
    private TextButton backButton;
    private TextButton createGameButton;

    public GameListScreen(Application app) {
        this.app = app;

        stage = new Stage(new FitViewport(Application.SCREEN_WIDTH, Application.SCREEN_HEIGHT, app.camera));
        skin = new Skin();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        games = new ArrayList<Game>();

        initUI();

        //Network related stuff
        gameListListener = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                if (object instanceof GamesResponse) {
                    GamesResponse response = (GamesResponse) object;
                    for (com.dapasta.notpong.packets.server.Game responseGame : response.games) {
                        Game game = new Game(1, responseGame.creator, responseGame.size);
                        games.add(game);
                    }

                    gameTable.clearChildren();
                    for (final Game game : games) {
                        Label size = new Label(game.getSize() + "", skin);
                        Label creator = new Label(game.getCreator(), skin);
                        TextButton joinButton = new TextButton("Join", skin);
                        joinButton.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
//                                app.socket.on("joinedGame", new Emitter.Listener() {
//                                    @Override
//                                    public void call(Object... args) {
//                                        System.out.println(args[0]);
//                                    }
//                                });
//                                app.socket.emit("joinGame", game.getId());
                            }
                        });

                        gameTable.add(size);
                        gameTable.add(creator);
                        gameTable.row();
                    }
                }
            }
        };
        app.network.addListener(gameListListener);

        getGames();
    }

    private void initUI() {
        skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        skin.add("default-font", app.font30);
        skin.load(Gdx.files.internal("ui/uiskin.json"));

        gameTable = new Table(skin);
        gameTable.setDebug(true);
        gameTable.setFillParent(true);
        gameTable.top();

        scrollPane = new ScrollPane(gameTable, skin);

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.mainMenuScreen);
            }
        });

        createGameButton = new TextButton("Create Server", skin);
        createGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.createGameScreen);
            }
        });

        container = new Table(skin);
        container.setFillParent(true);
        container.center();
        container.add("Game List").colspan(2);
        container.row();
        container.add(scrollPane).minSize(stage.getWidth() * 5f / 6f, stage.getHeight() - 150).colspan(2);
        container.row();
        container.add(backButton).uniform().fillX();
        container.add(createGameButton).uniform().fillX();

        stage.addActor(container);
    }

    private void getGames() {
        GamesRequest request = new GamesRequest();
        app.network.sendTcpPacket(request);
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            getGames();
        }
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
        app.network.removeListener(gameListListener);
    }

    @Override
    public void dispose() {
//        skin.dispose();
        stage.dispose();
    }
}
