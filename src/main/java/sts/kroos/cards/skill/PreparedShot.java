package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 预备射击(初始) - 1费, 获得2层寒芒, 升级后3层。
 * 寒芒 power 尚未实现, 这里留空实现, 待 power 接入后填充。
 */
public class PreparedShot extends AbstractKroosCard {

    public static final String ID = KroosMod.MOD_ID + ":PreparedShot";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/prepared_shot.png";

    private static final int COST = 1;
    public static final int FROST = 2;
    public static final int UPGRADE_FROST = 1;

    public int magicNumberBase = FROST;

    public PreparedShot() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        this.baseMagicNumber = FROST;
        this.magicNumber = FROST;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // TODO: 接入寒芒 power 后调用 addToBot(new ApplyPowerAction(p, p, new FrostPower(p, magicNumber), magicNumber));
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
