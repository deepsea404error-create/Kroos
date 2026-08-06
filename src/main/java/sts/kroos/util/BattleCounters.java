package sts.kroos.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 战斗范围的命名计数器。在战斗开始时统一清零 (由 KroosMod.receiveOnBattleStart 调用 resetAll)。
 *
 * 用途:
 *   - 卡牌"本场战斗每打出过 N 次本牌, 伤害+M" 类机制 (强化箭 / 精准射击)
 *   - 任何需要跨同张卡多个副本共享状态的简单整数计数
 *
 * 设计要点:
 *   - 静态全局, 但生命周期由 OnStartBattle 拉平 — 不跨战斗污染
 *   - 卡牌建议使用各自类名作为 key 前缀, 防止冲突
 */
public final class BattleCounters {
    private static final Map<String, Integer> COUNTERS = new HashMap<>();

    private BattleCounters() {}

    public static int get(String key) { return COUNTERS.getOrDefault(key, 0); }

    public static int inc(String key) {
        int v = get(key) + 1;
        COUNTERS.put(key, v);
        return v;
    }

    public static void set(String key, int v) { COUNTERS.put(key, v); }

    public static void resetAll() { COUNTERS.clear(); }
}
