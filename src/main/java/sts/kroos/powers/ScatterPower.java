package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 散射 Power (不可堆叠)。
 *
 * 含义: [箭矢]牌能对非主目标的其他敌人造成 50% 伤害。
 *
 * 实现方式: 不在 power 内 hook 伤害, 由 AbstractKroosCard.scatterIfArrow
 * 在箭矢牌 useImpl 中显式调用。原因: 散射伤害必须使用与主目标一致的伤害值
 * (含暴击/箭矢改良等所有修饰), 卡侧调用最直接。
 */
public class ScatterPower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":Scatter";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/scatter_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/scatter_small.png";

    public ScatterPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1; // 不可堆叠
        this.type = PowerType.BUFF;
        loadIcons();
        updateDescription();
    }

    private void loadIcons() {
        Texture large = TextureLoader.getTexture(ICON_LARGE);
        Texture small = TextureLoader.getTexture(ICON_SMALL);
        if (large != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(large, 0, 0, 128, 128);
        if (small != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(small, 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
