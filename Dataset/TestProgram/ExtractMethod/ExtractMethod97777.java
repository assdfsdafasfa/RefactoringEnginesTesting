class A{
 public void myTest(List list, String s1, String s2) {        
 }
    
 public void myTest(Collection list, String s1, String s2) {       
 }

 public void usage() {
       List list = new ArrayList();
        String aa = "AA";
        String bb = "bb";
        myTest(list, aa, bb);
        Collection col = new ArrayList();
        myTest(col, aa, bb);
}
}