---

### NIO & BIO

#### Stream vs Channel

- Stream 不会自动缓冲数据, channel 会利用系统提供的发送缓冲区、 接收缓冲区(更为底层);
- 两者均为全双工, 即读写可以同时进行;
- Stream 仅支持阻塞API, channel 同时支持阻塞, 非阻塞API, 网络channel 可配合selector实现多路复用;

---

#### Selector

selector.select() 何时不阻塞

- 事件发生时;
  - 客户端发送连接请求, 会触发accept 事件;
  - 客户端发送数据过来, 客户端正常/异常关闭时, 都会触发 read 事件, 另外如果发送的数据大于 buffer 缓冲区, 会多次触发 read 事件;
  - channel 可写, 会触发 write 事件;
  - 在 Linux 下 nio bug 发生时;
- 调用selector.wakeup();
- 调用selector.close();
- selector 所在线程interrupt;

---

### IO 模型

* 阻塞IO
* 非阻塞IO
* 多路复用
* 信号驱动
* 异步IO

---
