# spring IOC的设计

IOC（Inversion Of Controll，控制翻转）是一种设计思想，将原本在程序中手动创建对象的控制权权，交由spring框架来管理。IOC容器是Spring用来实现IOC的载体，IOC容器实际上就是一个Map(ConcurrentHashMap)，map中存放各种对象。

