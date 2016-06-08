package com.dapasta.notpong.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dapasta.notpong.Side;

public class Paddle {
    private Side side;

    protected Rectangle rect;

    protected static final float SPEED = 4f;

    protected float scaleFactor;


    public Paddle(float unscaledWidth, float unscaledHeight, Side side, float scaleFactor) {
        this.side = side;
        this.scaleFactor = scaleFactor;

        //Calculate size of paddle
        float xPos;
        float yPos;
        float width;
        float height;
        switch (side) {
            case LEFT:
                width = unscaledWidth * Gdx.graphics.getWidth() * scaleFactor;
                height = unscaledHeight * Gdx.graphics.getHeight() * scaleFactor;
                xPos = width;
                yPos = (Gdx.graphics.getHeight() / 2) - (height / 2);
                break;
            case RIGHT:
                width = unscaledWidth * Gdx.graphics.getWidth() * scaleFactor;
                height = unscaledHeight * Gdx.graphics.getHeight() * scaleFactor;
                xPos = Gdx.graphics.getWidth() - (2 * width);
                yPos = (Gdx.graphics.getHeight() / 2) - (height / 2);
                break;
            case TOP:
                width = unscaledHeight * Gdx.graphics.getHeight() * scaleFactor;
                height = unscaledWidth * Gdx.graphics.getWidth() * scaleFactor;
                xPos = (Gdx.graphics.getWidth() / 2) - (width / 2);
                yPos = Gdx.graphics.getHeight() - height;
                break;
            case BOTTOM:
                width = unscaledHeight * Gdx.graphics.getHeight() * scaleFactor;
                height = unscaledWidth * Gdx.graphics.getWidth() * scaleFactor;
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

    public void setScaledPosition(float x) {
        setPosition(Gdx.graphics.getHeight() * x * scaleFactor);
    }
}
