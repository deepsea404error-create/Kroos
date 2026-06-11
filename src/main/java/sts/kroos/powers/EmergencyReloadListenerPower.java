package sts.kroos.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;

/**
 * 紧急装填监听 — 内部一次性辅助 power, 不进入本地化系统。
 *
 * 含义: 监听接下来 amount 次抽牌, 每抽到 1 张攻击牌就施加 1 层寒芒。
 *       计数耗尽后自移除。
 *
 * 注意:
 *   - 描述置空, 不出现在玩家可见 power 列表的字符串字典中(用 NAME 直接编码)。
 *   - 类型 BUFF, 但实际是中性内部计数, 不会被 power 互动影响。
 */
public class EmergencyReloadListenerPower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":EmergencyReloadListener";
    public static final String NAME = "紧急装填";

    public EmergencyReloadListenerPower(AbstractCreature owner, int draws) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = draws;
        this.type = PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = "下 " + this.amount + " 次抽牌时, 每抽到[R]攻击[]牌获得 1 层[#b0c4de]寒芒[]。";
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (this.amount <= 0 || card == null) return;
        this.amount--;
        if (card.type == AbstractCard.CardType.ATTACK) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    owner, owner, new FrostPower(owner, 1), 1));
        }
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                    owner, owner, POWER_ID));
        } else {
            updateDescription();
        }
    }
}
