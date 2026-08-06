package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.HeartbeatPower;

/**
 * 心音 - 2费(强化 1费), 每回合从前 1 张卡牌获得的格挡翻倍。
 *   - 寒芒: 消耗 1 层寒芒, 获得 6 点防御
 */
public class Heartbeat extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Heartbeat";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/heartbeat.png";

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int TRIGGERS = 1;
    private static final int FROST_BLOCK = 6;

    public Heartbeat() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = TRIGGERS;
        this.magicNumber = TRIGGERS;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new HeartbeatPower(p, this.magicNumber), this.magicNumber));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new GainBlockAction(p, p, FROST_BLOCK));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Heartbeat(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            upgradeDescription();
        }
    }
}
