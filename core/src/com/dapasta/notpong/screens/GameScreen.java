package com.dapasta.notpong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dapasta.notpong.Application;
import com.dapasta.notpong.entities.Ball;
import com.dapasta.notpong.entities.ControlledPaddle;
import com.dapasta.notpong.entities.Paddle;
import com.dapasta.notpong.Side;
import com.dapasta.notpong.packets.server.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.HashMap;
import java.util.Map;

public class GameScreen implements Screen {

    private final Application app;

    private Listener movementListener;

    // Game data
    private int playerSize;
    private String gameId;

    // Game field data
    private float fieldSize;
    private float paddleWidth;
    private float paddleHeight;
    private Map<Integer, Paddle> players;
    private Ball ball;


    public GameScreen(final Application app) {
        this.app = app;

        movementListener = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);

                if (object instanceof MovementResponse) {
                    MovementResponse response = (MovementResponse) object;

                    Paddle paddle = players.get(app.network.getId());
                    paddle.setScaledPosition(response.x);
                    ((ControlledPaddle) paddle).movementReceived(response, fieldSize);
                }  else if (object instanceof BallBroadcast) {
                    BallBroadcast broadcast = (BallBroadcast) object;

                    ball.setPosition(Gdx.graphics.getWidth() * (broadcast.x / fieldSize), Gdx.graphics.getHeight() * (broadcast.y / fieldSize));
                } else if (object instanceof PlayerUpdateBroadcast) {
                    PlayerUpdateBroadcast broadcast = (PlayerUpdateBroadcast) object;

                    players.get(broadcast.id).setPosition(Gdx.graphics.getHeight() * broadcast.x / fieldSize);
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
                        Paddle paddle = new Paddle(paddleWidth, paddleHeight, side, 1 / fieldSize);
                        players.put(broadcast.id, paddle);
                    }
                } else if (object instanceof PlayerDisconnectBroadcast) {
                    PlayerDisconnectBroadcast broadcast = (PlayerDisconnectBroadcast) object;
                    players.remove(broadcast.id);
                }
            }
        };
    }

    public void createGame(GameInfoPacket response, boolean isCreator) {
        gameId = response.sessionId;
        playerSize = response.playerSize;

        fieldSize = response.fieldSize;
        paddleWidth = response.paddleWidth;
        paddleHeight = response.paddleHeight;

        players = new HashMap<Integer, Paddle>(playerSize);
        players.put(app.network.getId(), new ControlledPaddle(paddleWidth, paddleHeight, 1 / fieldSize));

        ball = new Ball(Gdx.graphics.getWidth() * (response.ballRadius / fieldSize), !isCreator, false);
        ball.resetPosition();
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
                    Paddle paddle = new Paddle(paddleWidth, paddleHeight, side, 1 / fieldSize);
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

        ball.draw(app.shapeRenderer);

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
