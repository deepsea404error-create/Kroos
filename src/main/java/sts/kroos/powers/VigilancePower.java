package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 警惕 Power。
 * 含义: 每次退出浅眠状态获得 X 点活力。
 *
 * 实现: 监听 IDozeExitListener 接口, DozePower.onRemove 时收到 onDozeExited 回调。
 */
public class VigilancePower extends AbstractPower implements IDozeExitListener {
    public static final String POWER_ID = KroosMod.MOD_ID + ":Vigilance";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/vigilance_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/vigilance_small.png";

    public VigilancePower(AbstractCreature owner, int amount) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner; this.amount = amount;
        this.type = PowerType.BUFF;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 84, 84);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void updateDescription() { this.description = DESC[0] + this.amount + DESC[1]; }

    @Override
    public void onDozeExited() {
        if (this.amount <= 0) return;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                owner, owner, new VigorPower(owner, this.amount), this.amount));
    }
}
