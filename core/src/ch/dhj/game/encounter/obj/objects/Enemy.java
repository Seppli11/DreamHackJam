package ch.dhj.game.encounter.obj.objects;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.player.AnimationSet;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sebastian on 30.09.2017.
 */
public abstract class Enemy extends Figure{
	public Enemy(Vector2 position, String name, AnimationSet animationSet) {
		super(position, name, animationSet);
	}

	public abstract Action[] getActions();
}