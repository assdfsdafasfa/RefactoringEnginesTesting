 class BBB {
  public BBB(String x) {
  }
}

 class AAA extends BBB {
  public AAA(String x) {
    super(test(x));
  }

  private static String test(String x) {
     String y = x.trim();
     return y.length() > 0 ? y : "aaaa";
  }
}