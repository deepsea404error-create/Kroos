package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 幻影伪装 Power。
 *
 * 含义: 回合结束时若处于浅眠状态, 获得 X 层迷彩。
 * 注: 不耦合浅眠/迷彩内部逻辑, 仅施加 CamouflagePower。
 */
public class PhantomCamoPower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":PhantomCamo";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/phantom_camo_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/phantom_camo_small.png";

    public PhantomCamoPower(AbstractCreature owner, int amount) {
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
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer || this.amount <= 0 || owner == null) return;
        if (!owner.hasPower(DozePower.POWER_ID)) return;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                owner, owner, new CamouflagePower(owner, this.amount), this.amount));
    }
}
