package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FlawPower;

/**
 * 封喉 - 2费, 消耗。造成 4 (强化 5) 点伤害 4 次。
 *   - 每次伤害若未被完全阻挡, 施加 1 层破绽
 *   - 寒芒: 消耗 3 层寒芒, 额外造成 2 次伤害
 *
 * 未阻挡判定: 出手前查目标 currentBlock, 若 damage > currentBlock 则视为破防。
 * 此判定在排队时计算, 不考虑战中后续 block 变化(简化处理)。
 */
public class ThroatSeal extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":ThroatSeal";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/throat_seal.png";

    private static final int COST = 2;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 1;
    private static final int HITS = 4;
    private static final int FROST_EXTRA_HITS = 2;

    public ThroatSeal() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = HITS;
        this.magicNumber = HITS;
        this.exhaust = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int hits = this.magicNumber;
        if (canConsumeFrost(3)) {
            consumeFrost(3);
            hits += FROST_EXTRA_HITS;
        }
        final int dmg = this.damage;
        for (int i = 0; i < hits; i++) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    if (m == null || m.isDeadOrEscaped()) return;
                    boolean breaksThrough = m.currentBlock < dmg;
                    AbstractDungeon.actionManager.addToTop(new DamageAction(m,
                            new DamageInfo(p, dmg, ThroatSeal.this.damageTypeForTurn),
                            AttackEffect.SLASH_DIAGONAL));
                    if (breaksThrough) {
                        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
                                m, p, new FlawPower(m, 1), 1));
                    }
                }
            });
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new ThroatSeal(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
