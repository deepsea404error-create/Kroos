package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.CamouflagePower;

/**
 * 紧急回避 - 1费, 保留。获得 8 点格挡。
 *   - 寒芒: 消耗 1 层寒芒, 获得 1 层迷彩
 *   - 强化: 11 格挡
 */
public class EmergencyEvasion extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":EmergencyEvasion";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/emergency_evasion.png";

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 3;

    public EmergencyEvasion() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.selfRetain = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(p, p, new CamouflagePower(p, 1), 1));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new EmergencyEvasion(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            upgradeDescription();
        }
    }
}
