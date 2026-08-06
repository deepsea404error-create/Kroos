package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 心之痕 Power。
 * 含义: 每回合前 X 次触发寒芒效果时, 消耗的寒芒 -1。
 *
 * 由 AbstractKroosCard.consumeFrost 在结算前调用 tryDiscount() 询问本 power
 * 是否需要减免。本 power 仅做计数与回报, 不直接操作 FrostPower 层数。
 *
 * 每回合开始重置 triggersLeft 为 amount。
 */
public class HeartScarPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":HeartScar";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/heart_scar_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/heart_scar_small.png";

    private int triggersLeft;

    public HeartScarPower(AbstractCreature owner, int amount) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner; this.amount = amount;
        this.type = PowerType.BUFF;
        this.triggersLeft = amount;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 84, 84);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 36, 36);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESC[0] + this.amount + DESC[1] + " (本回合剩余 " + triggersLeft + " 次)";
    }

    @Override
    public void atStartOfTurn() {
        triggersLeft = this.amount;
        updateDescription();
    }

    /**
     * 申请减免。返回实际应扣的寒芒层数(0 表示完全免费)。
     * 若 triggersLeft > 0 则消耗 1 次计数并返回 max(0, requested-1), 否则原值返回。
     */
    public int tryDiscount(int requested) {
        if (triggersLeft <= 0 || requested <= 0) return requested;
        triggersLeft--;
        this.flash();
        updateDescription();
        return Math.max(0, requested - 1);
    }
}
