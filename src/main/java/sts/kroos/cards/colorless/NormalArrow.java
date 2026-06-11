package sts.kroos.cards.colorless;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 普通箭矢 (衍生无色 — 箭矢)。
 * 0费, 消耗, 箭矢。造成 4 (强化 6) 点伤害。
 *
 * 由 速射 / 散射 等卡牌/power 添加到玩家手牌中。
 */
public class NormalArrow extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":NormalArrow";
    private static final String IMG = KroosMod.RES_ROOT + "cards/colorless/normal_arrow.png";

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;

    public NormalArrow() {
        super(ID, IMG, COST, CardType.ATTACK,
                AbstractCard.CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.exhaust = true;
        this.isArrow = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.SLASH_DIAGONAL));
        scatterIfArrow(p, m, this.damage);
    }

    @Override
    public AbstractKroosCard makeCopy() { return new NormalArrow(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
