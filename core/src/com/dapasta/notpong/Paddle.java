package com.dapasta.notpong;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Paddle {
    private Side side;
    private boolean userControlled;

    private Rectangle rect;
    private static final float RELATIVE_WIDTH = 0.01f;
    private static final float RELATIVE_HEIGHT = 0.175f;

    private static final float SPEED = 4f;

    public Paddle(Side side, boolean userControlled) {
        this.side = side;
        this.userControlled = userControlled;

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
        if (userControlled) {
            //Make sure screen is touched when on mobile
            if((Gdx.app.getType() == Application.ApplicationType.Android && Gdx.input.isTouched())
                    || Gdx.app.getType() != Application.ApplicationType.Android) {

                float yInput = Gdx.graphics.getHeight() - Gdx.input.getY();
                Vector2 center = new Vector2();
                center = rect.getCenter(center);

                //Check if paddle goes out of bounds
                if ((yInput > center.y && rect.getY() + rect.getHeight() < Gdx.graphics.getHeight())
                        || (yInput < center.y && rect.getY() > 0)) {
                    float dY = yInput - center.y;

                    //Accelerate to mouse/finger
                    rect.setCenter(center.x, center.y + (dY * SPEED * delta));

                }c
                app.socket.emit("updatePaddle", yInput * 100 / Gdx.graphics.getHeight());
            }
        }
    }

    public void setPosition(float pos) {
        Vector2 center = new Vector2();
        center = rect.getCenter(center);
        rect.setCenter(center.x, pos);
    }
}
