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
 * 奋起(梦击) - 1费, 造成 10 (强化 14) 点伤害, 施加 1 (强化 2) 层破绽, 随后退出浅眠。
 *   - 先结算伤害与破绽, 再解除浅眠; 每次因该牌退出浅眠, 之后的伤害次数+1 (BattleCounters)
 *   - 寒芒: 消耗 1 层寒芒, 获得 1 (强化 2) 点力量
 *
 * 计数: 本卡 use 时若实际解除了一次浅眠, 在伤害结算后 +1 (下一次生效)。
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

    /** 卡面中动态攻击次数的插入锚点 (与 Cards-strings 中的句子保持一致) */
    private static final String COUNTER_ANCHOR = "造成的伤害次数+1。";

    private String baseDesc;

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
        this.rawDescription = baseDesc.replace(COUNTER_ANCHOR,
                COUNTER_ANCHOR + "(当前攻击" + hits + "次)");
        initializeDescription();
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        // 先按已有计数结算伤害与破绽 (不含本次退出), 再解除浅眠并计数, 供下次使用
        int hits = 1 + BattleCounters.get(COUNTER_KEY);
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
        if (p.hasPower(DozePower.POWER_ID)) {
            addToBot(new RemoveSpecificPowerAction(p, p, DozePower.POWER_ID));
            BattleCounters.inc(COUNTER_KEY);
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
            // applyPowers() 以 baseDesc 为基准拼接动态后缀, 升级后需同步为强化版描述,
            // 否则会被回退成未升级文本
            this.baseDesc = this.rawDescription;
        }
    }
}
