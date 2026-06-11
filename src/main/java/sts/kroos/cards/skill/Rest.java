package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.DozePower;

/**
 * 休息 - 1费, 获得 8 点格挡, 进入浅眠状态。
 *   - 强化: 12 格挡
 */
public class Rest extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Rest";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/rest.png";

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 4;

    public Rest() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = BLOCK;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        if (!p.hasPower(DozePower.POWER_ID)) {
            addToBot(new ApplyPowerAction(p, p, new DozePower(p)));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Rest(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            upgradeDescription();
        }
    }
}
