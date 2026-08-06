package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 破空连射 - 2费, 造成 4 (强化 5) 点伤害。手牌中每有 1 张[箭矢]牌, 额外造成 1 次伤害。
 */
public class SkyVolley extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":SkyVolley";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/sky_volley.png";

    private static final int COST = 2;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 1;

    public SkyVolley() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int hits = 1 + countHandArrows(p);
        for (int i = 0; i < hits; i++) {
            addToBot(new DamageAction(m,
                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AttackEffect.BLUNT_LIGHT));
        }
    }

    private static int countHandArrows(AbstractPlayer p) {
        int n = 0;
        if (p != null && p.hand != null) {
            for (AbstractCard c : p.hand.group) {
                if (c instanceof AbstractKroosCard && ((AbstractKroosCard) c).isArrow) {
                    n++;
                }
            }
        }
        return n;
    }

    @Override
    public AbstractKroosCard makeCopy() { return new SkyVolley(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
