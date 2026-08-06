package sts.kroos.powers;

/**
 * 浅眠退出监听器。
 *
 * 实现此接口的 power 在挂载到 owner 上后, 当 owner 身上的 [DozePower] 被移除时
 * 会收到 onDozeExited() 回调。
 *
 * 用于解耦 DozePower 与 Vigilance/警惕 等"退出浅眠时…"的 power:
 * DozePower 只负责广播事件, 监听者各自管理自己的反应逻辑。
 */
public interface IDozeExitListener {
    void onDozeExited();
}
