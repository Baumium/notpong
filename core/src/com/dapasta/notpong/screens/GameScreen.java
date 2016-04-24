package com.dapasta.notpong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dapasta.notpong.Application;
import com.dapasta.notpong.entities.ControlledPaddle;
import com.dapasta.notpong.entities.Paddle;
import com.dapasta.notpong.Side;
import com.dapasta.notpong.packets.server.MovementResponse;
import com.dapasta.notpong.packets.server.PlayerDisconnectBroadcast;
import com.dapasta.notpong.packets.server.PlayerJoinBroadcast;
import com.dapasta.notpong.packets.server.PlayerUpdateBroadcast;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.HashMap;
import java.util.Map;

public class GameScreen implements Screen {

    private final Application app;

    private Listener movementListener;

    //Game data
    private int size;
    private int gameId;

    private Map<Integer, Paddle> players;


    public GameScreen(final Application app) {
        this.app = app;

        movementListener = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);

                if (object instanceof MovementResponse) {
                    MovementResponse response = (MovementResponse) object;

                    Paddle paddle = players.get(app.network.getId());
                    paddle.setPosition(((response.x)));
                    ((ControlledPaddle) paddle).movementReceived(response);
                }  else if (object instanceof PlayerUpdateBroadcast) {
                    PlayerUpdateBroadcast broadcast = (PlayerUpdateBroadcast) object;

                    players.get(broadcast.id).setPosition(broadcast.x);
                } else if (object instanceof PlayerJoinBroadcast) {
                    PlayerJoinBroadcast broadcast = (PlayerJoinBroadcast) object;

                    // Get new player side based on current player size
                    Side side;
                    switch (players.size()) {
                        case 1:
                            side = Side.RIGHT;
                            break;
                        case 2:
                            side = Side.TOP;
                            break;
                        case 3:
                            side = Side.BOTTOM;
                            break;
                        default:
                            side = null;
                    }

                    if (side != null) {
                        Paddle paddle = new Paddle(side);
                        players.put(broadcast.id, paddle);
                    }
                } else if (object instanceof PlayerDisconnectBroadcast) {
                    PlayerDisconnectBroadcast broadcast = (PlayerDisconnectBroadcast) object;
                    players.remove(broadcast.id);
                }
            }
        };
    }

    public void createGame(int gameId, int size) {
        this.gameId = gameId;
        this.size = size;
        players = new HashMap<Integer, Paddle>(size);
        players.put(app.network.getId(), new ControlledPaddle());
    }

    public void addPlayers(Map<Integer, String> players) {
        for (Integer id : players.keySet()) {
            if (id != app.network.getId()) {
                Side side;
                switch (this.players.size()) {
                    case 1:
                        side = Side.RIGHT;
                        break;
                    case 2:
                        side = Side.TOP;
                        break;
                    case 3:
                        side = Side.BOTTOM;
                        break;
                    default:
                        side = null;
                }

                if (side != null) {
                    Paddle paddle = new Paddle(side);
                    this.players.put(id, paddle);
                }
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);

        app.network.addListener(movementListener);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (Paddle paddle : players.values()) {
            paddle.update(delta, app);
        }

        app.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Paddle paddle : players.values()) {
            paddle.draw(app.shapeRenderer);
        }

        app.shapeRenderer.end();
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
        app.network.removeListener(movementListener);
    }

    @Override
    public void dispose() {

    }
}
