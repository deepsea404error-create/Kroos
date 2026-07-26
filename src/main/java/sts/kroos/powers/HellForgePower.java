package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 狱淬 Power。
 * 含义: 攻击带有"中的"的敌人时，额外造成 X 点伤害。
 */
public class HellForgePower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":HellForge";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/hellforge_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/hellforge_small.png";

    public HellForgePower(AbstractCreature owner, int amount) {
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
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK) return;
        if (this.amount <= 0) return;
        // Get the target monster from the action
        AbstractMonster target = null;
        if (action.target instanceof AbstractMonster) {
            target = (AbstractMonster) action.target;
        }
        if (target == null || target.isDeadOrEscaped()) return;
        if (!target.hasPower(HitPower.POWER_ID)) return;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                target,
                new DamageInfo(AbstractDungeon.player, this.amount, DamageInfo.DamageType.HP_LOSS),
                AbstractGameAction.AttackEffect.FIRE));
    }
}
