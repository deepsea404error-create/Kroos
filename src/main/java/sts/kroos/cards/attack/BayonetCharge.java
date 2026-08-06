package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.DoubleIncomingPower;
import sts.kroos.powers.FlawPower;

/**
 * 下挂刺刀 - 1费, 造成 8 (强化 11) 点伤害, 施加 2 (强化 3) 层破绽。
 *   - 若敌人意图为攻击, 本回合该敌人所受伤害翻倍 (DoubleIncomingPower)
 *   - 寒芒: 若敌人有防御, 消耗 2 层寒芒, 去除敌人的所有格挡值
 */
public class BayonetCharge extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":BayonetCharge";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/bayonet_charge.png";

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int FLAW = 2;
    private static final int UPGRADE_FLAW = 1;

    public BayonetCharge() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = FLAW;
        this.magicNumber = FLAW;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        // 攻击意图 → 施加翻倍 power (本牌伤害本身也会被翻倍, 因 ApplyPowerAction 先于 DamageAction 入队)
        if (m != null && isAttackIntent(m.intent) && !m.hasPower(DoubleIncomingPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(m, p, new DoubleIncomingPower(m)));
        }
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.SLASH_HEAVY));
        addToBot(new ApplyPowerAction(m, p,
                new FlawPower(m, this.magicNumber), this.magicNumber));

        // 寒芒: 有防御则消 2, 去除所有格挡
        if (m != null && m.currentBlock > 0 && canConsumeFrost(2)) {
            consumeFrost(2);
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    if (m != null && !m.isDeadOrEscaped()) {
                        m.loseBlock();
                    }
                }
            });
        }
    }

    private static boolean isAttackIntent(Intent intent) {
        if (intent == null) return false;
        switch (intent) {
            case ATTACK:
            case ATTACK_BUFF:
            case ATTACK_DEBUFF:
            case ATTACK_DEFEND:
                return true;
            default:
                return false;
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new BayonetCharge(); }

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
