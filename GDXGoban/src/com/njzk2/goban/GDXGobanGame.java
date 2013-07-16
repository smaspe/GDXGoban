package com.njzk2.goban;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GDXGobanGame implements ApplicationListener {
	private static final int WHITE = -1;
	private static final int EMPTY = 0;
	private static final int BLACK = 1;
	private static int WIDTH;
	private static int HEIGHT;
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
	public static final float unitScale = 1 / 32f;
	private TextureRegion[] whiteTexture;
	private TextureRegion[] blackTexture;
	int[][] board;
	int currentPlayer = BLACK;

	@Override
	public void create() {
		whiteTexture = TextureRegion.split(new Texture("data/igWeiss.png"), 32, 32)[0];
		blackTexture = TextureRegion.split(new Texture("data/igSchwarz.png"), 32, 32)[0];

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		TiledMap map = new TmxMapLoader().load("data/goban19.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, unitScale);

		camera = new OrthographicCamera();
		camera.setToOrtho(true, w * unitScale, h * unitScale);
		WIDTH = ((TiledMapTileLayer) map.getLayers().get(0)).getWidth();
		HEIGHT = ((TiledMapTileLayer) map.getLayers().get(0)).getHeight();
		board = new int[WIDTH][HEIGHT];
		camera.position.x = WIDTH / 2;
		camera.position.y = HEIGHT / 2;
		camera.update();
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new ZoomMoveKeyProcessor(camera));
		multiplexer.addProcessor(new GestureDetector(new ZoomPanDetector(camera)));
		multiplexer.addProcessor(new GestureDetector(new PutStoneDetector(this, camera)));
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		renderer.setView(camera);
		renderer.render();
		SpriteBatch batch = renderer.getSpriteBatch();
		batch.begin();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == WHITE) {
					batch.draw(whiteTexture[2], i, j, 1, 1);
				} else if (board[i][j] == BLACK) {
					batch.draw(blackTexture[2], i, j, 1, 1);
				}
			}
		}
		batch.end();
	}

	public void play(int x, int y) {
		if (board[x][y] == EMPTY) {
			board[x][y] = currentPlayer;
			currentPlayer *= -1;
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
}
