package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.CamouflagePower;
import sts.kroos.powers.CriticalPower;

/**
 * 无痕 - 2费, 消耗。
 *   - 获得 1 (强化 2) 层暴击
 *   - 获得 2 (强化 3) 层迷彩
 *   - 寒芒: 消耗 1 层寒芒, 获得 1 层迷彩
 */
public class Traceless extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Traceless";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/traceless.png";

    private static final int COST = 2;
    private static final int CRIT = 1;
    private static final int UPGRADE_CRIT = 1;
    private static final int CAMO = 2;
    private static final int UPGRADE_CAMO = 1;
    private static final int FROST_CAMO = 1;

    public Traceless() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = CAMO;
        this.magicNumber = CAMO;
        this.exhaust = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int crit = this.upgraded ? (CRIT + UPGRADE_CRIT) : CRIT;
        addToBot(new ApplyPowerAction(p, p, new CriticalPower(p, crit), crit));
        addToBot(new ApplyPowerAction(p, p,
                new CamouflagePower(p, this.magicNumber), this.magicNumber));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(p, p,
                    new CamouflagePower(p, FROST_CAMO), FROST_CAMO));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Traceless(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CAMO);
            upgradeDescription();
        }
    }
}
