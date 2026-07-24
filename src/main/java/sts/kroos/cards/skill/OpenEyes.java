package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.DozePower;

/**
 * 睁眼 - 0费, 退出浅眠状态。
 *   - 寒芒: 消耗 1 层寒芒, 获得 4 点活力 (强化后为 6, 且本牌保留)
 *
 * 退出浅眠 = RemoveSpecificPowerAction(DozePower), 由 DozePower.onRemove 链式触发蓄势放电。
 * 活力 = 原版 EnergizedPower (下回合 +X 能量)
 */
public class OpenEyes extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":OpenEyes";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/open_eyes.png";

    private static final int COST = 0;
    private static final int VITALITY = 4;
    private static final int UPGRADE_VITALITY = 2;

    public OpenEyes() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = VITALITY;
        this.magicNumber = VITALITY;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(DozePower.POWER_ID)) {
            addToBot(new RemoveSpecificPowerAction(p, p, DozePower.POWER_ID));
        }
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(p, p,
                    new VigorPower(p, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new OpenEyes(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_VITALITY);
            this.selfRetain = true;
            upgradeDescription();
        }
    }
}
