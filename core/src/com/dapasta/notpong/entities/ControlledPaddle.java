package com.dapasta.notpong.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dapasta.notpong.Application;
import com.dapasta.notpong.Side;
import com.dapasta.notpong.packets.client.MovementRequest;
import com.dapasta.notpong.packets.server.MovementResponse;

import java.util.HashMap;
import java.util.Map;

public class ControlledPaddle extends Paddle {

    private int movementCounter;
    private Map<Integer, MovementRequest> movementQueue;
    private final int MAX_QUEUE_SIZE = 50;

    public ControlledPaddle(float width, float height, float scaleFactor) {
        super(width, height, Side.LEFT, scaleFactor);

        movementCounter = 0;
        movementQueue = new HashMap<Integer, MovementRequest>();
    }

    public void update(float delta, Application app) {
        super.update(delta, app);

        // Wait for server to catch up
        if (movementQueue.size() < MAX_QUEUE_SIZE) {
            //Make sure screen is touched when on mobile
            if ((Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android && Gdx.input.isTouched())
                    || (Gdx.app.getType() != com.badlogic.gdx.Application.ApplicationType.Android) && Gdx.input.isTouched()) {
                float yInput = Gdx.graphics.getHeight() - Gdx.input.getY();
                Vector2 center = new Vector2();
                center = rect.getCenter(center);

                //Check if paddle goes out of bounds
                if ((yInput > center.y && rect.getY() + rect.getHeight() < Gdx.graphics.getHeight())
                        || (yInput < center.y && rect.getY() > 0)) {
                    float dY = yInput - center.y;

                    //Accelerate to mouse/finger
                    float pos = center.y + (dY * SPEED * delta);
                    setPosition(pos);

                    //Create request
                    MovementRequest request = new MovementRequest();
                    request.x = pos / Gdx.graphics.getHeight()/ scaleFactor;
                    request.playerId = app.network.getId();
                    request.sessionId = app.sessionId;
                    request.id = movementCounter;

                    //Store request for future checking
                    movementQueue.put(movementCounter, request);
                    movementCounter++;
                    app.network.sendTcpPacket(request);
                }
            }
        }
    }

    public void draw(ShapeRenderer renderer) {
        super.draw(renderer);
    }

    public void movementReceived(MovementResponse response, float fieldSize) {
        //Check stored request against server response
        if(movementQueue.containsKey(response.id)) {
            MovementRequest request = movementQueue.get(response.id);
            if(request.x == response.x) {
                movementQueue.remove(response.id);
            } else {
                setScaledPosition(response.x);

                // Remove all movements after desynced movement
                for(int i = response.id; movementQueue.containsKey(i); i++) {
                    movementQueue.remove(i);
                }
            }
        }
    }
}
