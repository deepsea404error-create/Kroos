package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.DozePower;

/**
 * 独处 - 1费。
 *   - 若处于浅眠, 抽 3 (强化 4) 张牌
 *   - 否则进入浅眠状态
 *   - 寒芒: 消耗 1 层寒芒, 回复 2 (强化 3) 点生命
 */
public class Solitude extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Solitude";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/solitude.png";

    private static final int COST = 1;
    private static final int DRAW = 3;
    private static final int UPGRADE_DRAW = 1;
    private static final int HEAL = 2;
    private static final int HEAL_UPG = 3;

    public Solitude() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = DRAW;
        this.magicNumber = DRAW;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(DozePower.POWER_ID)) {
            addToBot(new DrawCardAction(p, this.magicNumber));
        } else {
            addToBot(new ApplyPowerAction(p, p, new DozePower(p)));
        }
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new HealAction(p, p, this.upgraded ? HEAL_UPG : HEAL));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Solitude(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DRAW);
            upgradeDescription();
        }
    }
}
