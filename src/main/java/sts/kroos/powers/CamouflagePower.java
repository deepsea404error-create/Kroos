package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 迷彩 Power。
 *
 * 含义: 所受到的攻击伤害降低 50%。每受到 1 次攻击 -1 层迷彩。
 *
 * 实现:
 *   - atDamageReceive (NORMAL): damage * 0.5 (向下取整由 StS 通道处理)
 *   - onAttacked: 自减 1 层
 *
 * 当 amount 减到 0 后, 标准 AbstractPower 流程会触发自移除。
 */
public class CamouflagePower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":Camouflage";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/camouflage_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/camouflage_small.png";

    public CamouflagePower(AbstractCreature owner, int amount) {
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

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (this.amount > 0 && type == DamageInfo.DamageType.NORMAL) {
            return damage * 0.5F;
        }
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0 && this.amount > 0) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(owner, owner, POWER_ID, 1));
        }
        return damageAmount;
    }
}
