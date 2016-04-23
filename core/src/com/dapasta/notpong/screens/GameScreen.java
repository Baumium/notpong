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

                if(object instanceof MovementResponse) {
                    MovementResponse response = (MovementResponse) object;

                    Paddle paddle = players.get(app.network.getId());
                    paddle.setPosition(((response.x)));
                    ((ControlledPaddle) paddle).movementReceived(response);
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

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);

        app.network.addListener(movementListener);

//        app.socket.on("update", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                try {
//                    JSONObject gameObject = new JSONObject(args[0].toString());
//
//                    JSONObject playersObject = gameObject.getJSONObject("players");
//                    for (String key : playersObject.keySet()) {
//                        JSONObject paddleObject = playersObject.getJSONObject(key).getJSONObject("paddle");
//                        players.get(key).setPosition((float) paddleObject.getDouble("pos") * Gdx.graphics.getHeight() / 100f);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        app.socket.on("playerJoined", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                System.out.println(args[0]);
//            }
//        });
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
//        app.socket.off("update");
//        app.socket.off("playerJoined");
        app.network.removeListener(movementListener);
    }

    @Override
    public void dispose() {

    }
}
