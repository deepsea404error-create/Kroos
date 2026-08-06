package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 锋芒尽露 - 1费。
 *   - 寒芒: 消耗 3 层寒芒, 造成 21 (强化 27) 点伤害。
 *
 * 寒芒未达 3 层时本牌无效果 (仅消耗 1 费)。
 */
public class GlitteringEdge extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":GlitteringEdge";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/glittering_edge.png";

    private static final int COST = 1;
    private static final int DAMAGE = 21;
    private static final int UPGRADE_DAMAGE = 6;

    public GlitteringEdge() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        if (canConsumeFrost(3)) {
            consumeFrost(3);
            addToBot(new DamageAction(m,
                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AttackEffect.SLASH_HEAVY));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new GlitteringEdge(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
