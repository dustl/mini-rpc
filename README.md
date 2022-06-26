# 工程简介

结合zookeeper 与netty以及序列化协议 kryo、hessian搭建一个简易的RPC框架。

- 服务端启动后把提供的服务注册到zookeeper,客户端订阅注册中心服务地址。
- 客户端通过动态代理，封装请求参数、获取到的服务地址、端口，序列化请求数据信息，再发送至服务端。
- 服务端对请求的数据进行解码，根据解码后，获取到客户端调用的接口以及参数，调用服务端对应的接口服务。再把调用结果返回给客户端。

### 模块

- consumer为服务消费者，依赖于rpc-client模块、provider-api模块
- provider为服务提供者，依赖于rpc-server模块、provider-api模块
- provider-api为服务暴露api接口
- rpc-client封装客户端发起请求的过程（动态代理、负载均衡、网络通讯）
- rpc-server负责服务发布以及请求处理（反射调用）。

### 组件

#### 动态代理

rpc-client中，`ClientStubInvocationHandler`实现`InvocationHandler`，用于`RpcClientProcessor`，实现`BeanFactoryPostProcessor`扫描所有bean，判断是否被`RpcAutowired`修饰，如果是就把这个属性设置为代理对象，在调用的时候，通过代理对象封装请求数据以及发送请求给服务端。

#### 服务注册与发现

rpc-core中负责zk的服务注册于发现逻辑，供rpc-server使用，并在rpc-server上注册`@RpcService`修饰的的服务，通过`RpcServiceProvider`实现`BeanPostProcessor`扫描服务提供者，依次注册到zk上。

#### 负载均衡

实现在rpc-core中，支持轮询，随机策略，由rpc-client指定策略，通过实现LoadBalance接口，实现自己的自定义负载均衡策略。

#### 自定义消息协议

- 魔数：通讯双方的约定，固定2个字节，防止别人随便往服务端的端口发送数据，每次解码信息需要验证魔数。
- 协议版本号：规定协议的版本号。
- 序列化算法：将发送数据序列化化为二进制。接收方将二进制字节流转换成对象，目前支持hessian、kryo。kryo的性能较好。
- 报文类型：区分是请求还是响应类型，以及心跳等。
- 状态：标识该请求是否正常。
- 消息id:唯一id,记录每个请求。
- 数据长度：记录消息内容的消息长度。
- 数据内容：请求的真实内容。

# 延伸阅读

