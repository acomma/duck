/**
 * 协议设计
 * <pre class="code">
 * 0       1       2         3          4        5       6       7       8        9      10      11       12       13       14      15       16
 * +-------+-------+---------+----------+--------+-------+-------+-------+--------+-------+-------+-------+--------+--------+--------+--------+
 * |    Magic      | Version | Req/Resp |                         Request ID                              |               Length              |
 * +------------------------------------------------------------------------------------------------------------------------------------------+
 * |                                                                                                                                          |
 * |                                                                 Payload                                                                  |
 * |                                                                                                                                          |
 * +------------------------------------------------------------------------------------------------------------------------------------------+
 * </pre>
 *
 * 使用方式
 * <ol>
 * <li>定义服务
 * <pre>{@code
 * public interface GreetingService {
 *     String greeting(String name);
 * }
 * }</pre></li>
 * <li>服务提供者
 * <pre>{@code
 * @Service
 * @RpcService(interfaceClass = GreetingService.class)
 * public class GreetingServiceImpl implements GreetingService {
 *     @Override
 *     public String greeting(String name) {
 *         return "Hello, " + name + "!";
 *     }
 * }
 * }</pre></li>
 * <li>服务消费者
 * <pre>{@code
 * @RestController
 * public class GreetingController {
 *     @RpcReference
 *     private GreetingService greetingService;
 *
 *     @GetMapping("/greeting")
 *     public String greeting(String name) {
 *         return greetingService.greeting(name);
 *     }
 * }
 * }</pre></li>
 * </ol>
 *
 * 参考资料
 * <ol>
 * <li><a href="https://developer.aliyun.com/article/785148">自己动手从0开始实现一个分布式RPC框架</a></li>
 * <li><a href="https://mp.weixin.qq.com/s/-gG7XgwBxTNc1847dfqECA">带你手把手实操一个RPC框架 ｜ 得物技术</a></li>
 * <li><a href="https://juejin.cn/post/6992867064952127524">如何手撸一个较为完整的RPC框架</a>，<a href="https://gitee.com/listen_w/rpc">Gitee</a></li>
 * <li><a href="https://juejin.cn/post/7028939382744694815">Java六种异步转同步方案，总有一款适合你</a></li>
 * <li><a href="https://www.jianshu.com/p/f00aa6f66281">5种必会的Java异步调用转同步的方法你会几种</a></li>
 * <li><a href="https://cn.dubbo.apache.org/zh-cn/overview/reference/protocols/tcp/">Dubbo2 协议规范</a></li>
 * <li><a href="https://www.tianxiaobo.com/2019/01/09/Dubbo-%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90-%E6%9C%8D%E5%8A%A1%E8%B0%83%E7%94%A8%E8%BF%87%E7%A8%8B/">Dubbo 源码分析 - 服务调用过程</a></li>
 * <li><a href="https://cn.dubbo.apache.org/zh-cn/docsv2.7/user/examples/thread-model/">线程模型</a></li>
 * <li><a href="https://cn.dubbo.apache.org/zh-cn/docsv2.7/user/examples/consumer-threadpool/">消费端线程池模型</a></li>
 * <li><a href="https://www.cnblogs.com/tianzhiliang/p/11739372.html">netty中Pipeline的ChannelHandler执行顺序案例详解</a></li>
 * <li><a href="https://juejin.cn/post/7103495227163787278">论Hessian的各种坑爹骚操作</a></li>
 * </ol>
 */
package me.acomma.duck.boot.rpc;
