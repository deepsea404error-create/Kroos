package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.ChargePower;
import sts.kroos.powers.DozePower;
import sts.kroos.powers.FocusPower;

/**
 * 溃缩 - 1费。
 *   - 若处于浅眠, 立即获得 1 (强化 2) 层蓄势并退出浅眠
 *     (即蓄势放电效果立刻生效, 强化值即"额外充能再放")
 *   - 寒芒: 消耗 1 层寒芒, 获得 2 层专注
 */
public class Collapse extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Collapse";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/collapse.png";

    private static final int COST = 1;
    private static final int CHARGE = 1;
    private static final int UPGRADE_CHARGE = 1;
    private static final int FOCUS = 2;

    public Collapse() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = CHARGE;
        this.magicNumber = CHARGE;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(DozePower.POWER_ID)) {
            // 额外蓄势 + 退出浅眠 (链式触发蓄势放电)
            addToBot(new ApplyPowerAction(p, p,
                    new ChargePower(p, this.magicNumber), this.magicNumber));
            addToBot(new RemoveSpecificPowerAction(p, p, DozePower.POWER_ID));
        }
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(p, p, new FocusPower(p, FOCUS), FOCUS));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Collapse(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CHARGE);
            upgradeDescription();
        }
    }
}
