package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FrostEchoPower;

/**
 * 简易工事 - 1费, 获得 8 点格挡, 下回合获得 2 层寒芒。
 *   - 强化: 11 格挡, 3 层寒芒
 */
public class SimpleFortification extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":SimpleFortification";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/simple_fortification.png";

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 3;
    private static final int FROST_NEXT = 2;
    private static final int UPGRADE_FROST_NEXT = 1;

    public SimpleFortification() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = FROST_NEXT;
        this.magicNumber = FROST_NEXT;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(p, p,
                new FrostEchoPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new SimpleFortification(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_FROST_NEXT);
            upgradeDescription();
        }
    }
}
