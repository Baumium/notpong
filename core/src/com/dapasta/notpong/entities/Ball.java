package com.dapasta.notpong.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dapasta.notpong.Application;

public class Ball {
    private Vector2 position;
    private float radius;
    private boolean reverseX;
    private boolean reverseY;

    public Ball(float radius, boolean reverseX, boolean reverseY) {
        this.radius = radius;
        position = new Vector2();
        resetPosition();

        this.reverseX = reverseX;
        this.reverseY = reverseY;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void resetPosition() {
        position.set(Gdx.graphics.getWidth() / 2 - radius, Gdx.graphics.getHeight() / 2 - radius);
    }

    public void setPosition(float x, float y) {
        // Adjust ball position to multiple perspectives
        if (reverseX) {
            x = Gdx.graphics.getWidth() - x;
        }
        if (reverseY) {
            y = Gdx.graphics.getHeight() - y;
        }

        position.set(x, y);
    }

    public void draw(ShapeRenderer renderer) {
        renderer.setColor(Color.BLACK);
        renderer.circle(position.x, position.y, radius);
    }
}
