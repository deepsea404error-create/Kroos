package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 一瞬监听 — 本回合内, 每打出 1 张攻击牌, 对一名随机存活敌人施加 1 层破绽。
 * 回合结束自动移除。
 */
public class OneInstantListenerPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":OneInstantListener";
    public static final String NAME = "一瞬";
    private static final String IL = KroosMod.RES_ROOT + "powers/one_instant_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/one_instant_small.png";

    public OneInstantListenerPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1; // 不计数, 仅作 token
        this.type = PowerType.BUFF;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 84, 84);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 36, 36);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = "本回合每打出 1 张攻击牌，对随机敌人施加 1 层破绽。";
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card == null || card.type != AbstractCard.CardType.ATTACK) return;
        AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
        if (target == null) return;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                target, owner, new FlawPower(target, 1), 1));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) return;
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                owner, owner, POWER_ID));
    }
}
