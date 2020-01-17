## scenario
In a bank project, the bank has some beans for security check, it needs to set up a decryption key as env variable. These beans are not needed for test. It is quite annoying for unit test. 

Setup @Profile("!test") and make the test @ActiveProfiles("test")


