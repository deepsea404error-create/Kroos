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
 * 一瞬监听 — 内部一次性辅助 power, 不进入本地化系统。
 *
 * 含义: 本回合内, 每打出 1 张攻击牌, 对一名随机存活敌人施加 1 层破绽。
 * 回合结束自动移除。
 */
public class OneInstantListenerPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":OneInstantListener";
    public static final String NAME = "一瞬";

    private static final String DUMMY_ICON = KroosMod.RES_ROOT + "powers/hit_small.png";

    public OneInstantListenerPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1; // 不计数, 仅作 token
        this.type = PowerType.BUFF;
        // Internal power, no real icon needed but renderIcons() requires this.img to be non-null
        Texture dummy = TextureLoader.getTexture(DUMMY_ICON);
        if (dummy != null) this.img = dummy;
        updateDescription();
    }

    // 内部不可见 power, 无需图标; 阻止 ApplyPowerAction 自动调用 flash() 时 NPE
    @Override
    public void flash() {}

    @Override
    public void updateDescription() {
        this.description = "本回合每打出 1 张[R]攻击[]牌, 对随机敌人施加 1 层[R]破绽[]。";
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
