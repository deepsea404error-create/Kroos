package sts.kroos.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.kroos.cards.colorless.A1_Fen;
import sts.kroos.cards.colorless.A1_Furong;
import sts.kroos.cards.colorless.A1_Miguelu;
import sts.kroos.cards.colorless.A1_Yanrong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * A1 小队卡的工厂与采样器。
 *
 * 提供:
 *   - randomA1(): 返回 4 种 A1 小队卡中的一种新实例
 *   - randomA1Cards(n): 返回 n 种不同的 A1 小队卡 (用于 3选1 等选择场景)
 *
 * 随机数走 AbstractDungeon.cardRandomRng, 保证 seed 一致回放。
 */
public final class A1SquadFactory {

    private static final List<Supplier<AbstractCard>> POOL = new ArrayList<>();
    static {
        POOL.add(A1_Fen::new);
        POOL.add(A1_Miguelu::new);
        POOL.add(A1_Furong::new);
        POOL.add(A1_Yanrong::new);
    }

    private A1SquadFactory() {}

    public static AbstractCard randomA1() {
        int idx = AbstractDungeon.cardRandomRng.random(POOL.size() - 1);
        return POOL.get(idx).get();
    }

    /** 返回 n 种不重复的 A1 卡新实例 (n 上限 = POOL.size()) */
    public static List<AbstractCard> randomA1Cards(int n) {
        List<Supplier<AbstractCard>> shuffled = new ArrayList<>(POOL);
        Collections.shuffle(shuffled,
                new java.util.Random(AbstractDungeon.cardRandomRng.randomLong()));
        int take = Math.min(n, shuffled.size());
        List<AbstractCard> out = new ArrayList<>(take);
        for (int i = 0; i < take; i++) out.add(shuffled.get(i).get());
        return out;
    }
}
