package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 成长的证明 — BOSS。
 * 每消耗 1 层寒芒, 下一次攻击伤害 +2。
 *
 * 实现: 监听 IFrostConsumeListener, 每次寒芒消耗给玩家施加 VigorPower(2 * logicalAmount)。
 *      VigorPower 原生语义即"下次攻击+X, 攻击后清空", 完美匹配。
 */
public class ProofOfGrowth extends CustomRelic implements IFrostConsumeListener {
    public static final String ID = KroosMod.MOD_ID + ":ProofOfGrowth";
    private static final String IMG = KroosMod.RES_ROOT + "relics/proof_of_growth.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/proof_of_growth_outline.png";

    public static final int BONUS_PER_LAYER = 2;

    public ProofOfGrowth() {
        super(ID, TextureLoader.getTexture(IMG), RelicTier.BOSS, LandingSound.MAGICAL);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void onFrostConsumed(int logicalAmount) {
        if (logicalAmount <= 0 || AbstractDungeon.player == null) return;
        int bonus = logicalAmount * BONUS_PER_LAYER;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new VigorPower(AbstractDungeon.player, bonus), bonus));
    }

    @Override
    public AbstractRelic makeCopy() { return new ProofOfGrowth(); }
}
