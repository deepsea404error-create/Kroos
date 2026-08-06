package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.DreamShadowPower;

/**
 * 梦影 - 2费(强化 1费), 每次进入浅眠时获得 1 点敏捷。
 *   - 寒芒: 消耗 1 层寒芒, 立即获得 1 点敏捷
 */
public class DreamShadow extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":DreamShadow";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/dream_shadow.png";

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int DEX = 1;

    public DreamShadow() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = DEX;
        this.magicNumber = DEX;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new DreamShadowPower(p, this.magicNumber), this.magicNumber));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, DEX), DEX));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new DreamShadow(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            upgradeDescription();
        }
    }
}
