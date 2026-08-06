package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FocusPower;

import java.util.ArrayList;

/**
 * 装备改良 - 0费, 随机升级 1 张手牌, 获得 1 层专注。
 *   - 寒芒: 消耗 1 层寒芒, 额外升级 1 张手牌
 *   - 强化: 随机升级 2 张, 获得 2 层专注
 */
public class EquipmentUpgrade extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":EquipmentUpgrade";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/equipment_upgrade.png";

    private static final int COST = 0;
    private static final int BASE_UPGRADES = 1;
    private static final int UPGRADE_UPGRADES = 1;
    private static final int FOCUS = 1;
    private static final int UPGRADE_FOCUS = 1;

    public EquipmentUpgrade() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_UPGRADES;
        this.magicNumber = BASE_UPGRADES;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int n = this.magicNumber;
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            n += 1;
        }
        final int upgrades = n;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                ArrayList<AbstractCard> pool = new ArrayList<>();
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c.canUpgrade()) pool.add(c);
                }
                for (int i = 0; i < upgrades && !pool.isEmpty(); i++) {
                    int idx = AbstractDungeon.cardRandomRng.random(pool.size() - 1);
                    AbstractCard c = pool.remove(idx);
                    c.upgrade();
                    c.applyPowers();
                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                }
                AbstractDungeon.player.hand.glowCheck();
            }
        });
        int focus = this.upgraded ? (FOCUS + UPGRADE_FOCUS) : FOCUS;
        addToBot(new ApplyPowerAction(p, p, new FocusPower(p, focus), focus));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new EquipmentUpgrade(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_UPGRADES);
            upgradeDescription();
        }
    }
}
