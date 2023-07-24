import SwipeQueue.SwipeConsumer;

public class Consumer {
  public static void main(String[] args){
    //Set aws credentials
    System.setProperty("aws.accessKeyId", "ASIATYHSEPEYYWGLVHNR");
    System.setProperty("aws.secretAccessKey", "iXYTU2Jqa4h0vKzUoBG+J2aKWZgUP4+nNLfZ8M5I");
    System.setProperty("aws.sessionToken", "FwoGZXIvYXdzEJL//////////wEaDBSJQA9wFpJpZt+E+iLJAeixT75PcobOQEXyPFmOHcjECd+y8N+6YeB03wTT0onODgiqzcrnrHv+++uog/XGBP/nOTzUM0pZfh9eeCfL1zZZ5YcHevN05ZrOXgltJXuEy2isHATRNSb1sXb1H6l1fl9JCRQyrmoha772r9GuVuWmzX20y3P3S/KyHa8MpollZJ/hvkTGd8HIHt0CNDNzx1+NPAAMa+SPe5mhf277SjsPt6BEwV4kFOJOPh+BfJZqcWXLYUx82LMPXiQJ0ZC5AOR7eqy66hNH8Cjyz/qlBjItMNEFX8bEX3wdzJfMgYHRqnqXhnD8dt/wD/K6VhD/8L8sDgGzGtBNapTM0uD8");

    SwipeConsumer consumer = new SwipeConsumer();
    consumer.start();
  }
}
