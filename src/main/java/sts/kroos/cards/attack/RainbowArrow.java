package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FlawPower;

/**
 * 破虹箭(箭矢) - 1费, 造成 6 (强化 9) 点伤害。
 *   - 对拥有[格挡]的敌人伤害翻倍, 并施加 1 层破绽
 *   - 寒芒: 若造成伤害, 消耗 1 层寒芒, 给予 2 (强化 3) 层易伤
 */
public class RainbowArrow extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":RainbowArrow";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/rainbow_arrow.png";

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int FROST_VULN = 2;
    private static final int FROST_VULN_UPG = 3;

    public RainbowArrow() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.isArrow = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int dmg = this.damage;
        boolean hasBlock = m != null && m.currentBlock > 0;
        if (hasBlock) {
            dmg *= 2;
            addToBot(new ApplyPowerAction(m, p, new FlawPower(m, 1), 1));
        }
        addToBot(new DamageAction(m,
                new DamageInfo(p, dmg, this.damageTypeForTurn),
                AttackEffect.SLASH_DIAGONAL));
        scatterIfArrow(p, m, dmg);

        if (dmg > 0 && canConsumeFrost(1)) {
            consumeFrost(1);
            int vuln = this.upgraded ? FROST_VULN_UPG : FROST_VULN;
            addToBot(new ApplyPowerAction(m, p,
                    new VulnerablePower(m, vuln, false), vuln));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new RainbowArrow(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
