## Spring Cloud Hystrix 服务熔断现状
####1、spring cloud hystrix 会切换线程池
    1）ThreadLocal 比如 
        事务：详见 starbucks 中的 front IndexController.index() 方法 。 好像事务是生效的，但是tm的线程都没有后续了，什么鬼 
    2) 异常捕获，不会打印异常日志。这是个什么原因，原先的线程是终止了吗？(根据结果来看是终止了，原因同事务的demo)
    3) 默认fallback线程池 hystrixTimer