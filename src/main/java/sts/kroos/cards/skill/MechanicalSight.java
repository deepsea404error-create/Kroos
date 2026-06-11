package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FocusPower;

/**
 * 机械瞄具 - 0费, 获得 4 点活力 (下回合 +4 能量)。
 *   - 寒芒: 消耗 1 层寒芒, 获得 1 层专注
 *   - 强化: 6 点活力
 */
public class MechanicalSight extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":MechanicalSight";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/mechanical_sight.png";

    private static final int COST = 0;
    private static final int VITALITY = 4;
    private static final int UPGRADE_VITALITY = 2;

    public MechanicalSight() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = VITALITY;
        this.magicNumber = VITALITY;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new EnergizedPower(p, this.magicNumber), this.magicNumber));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(p, p, new FocusPower(p, 1), 1));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new MechanicalSight(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_VITALITY);
            upgradeDescription();
        }
    }
}
