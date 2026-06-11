package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.CamouflagePower;

/**
 * 隐藏 - 1费, 消耗, 保留。
 *   - 获得 7 (强化 11) 点格挡, 1 (强化 2) 层迷彩
 *   - 寒芒: 消耗 1 层寒芒, 获得 1 点敏捷
 */
public class Hide extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Hide";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/hide.png";

    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int UPGRADE_BLOCK = 4;
    private static final int CAMO = 1;
    private static final int UPGRADE_CAMO = 1;
    private static final int DEX = 1;

    public Hide() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = CAMO;
        this.magicNumber = CAMO;
        this.exhaust = true;
        this.selfRetain = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(p, p,
                new CamouflagePower(p, this.magicNumber), this.magicNumber));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(p, p,
                    new DexterityPower(p, DEX), DEX));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Hide(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_CAMO);
            upgradeDescription();
        }
    }
}
