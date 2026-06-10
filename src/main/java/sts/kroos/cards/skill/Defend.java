package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 防御(初始) - 1费, 获得5点格挡, 升级后8点。
 */
public class Defend extends AbstractKroosCard {

    public static final String ID = KroosMod.MOD_ID + ":Defend";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/defend.png";

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_BLOCK = 3;

    public Defend() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
    }

    @Override
    public AbstractKroosCard makeCopy() {
        return new Defend();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            upgradeDescription();
        }
    }
}
