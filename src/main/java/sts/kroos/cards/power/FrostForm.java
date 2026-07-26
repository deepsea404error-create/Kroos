package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FrostFormPower;

/**
 * 寒芒形态 - 2费(强化: 保留)。
 *   - 每回合前 2 (强化 3) 张攻击牌重复打出1次。
 */
public class FrostForm extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":FrostForm";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/frostform.png";

    private static final int COST = 2;
    private static final int TRIGGERS = 2;
    private static final int TRIGGERS_UPG = 3;

    public FrostForm() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = TRIGGERS;
        this.magicNumber = TRIGGERS;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FrostFormPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new FrostForm(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(TRIGGERS_UPG - TRIGGERS);
            this.selfRetain = true;
            upgradeDescription();
        }
    }
}
