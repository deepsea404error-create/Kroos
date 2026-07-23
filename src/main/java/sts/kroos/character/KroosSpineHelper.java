package sts.kroos.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine38.AnimationState;
import com.esotericsoftware.spine38.AnimationStateData;
import com.esotericsoftware.spine38.Skeleton;
import com.esotericsoftware.spine38.SkeletonData;
import com.esotericsoftware.spine38.SkeletonBinary;
import com.esotericsoftware.spine38.SkeletonJson;
import com.esotericsoftware.spine38.SkeletonRenderer;
import com.megacrit.cardcrawl.core.Settings;

public class KroosSpineHelper {
    private TextureAtlas atlas;
    private Skeleton skeleton;
    private AnimationState state;
    private AnimationStateData stateData;

    private static final PolygonSpriteBatch psb = new PolygonSpriteBatch();
    private static final SkeletonRenderer sr = new SkeletonRenderer();
    static {
        sr.setPremultipliedAlpha(true);
    }

    private final float scale;

    public KroosSpineHelper(String atlasPath, String skeletonPath, float scale) {
        this.scale = scale;
        loadSpine(atlasPath, skeletonPath);
    }

    private void loadSpine(String atlasPath, String skeletonPath) {
        atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
        float renderScale = Settings.renderScale / scale;
        SkeletonData data;

        if (skeletonPath.endsWith(".json")) {
            SkeletonJson json = new SkeletonJson(atlas);
            json.setScale(renderScale);
            data = json.readSkeletonData(Gdx.files.internal(skeletonPath));
        } else if (skeletonPath.endsWith(".skel")) {
            SkeletonBinary binary = new SkeletonBinary(atlas);
            binary.setScale(renderScale);
            data = binary.readSkeletonData(Gdx.files.internal(skeletonPath));
        } else {
            SkeletonJson json = new SkeletonJson(atlas);
            json.setScale(renderScale);
            data = json.readSkeletonData(Gdx.files.internal(skeletonPath));
        }

        skeleton = new Skeleton(data);
        skeleton.setColor(Color.WHITE);
        stateData = new AnimationStateData(data);
        state = new AnimationState(stateData);
        state.setAnimation(0, "Idle", true);

        stateData.setMix("Idle", "Attack", 0.1f);
        stateData.setMix("Attack", "Idle", 0.1f);
        stateData.setMix("Idle", "Die", 0.1f);
    }

    public void playIdle() {
        if (state != null) state.setAnimation(0, "Idle", true);
    }

    public void playAttack() {
        if (state != null) {
            state.setAnimation(0, "Attack", false);
            state.addAnimation(0, "Idle", true, 0f);
        }
    }

    public void playDeath() {
        if (state != null) state.setAnimation(0, "Die", false);
    }

    public void update() {
        if (state != null) state.update(Gdx.graphics.getDeltaTime());
    }

    public void applyAnimation() {
        if (state != null && skeleton != null) {
            state.apply(skeleton);
            skeleton.updateWorldTransform();
        }
    }

    public Skeleton getSkeleton() { return skeleton; }

    public void setPosition(float x, float y) {
        if (skeleton != null) skeleton.setPosition(x, y);
    }

    public void setColor(Color color) {
        if (skeleton != null) skeleton.setColor(color);
    }

    public void setFlip(boolean flipX, boolean flipY) {
        if (skeleton != null) {
            float absX = Math.abs(skeleton.getScaleX());
            float absY = Math.abs(skeleton.getScaleY());
            skeleton.setScaleX(flipX ? -absX : absX);
            skeleton.setScaleY(flipY ? -absY : absY);
        }
    }

    public void render(SpriteBatch sb) {
        if (skeleton == null) return;
        sb.end();
        psb.begin();
        sr.draw(psb, skeleton);
        psb.end();
        sb.begin();
    }

    public void dispose() {
        if (atlas != null) atlas.dispose();
        atlas = null;
        skeleton = null;
        state = null;
        stateData = null;
    }
}
