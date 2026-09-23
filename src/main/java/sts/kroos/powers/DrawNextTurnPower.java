package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 紧急补给 Power (急速转移寒芒效果)。
 * 含义: 下回合开始时额外抽 X 张牌, 抽完后移除自身 (一次性)。
 *
 * 与常驻的物资补给 (ExtraDrawPower) 不同, 本 Power 在触发后立即移除,
 * 避免抽牌加成持续到后续回合。
 */
public class DrawNextTurnPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":DrawNextTurn";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/extra_draw_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/extra_draw_small.png";

    public DrawNextTurnPower(AbstractCreature owner, int amount) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner; this.amount = amount;
        this.type = PowerType.BUFF;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 84, 84);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 36, 36);
        updateDescription();
    }

    @Override
    public void updateDescription() { this.description = DESC[0] + this.amount + DESC[1]; }

    @Override
    public void atStartOfTurnPostDraw() {
        if (this.amount <= 0) return;
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(owner, this.amount));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }
}
