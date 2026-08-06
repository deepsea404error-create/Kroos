package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 寒星 - 0费。
 *   - 寒芒: 消耗 3 层寒芒, 获得 2 (强化 3) 点能量
 */
public class FrostStar extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":FrostStar";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/frost_star.png";

    private static final int COST = 0;
    private static final int ENERGY = 2;
    private static final int UPGRADE_ENERGY = 1;

    public FrostStar() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = ENERGY;
        this.magicNumber = ENERGY;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        if (canConsumeFrost(3)) {
            consumeFrost(3);
            addToBot(new GainEnergyAction(this.magicNumber));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new FrostStar(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_ENERGY);
            upgradeDescription();
        }
    }
}
