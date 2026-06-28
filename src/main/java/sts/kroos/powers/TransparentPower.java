package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 通明 Power。
 * 含义: 每消耗 5 层寒芒, 获得 X 点能量。
 *
 * 由 AbstractKroosCard.consumeFrost 在结算时调用 notifyConsumed(logicalAmount)。
 * 累计器 consumedAccum 跨整场战斗持久, 每达到 5 个累计单位触发一次能量获取。
 *
 * 累计基于"逻辑消耗量"(用户意图扣的层数), 不受心之痕的减免影响 — 即使
 * 实际只扣了 0 层, 也算 1 个消耗事件的 1 层。
 */
public class TransparentPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":Transparent";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/transparent_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/transparent_small.png";

    public static final int THRESHOLD = 5;

    private int consumedAccum = 0;

    public TransparentPower(AbstractCreature owner, int amount) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner; this.amount = amount;
        this.type = PowerType.BUFF;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 84, 84);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESC[0] + this.amount + DESC[1] + " (已累计 " + consumedAccum + "/" + THRESHOLD + ")";
    }

    /** 由卡侧 consumeFrost 调用; 累计 X 层逻辑消耗, 每达 5 触发 amount 能量获取 */
    public void notifyConsumed(int logicalAmount) {
        if (logicalAmount <= 0 || this.amount <= 0) return;
        consumedAccum += logicalAmount;
        int gains = consumedAccum / THRESHOLD;
        if (gains > 0) {
            consumedAccum -= gains * THRESHOLD;
            this.flash();
            AbstractDungeon.actionManager.addToBottom(
                    new GainEnergyAction(this.amount * gains));
        }
        updateDescription();
    }
}
