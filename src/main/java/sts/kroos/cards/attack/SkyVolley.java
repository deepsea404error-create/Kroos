package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 破空连射 - 2费, 造成 4 (强化 5) 点伤害。每有 1 张[箭矢]牌, 额外造成 1 次伤害。
 *
 * 实现: 统计当前战斗所有 CardGroup (draw/discard/hand/exhaust) 内 isArrow=true 的卡牌数。
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
        int hits = 1 + countBattleArrows();
        for (int i = 0; i < hits; i++) {
            addToBot(new DamageAction(m,
                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AttackEffect.BLUNT_LIGHT));
        }
    }

    /** 战斗内所有 4 个 CardGroup 中的箭矢牌数(含本牌自身打出前所在位置已被移走情况下也合理) */
    private static int countBattleArrows() {
        int n = 0;
        AbstractPlayer p = AbstractDungeon.player;
        n += countIn(p.drawPile);
        n += countIn(p.discardPile);
        n += countIn(p.hand);
        n += countIn(p.exhaustPile);
        return n;
    }

    private static int countIn(com.megacrit.cardcrawl.cards.CardGroup g) {
        if (g == null) return 0;
        int n = 0;
        for (AbstractCard c : g.group) {
            if (c instanceof AbstractKroosCard && ((AbstractKroosCard) c).isArrow) n++;
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
