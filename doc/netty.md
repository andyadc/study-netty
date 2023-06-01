#### ByteBuf 优势

- 池化, 可以重用池中ByteBuf实例, 更节约内存, 减少内存泄露的可能
- 读写指针分离, 不需要像ByteBuff一样需要切换读写模式
- 可以自动扩容
- 支持链式调用, 使用更流程
- 很多地方体现零拷贝, 例如 slice, duplicate, copy, composite