package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.UnlitPower;

/**
 * 无明 - 1费, 暴击时获得 3 (强化 4) 点格挡。
 *   - 强化: 固有
 */
public class Unlit extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Unlit";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/unlit.png";

    private static final int COST = 1;
    private static final int BLOCK = 3;
    private static final int BLOCK_UPG = 4;

    public Unlit() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BLOCK;
        this.magicNumber = BLOCK;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new UnlitPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Unlit(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(BLOCK_UPG - BLOCK);
            this.isInnate = true;
            upgradeDescription();
        }
    }
}
