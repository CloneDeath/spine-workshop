
package com.esotericsoftware.spine.workshop;

import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class F_AnimationStates extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer renderer;

	TextureAtlas atlas;
	Skeleton skeleton;
	Animation walkAnimation;
	Animation jumpAnimation;
	Bone root;
	AnimationState state;

	public void create () {
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();

		atlas = new TextureAtlas(Gdx.files.internal("spineboy/spineboy.atlas"));
		SkeletonJson json = new SkeletonJson(atlas);
		SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("spineboy/spineboy-skeleton.json"));
		walkAnimation = json.readAnimation(Gdx.files.internal("spineboy/spineboy-walk.json"), skeletonData);
		jumpAnimation = json.readAnimation(Gdx.files.internal("spineboy/spineboy-jump.json"), skeletonData);

		state = new AnimationState();
		state.setMixing(walkAnimation, jumpAnimation, 0.4f);
		state.setAnimation(walkAnimation, true);

		skeleton = new Skeleton(skeletonData);

		root = skeleton.getRootBone();
		root.setX(250);
		root.setY(20);

		skeleton.updateWorldTransform();
	}

	public void render () {
		state.update(Gdx.graphics.getDeltaTime() / 6);

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();

		state.apply(skeleton);
		if (state.getTime() > 1 && state.getAnimation() == walkAnimation) state.setAnimation(jumpAnimation, false);
		skeleton.updateWorldTransform();
		skeleton.draw(batch);

		batch.end();
	}

	public void resize (int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		renderer.setProjectionMatrix(batch.getProjectionMatrix());
	}

	public void dispose () {
		atlas.dispose();
	}

	public static void main (String[] args) throws Exception {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "AnimationStates - Spine";
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new F_AnimationStates(), config);
	}
}
