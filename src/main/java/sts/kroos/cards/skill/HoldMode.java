package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 坚守模式 - 3费, 消耗。
 *   - 获得 8 (强化 11) 层多重护甲 (= PlatedArmorPower)
 *   - 寒芒: 消耗 1 层寒芒, 获得 6 (强化 8) 点防御
 *
 * 多重护甲沿用原版 PlatedArmorPower (受击后流失 1 层)。
 */
public class HoldMode extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":HoldMode";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/hold_mode.png";

    private static final int COST = 3;
    private static final int ARMOR = 8;
    private static final int UPGRADE_ARMOR = 3;
    private static final int FROST_BLOCK = 6;
    private static final int FROST_BLOCK_UPG = 8;

    public HoldMode() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = ARMOR;
        this.magicNumber = ARMOR;
        this.exhaust = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new PlatedArmorPower(p, this.magicNumber), this.magicNumber));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new GainBlockAction(p, p,
                    this.upgraded ? FROST_BLOCK_UPG : FROST_BLOCK));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new HoldMode(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_ARMOR);
            upgradeDescription();
        }
    }
}
