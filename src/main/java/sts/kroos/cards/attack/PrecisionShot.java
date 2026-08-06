package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FlawPower;
import sts.kroos.util.BattleCounters;

/**
 * 精准射击 - 1费, 造成 8 (强化 10) 点伤害, 施加 1 层破绽。
 *   - 本场每打出过 1 次精准射击, 伤害 +5 (强化 +8)
 *   - 寒芒: 消耗 1 层寒芒, 施加 1 层破绽
 *
 * 计数走 BattleCounters。
 */
public class PrecisionShot extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":PrecisionShot";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/precision_shot.png";
    private static final String COUNTER_KEY = "PrecisionShot:plays";

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_BASE_DAMAGE = 2;
    private static final int PER_USE_BONUS = 5;
    private static final int PER_USE_BONUS_UPG = 8;
    private static final int FLAW = 1;
    private static final int FROST_FLAW = 1;

    public PrecisionShot() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = FLAW;
        this.magicNumber = FLAW;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(m, p,
                new FlawPower(m, this.magicNumber), this.magicNumber));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(m, p,
                    new FlawPower(m, FROST_FLAW), FROST_FLAW));
        }
        BattleCounters.inc(COUNTER_KEY);
    }

    /** 显示伤害随历史使用次数累加, 给玩家直观反馈 */
    @Override
    public void applyPowers() {
        int plays = BattleCounters.get(COUNTER_KEY);
        int per = this.upgraded ? PER_USE_BONUS_UPG : PER_USE_BONUS;
        int base = this.upgraded ? (BASE_DAMAGE + UPGRADE_BASE_DAMAGE) : BASE_DAMAGE;
        this.baseDamage = base + plays * per;
        super.applyPowers();
    }

    @Override
    public AbstractKroosCard makeCopy() { return new PrecisionShot(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.baseDamage += UPGRADE_BASE_DAMAGE;
            this.upgradedDamage = true;
            upgradeDescription();
        }
    }
}
