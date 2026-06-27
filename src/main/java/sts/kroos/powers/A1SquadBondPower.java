package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.A1SquadFactory;
import sts.kroos.util.TextureLoader;

/**
 * A1 小队的羁绊 Power。
 * 含义: 每消耗 4 点寒芒, 随机获得 X 张 A1 小队卡 (强化后获得升级版)。
 *
 * 由 AbstractKroosCard.consumeFrost 在结算时调用 notifyConsumed(logicalAmount)。
 * 累计器 consumedAccum 跨整场战斗持久, 每达到 4 触发 amount 张 A1 卡获取。
 *
 * upgradeGained: 决定生成的 A1 卡是否预先升级 (强化版能力)。
 */
public class A1SquadBondPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":A1SquadBond";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/a1_bond_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/a1_bond_small.png";

    public static final int THRESHOLD = 4;

    private int consumedAccum = 0;
    private final boolean upgradeGained;

    public A1SquadBondPower(AbstractCreature owner, int amount, boolean upgradeGained) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner; this.amount = amount;
        this.type = PowerType.BUFF;
        this.upgradeGained = upgradeGained;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 128, 128);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 48, 48);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESC[0] + this.amount + DESC[1] + " (已累计 " + consumedAccum + "/" + THRESHOLD + ")";
    }

    /** 由卡侧 consumeFrost 调用 */
    public void notifyConsumed(int logicalAmount) {
        if (logicalAmount <= 0 || this.amount <= 0) return;
        consumedAccum += logicalAmount;
        int triggers = consumedAccum / THRESHOLD;
        if (triggers > 0) {
            consumedAccum -= triggers * THRESHOLD;
            this.flash();
            int total = triggers * this.amount;
            for (int i = 0; i < total; i++) {
                AbstractCard c = A1SquadFactory.randomA1();
                if (upgradeGained) c.upgrade();
                AbstractDungeon.actionManager.addToBottom(
                        new MakeTempCardInHandAction(c, false));
            }
        }
        updateDescription();
    }
}
