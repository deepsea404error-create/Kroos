package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 无明 Power。
 * 含义: 暴击时, 获得 X 点格挡。
 *
 * 检测方式: onUseCard 时如果 card.type==ATTACK 且玩家持有 CriticalPower 且 amount>0,
 * 则视为本次攻击触发了暴击, 获得 amount 点格挡。
 * (CriticalPower 自身的 onUseCard 在同一阶段扣 1 层, 不影响判定。)
 */
public class UnlitPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":Unlit";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/unlit_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/unlit_small.png";

    public UnlitPower(AbstractCreature owner, int amount) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner; this.amount = amount;
        this.type = PowerType.BUFF;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 128, 128);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 48, 48);
        updateDescription();
    }

    @Override
    public void updateDescription() { this.description = DESC[0] + this.amount + DESC[1]; }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount <= 0 || card == null || card.type != AbstractCard.CardType.ATTACK) return;
        AbstractPower cp = owner.getPower(CriticalPower.POWER_ID);
        if (cp == null || cp.amount <= 0) return;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(owner, owner, this.amount));
    }
}
