package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FrostPower;

/**
 * 势如破竹 - 0费, 消耗。
 *   - 寒芒: 消耗 4 层寒芒, 获得 7 (强化 9) 层寒芒
 */
public class Unstoppable extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Unstoppable";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/unstoppable.png";

    private static final int COST = 0;
    private static final int FROST_GAIN = 7;
    private static final int UPGRADE_FROST_GAIN = 2;

    public Unstoppable() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = FROST_GAIN;
        this.magicNumber = FROST_GAIN;
        this.exhaust = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        if (canConsumeFrost(4)) {
            consumeFrost(4);
            addToBot(new ApplyPowerAction(p, p,
                    new FrostPower(p, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Unstoppable(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FROST_GAIN);
            upgradeDescription();
        }
    }
}
