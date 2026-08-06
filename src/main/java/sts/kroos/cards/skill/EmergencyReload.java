package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FrostPower;

/**
 * 紧急装填 - 1费, 抽 2(3) 张牌, 每抽到 1 张攻击牌获得 1 层寒芒。
 * 消耗 1 层寒芒: 额外抽 1 张。
 *
 * 实现参考: 猎人·逃脱计划 (Escape Plan) 的 DrawCardAction 回调模式。
 */
public class EmergencyReload extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":EmergencyReload";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/emergency_reload.png";

    private static final int COST = 1;
    private static final int DRAW = 1;
    private static final int UPGRADE_DRAW = 1;

    public EmergencyReload() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = DRAW;
        this.magicNumber = DRAW;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int draw = this.magicNumber;
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            draw += 1;
        }
        // 使用 DrawCardAction(amount, callback) 构造函数
        addToBot(new DrawCardAction(draw, new AbstractGameAction() {
            @Override
            public void update() {
                if (!DrawCardAction.drawnCards.isEmpty() && !isDone) {
                    for (AbstractCard c : DrawCardAction.drawnCards) {
                        if (c.type == AbstractCard.CardType.ATTACK) {
                            addToTop(new ApplyPowerAction(p, p,
                                    new FrostPower(p, 1), 1));
                        }
                    }
                    isDone = true;
                }
            }
        }));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new EmergencyReload(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DRAW);
            upgradeDescription();
        }
    }
}
