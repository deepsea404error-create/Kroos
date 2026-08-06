package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FrostPower;

/**
 * 记忆创伤 - 1费, 消耗。
 *   - 消耗 3 点生命
 *   - 获得 3 (强化 4) 层寒芒和 2 (强化 3) 层力量
 */
public class MemoryWound extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":MemoryWound";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/memory_wound.png";

    private static final int COST = 1;
    private static final int HP_COST = 3;
    private static final int FROST = 3;
    private static final int UPGRADE_FROST = 1;
    private static final int STRENGTH = 2;
    private static final int UPGRADE_STRENGTH = 1;

    public MemoryWound() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = FROST;
        this.magicNumber = FROST;
        this.exhaust = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, HP_COST, AbstractGameAction.AttackEffect.NONE));
        addToBot(new ApplyPowerAction(p, p,
                new FrostPower(p, this.magicNumber), this.magicNumber));
        int str = this.upgraded ? (STRENGTH + UPGRADE_STRENGTH) : STRENGTH;
        addToBot(new ApplyPowerAction(p, p,
                new StrengthPower(p, str), str));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new MemoryWound(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FROST);
            upgradeDescription();
        }
    }
}
