package sts.kroos.relics;

/**
 * 寒芒消耗监听器。
 *
 * 实现此接口的遗物在 AbstractKroosCard.consumeFrost 结算时收到通知,
 * 用于"成长的证明" / "练习靶" / "通明" 类需要响应寒芒消耗的遗物。
 *
 * 用于解耦 consumeFrost 与具体遗物 — consumeFrost 只遍历 player.relics 调用监听者,
 * 监听者各自管理副作用 (如施加 VigorPower、累加格挡等)。
 *
 * @param logicalAmount 玩家请求扣的寒芒层数 (未受心之痕减免影响)
 */
public interface IFrostConsumeListener {
    void onFrostConsumed(int logicalAmount);
}
