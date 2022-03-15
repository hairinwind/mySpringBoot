
This project is to compare the restTemplate with webClient performance.  

webClient 确实是用更少的 Threads 和 内存  
但是后端如果不是 NIO 的，如果 webclient 发出的并发请求，超出了后端的最大允许 connections，就会产生大量的错误请求


1000 concurrent users

webclient 
heap: about 75M 
live threads 19
99% response time: 52658
有大量错误请求，因为超出了后端的 max connection

resttemplate
heap: about 160M
live threads 214
99% response time: 26044