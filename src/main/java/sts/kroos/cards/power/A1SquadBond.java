package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.actions.A1DiscoveryAction;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.A1SquadBondPower;
import sts.kroos.util.A1SquadFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A1 小队的羁绊 - 2费, 能力 (金卡)。
 *   - 每消耗 4 点寒芒, 随机获得 1 张 A1 小队卡 (强化: 升级版)
 *   - 寒芒: 消耗 1 层寒芒, 在 3 张 A1 小队卡中选 1 (强化: 选 2) 张加入手牌, 本回合耗能变 0
 *
 * 三选机制用原版 DiscoveryAction (它自动将选中牌设为 0 费本回合)。
 */
public class A1SquadBond extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":A1SquadBond";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/a1_squad_bond.png";

    private static final int COST = 2;
    private static final int BOND_AMOUNT = 1;

    public A1SquadBond() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = BOND_AMOUNT;
        this.magicNumber = BOND_AMOUNT;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new A1SquadBondPower(p, this.magicNumber, this.upgraded),
                this.magicNumber));

        if (canConsumeFrost(1)) {
            consumeFrost(1);
            List<AbstractCard> candidates = A1SquadFactory.randomA1Cards(3);
            int picks = this.upgraded ? 2 : 1;
            addToBot(new A1DiscoveryAction(new ArrayList<>(candidates), picks));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new A1SquadBond(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeDescription();
        }
    }
}
