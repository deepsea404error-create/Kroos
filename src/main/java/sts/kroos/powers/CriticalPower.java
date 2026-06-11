package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 暴击 Power。
 *
 * 含义: 攻击牌造成 1.5 倍伤害。每打出 1 张攻击牌消耗 1 层。
 *
 * 实现:
 *   - atDamageGive: 当 type == NORMAL 且有层数时, 伤害 * 1.5
 *     (利用 StS 标准源端伤害修饰流程, 卡牌显示伤害与实际伤害都会被乘)
 *   - onUseCard: 打出攻击牌时层数 -1 (通过 ReducePowerAction 入队)
 *
 * 注: 倍率 (1.5) 不与层数挂钩, 多层只是"次数"。"爆发"power 额外加 25%*X 暴击伤害由其自身处理。
 */
public class CriticalPower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":Critical";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    public static final float BASE_MULTIPLIER = 1.5F;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/critical_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/critical_small.png";

    public CriticalPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        loadIcons();
        updateDescription();
    }

    private void loadIcons() {
        Texture large = TextureLoader.getTexture(ICON_LARGE);
        Texture small = TextureLoader.getTexture(ICON_SMALL);
        if (large != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(large, 0, 0, 128, 128);
        if (small != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(small, 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    /** 源端伤害修饰: 攻击牌伤害 * 1.5 */
    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (this.amount > 0 && type == DamageInfo.DamageType.NORMAL) {
            return damage * BASE_MULTIPLIER;
        }
        return damage;
    }

    /** 每张攻击牌打出后消耗 1 层 (与触发次数无关, 一张卡 = 一次暴击) */
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount > 0 && card.type == AbstractCard.CardType.ATTACK) {
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(owner, owner, POWER_ID, 1));
            // 暴击触发 hook: 其他 power(共鸣/无明) 可以监听本 power 减少 1 层来联动
        }
    }
}
