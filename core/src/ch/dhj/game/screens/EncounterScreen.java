package ch.dhj.game.screens;

import ch.dhj.game.encounter.TurnManager;
import ch.dhj.game.encounter.obj.ParentObject;
import ch.dhj.game.encounter.obj.objects.Enemy;
import ch.dhj.game.encounter.obj.objects.Player;
import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Arrays;

import static ch.dhj.game.TexturesConst.ENCOUNTER_1_BG;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class EncounterScreen implements Screen {
	private AssetManager assetManager;
	private SpriteBatch batch;

	private OrthographicCamera camera;

	private TiledMap map;
	private MapRenderer mapRenderer;

	private Sprite background;

	private EncounterConfig config;

	private ParentObject parentObject;
	private Viewport viewport;

	public EncounterScreen(Player player, EncounterConfig config, AssetManager assetManager, SpriteBatch batch) {
		this.config = config;
		this.assetManager = assetManager;
		this.batch = batch;

		viewport = new StretchViewport(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_WIDTH);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_WIDTH);

		//asset loading
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load(config.map, TiledMap.class);
		assetManager.load(config.background, Texture.class);
		assetManager.finishLoading();

		//map loading
		map = assetManager.get(config.map);
		mapRenderer = new OrthogonalTiledMapRenderer(map, 10);

		//background loading
		background = new Sprite((Texture) assetManager.get(config.background, Texture.class));
		background.setPosition(0, 0);
		background.setSize(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_WIDTH);

		parentObject = new ParentObject(this);
		parentObject.add(player);
		for(Enemy e : config.enemies)
			parentObject.add(e);
		parentObject.init();
	}


	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		//render stuff
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
			background.draw(batch);
		batch.end();

		mapRenderer.setView(camera);
		mapRenderer.render();

		batch.begin();
			parentObject.render(delta, batch);
			//batch.draw(new Texture(Gdx.files.internal("textures/texture.png")), 0, 0, 1000, 1000);
		batch.end();
		parentObject.renderUi(delta, batch);



		parentObject.getTurnManager().update();
	}

	@Override
	public void resize(int width, int height) {
		parentObject.resize(width, height);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		parentObject.dispose();
	}


	public Viewport getViewport() {
		return viewport;
	}

	public static class EncounterConfig {
		public int id;
		public String background;
		public String map;
		public Array<Enemy> enemies;

		public EncounterConfig(int id, String background, String map, Enemy[] enemies) {
			this.id = id;
			this.background = background;
			this.map = map;
			this.enemies = new Array(enemies);
		}
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public TiledMap getMap() {
		return map;
	}

	public MapRenderer getMapRenderer() {
		return mapRenderer;
	}

	public Sprite getBackground() {
		return background;
	}

	public EncounterConfig getConfig() {
		return config;
	}

	public ParentObject getParentObject() {
		return parentObject;
	}
}
