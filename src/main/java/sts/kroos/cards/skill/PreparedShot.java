package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FrostPower;

/**
 * 预备射击(初始) - 1费, 获得2层寒芒, 升级后3层。
 */
public class PreparedShot extends AbstractKroosCard {

    public static final String ID = KroosMod.MOD_ID + ":PreparedShot";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/prepared_shot.png";

    private static final int COST = 1;
    public static final int FROST = 2;
    public static final int UPGRADE_FROST = 1;

    public PreparedShot() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        this.baseMagicNumber = FROST;
        this.magicNumber = FROST;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new FrostPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() {
        return new PreparedShot();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FROST);
            upgradeDescription();
        }
    }
}

