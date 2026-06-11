package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.DozePower;

/**
 * 好梦 - 1费, 获得 5 点格挡。
 *   - 若处于浅眠, 额外获得 3 点格挡
 *   - 否则进入浅眠状态
 *   - 寒芒: 消耗 1 层寒芒, 获得 1 点敏捷
 *   - 强化: 7 / 5 格挡
 */
public class GoodDream extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":GoodDream";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/good_dream.png";

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_BLOCK = 2;
    private static final int BONUS_BLOCK_IN_DOZE = 3;
    private static final int UPGRADE_BONUS_BLOCK = 2;
    private static final int DEX = 1;

    public GoodDream() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = BONUS_BLOCK_IN_DOZE;
        this.magicNumber = BONUS_BLOCK_IN_DOZE;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        boolean inDoze = p.hasPower(DozePower.POWER_ID);
        int total = this.block + (inDoze ? this.magicNumber : 0);
        addToBot(new GainBlockAction(p, p, total));
        if (!inDoze) {
            addToBot(new ApplyPowerAction(p, p, new DozePower(p)));
        }
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, DEX), DEX));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new GoodDream(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_BONUS_BLOCK);
            upgradeDescription();
        }
    }
}
