package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 梦影 Power。
 *
 * 含义: 首次进入浅眠时, 获得 X 点敏捷。
 * "首次"语义: 本 power 实例存活期间, 仅首次检测到 DozePower 被施加时触发一次。
 *
 * 实现: 监听 onApplyPower 广播, 当对方 power 为 DozePower 且未触发过时, 施加敏捷并置位 flag。
 */
public class DreamShadowPower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":DreamShadow";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/dream_shadow_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/dream_shadow_small.png";

    /** 本实例是否已触发过首次浅眠效果 */
    private boolean firstDozeTriggered = false;

    public DreamShadowPower(AbstractCreature owner, int amount) {
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
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (firstDozeTriggered) return;
        if (target != this.owner) return;
        if (!(power instanceof DozePower)) return;
        firstDozeTriggered = true;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                owner, owner, new DexterityPower(owner, this.amount), this.amount));
    }
}
