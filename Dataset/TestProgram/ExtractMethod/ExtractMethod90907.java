class A{
String foo(String strings){
        String s= "foo";
        for (String string : strings) {
            System.out.println(string + s);
        }
        return s;
    }
}