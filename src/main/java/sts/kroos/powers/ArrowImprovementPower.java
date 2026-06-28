package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 箭矢改良 Power。
 *
 * 含义: [箭矢]牌伤害增加 X 点。
 *
 * 实现方式: 不在 power 内 hook 伤害, 而是由 AbstractKroosCard.applyPowers
 * 和 calculateCardDamage 读取本 power 的层数后给 isArrow 卡的 this.damage 加成。
 * 原因: 仅作用于"箭矢"标签卡, 需要按卡判断, 由卡侧处理最简单。
 */
public class ArrowImprovementPower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":ArrowImprovement";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/arrow_improvement_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/arrow_improvement_small.png";

    public ArrowImprovementPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        loadIcons();
        updateDescription();
    }

    private void loadIcons() {
        Texture large = TextureLoader.getTexture(ICON_LARGE);
        Texture small = TextureLoader.getTexture(ICON_SMALL);
        if (large != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(large, 0, 0, 84, 84);
        if (small != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(small, 0, 0, 32, 32);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
