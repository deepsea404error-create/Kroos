package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

import java.util.HashSet;
import java.util.Set;

/**
 * 心音 Power。
 * 含义: 每回合前 X 张卡牌获得的格挡翻倍。
 *
 * 实现: modifyBlock 钩子翻倍格挡值（同时影响显示和实际效果）。
 *       doubledCards 集合保证同一张卡多次 modifyBlock 调用仅占用 1 次计数。
 *       atStartOfTurn 重置计数与集合。
 *       不检查 cardInUse，因为 applyPowers() 在卡牌打出前就已计算好 block 值。
 */
public class HeartbeatPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":Heartbeat";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/heartbeat_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/heartbeat_small.png";

    private int triggersLeft;
    private final Set<AbstractCard> doubledCards = new HashSet<>();

    public HeartbeatPower(AbstractCreature owner, int amount) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner; this.amount = amount;
        this.type = PowerType.BUFF;
        this.triggersLeft = amount;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 84, 84);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 36, 36);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESC[0] + this.amount + DESC[1];
    }

    @Override
    public void atStartOfTurn() {
        triggersLeft = this.amount;
        doubledCards.clear();
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        if (blockAmount <= 0 || card == null || this.amount <= 0) return blockAmount;
        if (doubledCards.contains(card)) return blockAmount * 2F;
        if (triggersLeft <= 0) return blockAmount;
        doubledCards.add(card);
        triggersLeft--;
        this.flash();
        return blockAmount * 2F;
    }
}
