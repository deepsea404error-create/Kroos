package sts.kroos.cards.colorless;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * A1 小队 - 芙蓉。蓝卡, 0费, 消耗, 虚无, 技能。
 *   - 失去 2 点生命, 获得 2 (强化 3) 点能量, 4 (强化 6) 点活力
 *   - 寒芒: 消耗 1 层寒芒, 获得 1 (强化 2) 层临时力量
 */
public class A1_Furong extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":A1_Furong";
    private static final String IMG = KroosMod.RES_ROOT + "cards/colorless/a1_furong.png";

    private static final int COST = 0;
    private static final int HP_LOSS = 2;
    private static final int ENERGY = 2;
    private static final int UPGRADE_ENERGY = 1;
    private static final int VITALITY = 4;
    private static final int UPGRADE_VITALITY = 2;
    private static final int TEMP_STR = 1;
    private static final int TEMP_STR_UPG = 2;

    public A1_Furong() {
        super(ID, IMG, COST, CardType.SKILL,
                AbstractCard.CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        this.baseMagicNumber = ENERGY;
        this.magicNumber = ENERGY;
        this.exhaust = true;
        this.isA1Squad = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, HP_LOSS, AbstractGameAction.AttackEffect.NONE));
        addToBot(new GainEnergyAction(this.magicNumber));
        int vit = this.upgraded ? UPGRADE_VITALITY + VITALITY - UPGRADE_VITALITY : VITALITY;
        // 简化: 强化后 vitality 直接为 6
        vit = this.upgraded ? 6 : VITALITY;
        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, vit), vit));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            int str = this.upgraded ? TEMP_STR_UPG : TEMP_STR;
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, str), str));
            addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, str), str));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new A1_Furong(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_ENERGY);
            upgradeDescription();
        }
    }
}
