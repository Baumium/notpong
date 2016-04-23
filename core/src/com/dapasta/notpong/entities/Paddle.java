package com.dapasta.notpong.entities;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dapasta.notpong.Side;
import com.dapasta.notpong.packets.client.MovementRequest;
import com.dapasta.notpong.packets.server.MovementResponse;

import java.util.HashMap;

public class Paddle {
    private Side side;

    protected Rectangle rect;
    private static final float RELATIVE_WIDTH = 0.01f;
    private static final float RELATIVE_HEIGHT = 0.175f;

    protected static final float SPEED = 4f;


    public Paddle(Side side) {
        this.side = side;

        //Calculate size of paddle
        float xPos;
        float yPos;
        float width;
        float height;
        switch (side) {
            case LEFT:
                width = Gdx.graphics.getWidth() * RELATIVE_WIDTH;
                height = Gdx.graphics.getHeight() * RELATIVE_HEIGHT;
                xPos = width;
                yPos = (Gdx.graphics.getHeight() / 2) - (height / 2);
                break;
            case RIGHT:
                width = Gdx.graphics.getWidth() * RELATIVE_WIDTH;
                height = Gdx.graphics.getHeight() * RELATIVE_HEIGHT;
                xPos = Gdx.graphics.getWidth() - (2 * width);
                yPos = (Gdx.graphics.getHeight() / 2) - (height / 2);
                break;
            case TOP:
                width = Gdx.graphics.getWidth() * RELATIVE_WIDTH;
                height = Gdx.graphics.getHeight() * RELATIVE_HEIGHT;
                xPos = (Gdx.graphics.getWidth() / 2) - (width / 2);
                yPos = Gdx.graphics.getHeight() - height;
                break;
            case BOTTOM:
                width = Gdx.graphics.getWidth() * RELATIVE_WIDTH;
                height = Gdx.graphics.getHeight() * RELATIVE_HEIGHT;
                xPos = (Gdx.graphics.getWidth() / 2) - (width / 2);
                yPos = 0;
                break;
            default:
                xPos = 0;
                yPos = 0;
                width = 0;
                height = 0;
        }
        rect = new Rectangle(xPos, yPos, width, height);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void update(float delta, com.dapasta.notpong.Application app) {

    }

    public void setPosition(float x) {
        Vector2 center = new Vector2();
        center = rect.getCenter(center);
        rect.setCenter(center.x, x);
    }
}
