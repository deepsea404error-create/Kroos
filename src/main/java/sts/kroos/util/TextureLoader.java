package sts.kroos.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;

import java.util.HashMap;

/**
 * 贴图加载缓存工具。所有路径均为相对 resources 的真实路径，不使用占位符。
 */
public class TextureLoader {
    private static final HashMap<String, Texture> textures = new HashMap<>();

    public static Texture getTexture(final String path) {
        Texture t = textures.get(path);
        if (t == null) {
            try {
                t = new Texture(path);
                textures.put(path, t);
            } catch (Exception e) {
                if (Settings.isDebug) {
                    System.err.println("[KroosMod] 贴图缺失: " + path);
                }
                return null;
            }
        }
        return t;
    }
}
