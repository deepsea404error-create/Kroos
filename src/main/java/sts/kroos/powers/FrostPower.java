package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 寒芒 Power。
 *
 * 含义: 当前拥有【X】层寒芒。消耗寒芒可触发卡牌额外效果。
 * 类型: 我方-正面 / 可堆叠 / 持久(不在回合结束清零)。
 *
 * 实际消耗由各张卡牌通过 AbstractKroosCard.consumeFrost(amount) 触发,
 * 该方法内部使用 ReducePowerAction 减少本 power 层数。
 *
 * 心之痕、寒芒涌动等 power 的效果由各自实现, 不在此处耦合。
 */
public class FrostPower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":Frost";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    private static final String ICON_LARGE_PATH = KroosMod.RES_ROOT + "powers/frost_large.png";
    private static final String ICON_SMALL_PATH = KroosMod.RES_ROOT + "powers/frost_small.png";

    public FrostPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;

        Texture large = TextureLoader.getTexture(ICON_LARGE_PATH);
        Texture small = TextureLoader.getTexture(ICON_SMALL_PATH);
        if (large != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(large, 0, 0, 128, 128);
        if (small != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(small, 0, 0, 48, 48);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
