package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FocusPower;

/**
 * 流云箭(箭矢) - 1费, 造成 8 (强化 11) 点伤害, 抽 1 张牌。
 *   - 若抽出的是[箭矢]牌, 其费用-1
 *   - 寒芒: 消耗 1 层寒芒, 获得 1 层专注
 *
 * 抽到-检查模式: 用 handSize 快照定位新抽到的牌(本牌已 in limbo 不计)。
 */
public class FlowingCloudArrow extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":FlowingCloudArrow";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/flowing_cloud_arrow.png";

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int FROST_FOCUS = 1;

    public FlowingCloudArrow() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.isArrow = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.SLASH_DIAGONAL));
        scatterIfArrow(p, m, this.damage);

        final int handBefore = p.hand.size();
        addToBot(new DrawCardAction(p, 1));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                int now = AbstractDungeon.player.hand.size();
                for (int i = handBefore; i < now; i++) {
                    AbstractCard c = AbstractDungeon.player.hand.group.get(i);
                    if (c instanceof AbstractKroosCard
                            && ((AbstractKroosCard) c).isArrow) {
                        c.cost = Math.max(0, c.cost - 1);
                        c.costForTurn = Math.max(0, c.costForTurn - 1);
                        c.isCostModified = true;
                    }
                }
            }
        });

        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(p, p, new FocusPower(p, FROST_FOCUS), FROST_FOCUS));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new FlowingCloudArrow(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
