package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 天坠之火 Power。
 * 含义: [中的]效果造成伤害增加 X 点。
 *
 * 实现: 仅作 token 暴露 amount; 由 HitPower.atStartOfTurn 在计算 tick 伤害时主动读取。
 * 选择此模式而非 patch HitPower 内部: 避免反向耦合 — 多个加成效果可累加在
 * SkyfireFlame 与未来类似 power 上, HitPower 一处汇总读取即可。
 */
public class SkyfireFlamePower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":SkyfireFlame";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/skyfire_flame_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/skyfire_flame_small.png";

    public SkyfireFlamePower(AbstractCreature owner, int amount) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner; this.amount = amount;
        this.type = PowerType.BUFF;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 84, 84);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 36, 36);
        updateDescription();
    }

    @Override
    public void updateDescription() { this.description = DESC[0] + this.amount + DESC[1]; }
}
