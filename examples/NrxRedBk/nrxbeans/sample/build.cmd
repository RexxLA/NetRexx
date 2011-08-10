del *.java
call nrc -keep -format -nocrossref EmpBean EmpBeanBeanInfo AnimBean
rename EmpBean.java.keep          EmpBean.java
rename EmpBeanBeanInfo.java.keep  EmpBeanBeanInfo.java
rename AnimBean.java.keep         AnimBean.java
call nrc -keep -format -nocrossref ViewTime Timer CountDown StopWatch
rename ViewTime.java.keep         ViewTime.java
rename Timer.java.keep            Timer.java
rename CountDown.java.keep        CountDown.java
rename StopWatch.java.keep        StopWatch.java
