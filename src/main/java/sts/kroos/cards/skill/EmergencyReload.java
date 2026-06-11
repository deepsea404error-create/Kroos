package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.EmergencyReloadListenerPower;

/**
 * 紧急装填 - 1费, 抽 2 张牌, 每抽到 1 张攻击牌获得 1 层寒芒。
 *   - 寒芒: 消耗 1 层寒芒, 额外抽 1 张
 *   - 强化: 抽 3 张
 *
 * 实现:
 *   - 抽牌前先施加 EmergencyReloadListenerPower(draws), 它会监听后续 draws 次 onCardDraw
 *   - 由于 ApplyPowerAction 与 DrawCardAction 均入队执行, listener 在 DrawCardAction 触发前生效
 */
public class EmergencyReload extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":EmergencyReload";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/emergency_reload.png";

    private static final int COST = 1;
    private static final int DRAW = 2;
    private static final int UPGRADE_DRAW = 1;

    public EmergencyReload() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = DRAW;
        this.magicNumber = DRAW;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int draw = this.magicNumber;
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            draw += 1;
        }
        addToBot(new ApplyPowerAction(p, p,
                new EmergencyReloadListenerPower(p, draw), draw));
        addToBot(new DrawCardAction(p, draw));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new EmergencyReload(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DRAW);
            upgradeDescription();
        }
    }
}
