package common.feign.timeout;

/**
 * 1. feign timeout
 *
 * <pre>
 *     1. global config
 *        feign:
 *           client:
 *               config:
 *                   default: #表示应用到所有服务提供方(也可以指定服务名称)
 *                       connectTimeout: 4000
 *                       readTimeout: 4000
 *     2. set for each api: Request.Options
 *          @GetMapping(value="/api/xx")
 *          public abstract Resulttest(Request.Options options) ;
 * </pre>
 *
 * 2. retry
 *
 * <pre>
 *     1. 默认超时下, 仅 GET 请求会重试一次
 *     2. 如果显示指定后如果还触发超时,那么feign是不会进行重试
 *     3. 如需要重试则需要自定义: feign.client.config.default.retryer=xxx
 * </pre>
 *
 *
 */
