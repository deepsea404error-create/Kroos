package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.DozePower;
import sts.kroos.powers.FlawPower;
import sts.kroos.util.BattleCounters;

/**
 * 奋起(梦击) - 1费, 解除浅眠状态。造成 10 (强化 14) 点伤害, 施加 1 (强化 2) 层破绽。
 *   - 每解除 1 次浅眠状态, 额外造成 1 次伤害 (BattleCounters)
 *   - 寒芒: 消耗 1 层寒芒, 获得 1 (强化 2) 点力量
 *
 * 计数: 本卡 use 时若实际解除了一次浅眠, +1。
 */
public class Rouse extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Rouse";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/rouse.png";
    private static final String COUNTER_KEY = "Rouse:dozeExits";

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int FLAW = 1;
    private static final int UPGRADE_FLAW = 1;
    private static final int STR = 1;
    private static final int STR_UPG = 2;

    private final String baseDesc;

    public Rouse() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = FLAW;
        this.magicNumber = FLAW;
        this.isDreamStrike = true;
        this.baseDesc = this.rawDescription;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int hits = 1 + BattleCounters.get(COUNTER_KEY);
        this.rawDescription = baseDesc + " NL 当前造成 " + hits + " 次伤害。";
        initializeDescription();
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        boolean exited = false;
        if (p.hasPower(DozePower.POWER_ID)) {
            addToBot(new RemoveSpecificPowerAction(p, p, DozePower.POWER_ID));
            BattleCounters.inc(COUNTER_KEY);
            exited = true;
        }
        int hits = 1 + BattleCounters.get(COUNTER_KEY) - (exited ? 1 : 0);
        // 当本次刚解除时, 本次也计入 → hits = 1 + counter
        if (exited) hits = 1 + BattleCounters.get(COUNTER_KEY);
        for (int i = 0; i < hits; i++) {
            addToBot(new DamageAction(m,
                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AttackEffect.SLASH_HEAVY));
        }
        addToBot(new ApplyPowerAction(m, p,
                new FlawPower(m, this.magicNumber), this.magicNumber));

        if (canConsumeFrost(1)) {
            consumeFrost(1);
            int str = this.upgraded ? STR_UPG : STR;
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, str), str));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Rouse(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_FLAW);
            upgradeDescription();
        }
    }
}
