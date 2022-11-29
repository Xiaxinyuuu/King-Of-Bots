import org.junit.jupiter.api.Test;

/**
 * @Description: 测试用例类
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年11月11日 19:31
 * @Copyright: 个人版权所有
 * @version: 1.0.0
 */

public class TestSamples {
    @Test
    public void test1(){
        judgePrime(2,7);
    }

    @Test
    public void test2(){
        judgePrime(2,4);
    }
    void judgePrime(int i,int n){
       while(true){
           if(i < n && n % i != 0){
               i ++;
           }else{
               if(i < n) System.out.println("NO");
               else System.out.println("YES");
               return;
           }
       }
    }
}