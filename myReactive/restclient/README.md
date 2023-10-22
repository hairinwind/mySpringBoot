
This project is to compare the restTemplate with webClient performance.  

webClient 确实是用更少的 Threads 和 内存  
但是后端如果不是 NIO 的，如果 webclient 发出的并发请求，超出了后端的最大允许 connections，就会产生大量的错误请求  
后来，经过设置 ConnectionProvider 的最大并发数，queue最大等待数，和等待时间，可以避免过多地发送请求去后端  
但是，性能上没有提高，因为后端是 block 的。只是节约了资源


1000 concurrent users

webclient 
heap: about 110M 
live threads 19
99% response time: 26824

resttemplate
heap: about 160M
live threads 214
99% response time: 26044