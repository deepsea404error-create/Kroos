package sts.kroos.cards.colorless;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * A1 小队 - 米格鲁。蓝卡, 1费, 消耗, 虚无, 技能。
 *   - 获得 8 (强化 11) 点格挡, 2 (强化 3) 点敏捷
 *   - 寒芒: 消耗 1 层寒芒, 额外获得 3 (强化 5) 点格挡
 */
public class A1_Migelu extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":A1_Migelu";
    private static final String IMG = KroosMod.RES_ROOT + "cards/colorless/a1_migelu.png";

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 3;
    private static final int DEX = 2;
    private static final int UPGRADE_DEX = 1;
    private static final int FROST_BLOCK = 3;
    private static final int FROST_BLOCK_UPG = 5;

    public A1_Migelu() {
        super(ID, IMG, COST, CardType.SKILL,
                AbstractCard.CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = DEX;
        this.magicNumber = DEX;
        this.exhaust = true;
        this.isA1Squad = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(p, p,
                new DexterityPower(p, this.magicNumber), this.magicNumber));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new GainBlockAction(p, p,
                    this.upgraded ? FROST_BLOCK_UPG : FROST_BLOCK));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new A1_Migelu(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_DEX);
            upgradeDescription();
        }
    }
}
