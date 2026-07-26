package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.util.TextureLoader;

/**
 * 寒芒形态 Power。
 * 含义: 每回合前 X 张攻击牌重复打出1次。
 *
 * 实现: 参考原版 DoubleTapPower，使用 CardQueueItem 机制重放卡牌。
 */
public class FrostFormPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":FrostForm";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/frostform_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/frostform_small.png";

    private int triggersLeft;

    public FrostFormPower(AbstractCreature owner, int amount) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner; this.amount = amount;
        this.type = PowerType.BUFF;
        this.triggersLeft = amount;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 84, 84);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESC[0] + this.amount + DESC[1];
    }

    @Override
    public void atStartOfTurn() {
        triggersLeft = this.amount;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.purgeOnUse || card.type != AbstractCard.CardType.ATTACK || triggersLeft <= 0) return;

        // 获取目标怪物
        AbstractMonster target = null;
        if (action.target instanceof AbstractMonster) {
            target = (AbstractMonster) action.target;
        }
        if (target == null || target.isDeadOrEscaped()) return;

        triggersLeft--;
        this.flash();

        // 参考原版 DoubleTapPower：使用 makeSameInstanceOf + CardQueueItem 重放
        AbstractCard copy = card.makeSameInstanceOf();
        copy.purgeOnUse = true;
        AbstractDungeon.player.limbo.addToTop(copy);
        AbstractDungeon.actionManager.cardQueue.add(
                new CardQueueItem(copy, target, card.energyOnUse, true, true));
    }
}
