import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Huffman {

    static final String huffmanFilePath = "/Users/xiaxinyu/Desktop/github-repo/kob/backend/backend/src/main/java/huffman.txt";

    static final String testFilePath = "/Users/xiaxinyu/Desktop/github-repo/kob/backend/backend/src/main/java/test.txt";

    static final int M = 100; //叶子结点数量
    static final int N = 2 * M - 1;//M个叶子结点的huffman树具有2 * N - 1个节点

    static Node[] huffmanTree = new Node[N];
    static String[] huffmanCode = new String[M];
    static double[] w = new double[M]; //每个字符出现的频率

    /**
     * 获取权重最小的两个结点下标
     */
    public static int[] getMin2Node(int k) {
        double min = Integer.MAX_VALUE;
        int[] res = new int[2];
        for (int i = 0; i <= k; i++) {
            if (huffmanTree[i].parent == -1 && huffmanTree[i].weight < min) {
                min = huffmanTree[i].weight;
                res[0] = i;
            }
        }
        min = Integer.MAX_VALUE;
        for (int i = 0; i <= k; i++) {
            if (huffmanTree[i].parent == -1 && huffmanTree[i].weight < min && i != res[0]) {
                min = huffmanTree[i].weight;
                res[1] = i;
            }
        }
        return res;
    }

    /**
     * 构造哈夫曼树
     */
    public static void createHuffmanTree(double[] w) {
        if (M <= 1) return;
        for (int i = 0; i < N; i++) huffmanTree[i] = new Node();
        int i = 0;
        for (; i < M; i++) { //初始化haffman树的M个叶子结点并赋予权重
            huffmanTree[i].parent = -1;
            huffmanTree[i].lChild = -1;
            huffmanTree[i].rChild = -1;
            huffmanTree[i].weight = w[i];
        }
        for (; i < N; i++) { //初始化哈夫曼树的非叶子结点
            huffmanTree[i].parent = -1;
            huffmanTree[i].lChild = -1;
            huffmanTree[i].rChild = -1;
            huffmanTree[i].weight = 0;
        }
        for (i = M; i < N; i++) { //M个叶子结点合并M - 1次
            int[] min2 = getMin2Node(i - 1);//选出权重最小的两个叶节点的下标
            huffmanTree[min2[0]].parent = i;
            huffmanTree[min2[1]].parent = i;
            huffmanTree[i].lChild = min2[0];
            huffmanTree[i].rChild = min2[1];
            huffmanTree[i].weight = huffmanTree[min2[0]].weight + huffmanTree[min2[1]].weight;
        }
    }

    /**
     * 对每个叶结点进行编码
     */
    public static void encode() {

        int start, c, p;
        for (int i = 0; i < M; i++) {
            StringBuilder code = new StringBuilder(); //临时存储每个结点的编码
            c = i;
            p = huffmanTree[i].parent;
            while (p != -1) {
                if (huffmanTree[p].lChild == c) {
                    code.append('0');
                } else {
                    code.append('1');
                }
                c = p;
                p = huffmanTree[p].parent;//递归查找
            }
            huffmanCode[i] = code.reverse().toString();
        }
    }


    /**
     * 打印哈夫曼树节点对应编码
     */
    public static void printHuffman(char[] chars) {//打印哈夫曼树的叶节点对应的编码
        System.out.println();
        System.out.println("每个字符的哈夫曼编码为：");
        System.out.println("字符" + "\t" + "哈夫曼编码" + "\t");
        for (int i = 0; chars[i] != '\0'; i++) {
            System.out.println(chars[i] + "\t" + huffmanCode[i] + "\t");
        }
        System.out.println();
    }


    public static void main(String[] args) {

        File file = new File(testFilePath);
        StringBuilder content = new StringBuilder(); //读取文件内容
        if (file.isFile() && file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String text = null;
                while ((text = bufferedReader.readLine()) != null) {
                    content.append(text);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        int len = content.length();

        int[] cnt = new int[1010]; //记录每个字符出现的次数
        for (int i = 0; i < len; i++) {
            cnt[content.charAt(i)]++;
        }
        char[] chars = new char[1010]; //记录每个不同字符
        int id = 0;
        for (int i = 0; i < 1000; i++) {
            if (cnt[i] != 0) {
                chars[id++] = (char) i;
            }
        }

        System.out.println("文件中不同字符个数 ：" + id);

        int[] strCnt = new int[1010];//不同字符出现的频数
        int k = 0;
        for (int i = 0; i < 1010; i++) {
            if (cnt[i] != 0) {
                strCnt[k++] = cnt[i];
            }
        }

        double[] w = new double[1010];
        System.out.println("字符\t" + "频数\t" + "权重\t");
        for (int i = 0; i < id; i++) {
            w[i] = strCnt[i] * 1.0 / len;
            System.out.println(chars[i] + "\t" + strCnt[i] + "\t" + w[i]);
        }


        createHuffmanTree(w);//构建哈夫曼树
        encode();//对哈夫曼树进行编码
        printHuffman(chars);//打印每个叶节点的编码
        double sum = 0;//哈夫曼树平均编码长度
        for (int i = 0; i < M; i++) {
            sum += huffmanCode[i].length() * w[i];
        }

        System.out.println("哈夫曼树平均编码长度为 : " + sum);

        //开始对文件进行压缩
        StringBuilder huffmanContent = new StringBuilder();

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < id; j++) {
                if (content.charAt(i) == chars[j]) {
                    huffmanContent.append(huffmanCode[j]);
                }
            }
        }

        FileOutputStream fileOutputStream = null;
        file = new File(huffmanFilePath);
        try {
            if (file.exists()) {
                //判断文件是否存在，如果不存在就新建一个txt
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(huffmanContent.toString().getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        int p = N - 1;//根节点
        int i = 0;//指示串的第i个字符
        int len2 = huffmanContent.toString().length();
        StringBuilder decodeContent = new StringBuilder();
        while (i < len2) {
            if (huffmanContent.charAt(i) == '0') {
                p = huffmanTree[p].lChild;
            }
            if (huffmanContent.charAt(i) == '1') {
                p = huffmanTree[p].rChild;
            }
            if (p < M) {//说明此时为叶节点
                decodeContent.append(chars[p]);
                p = N - 1;//重新指向根节点
            }
            i++;
        }
        System.out.println("测试文件内容 ：" + content);
        System.out.println("Huffman编码内容 ：" + huffmanContent);
        System.out.println("解码内容 ：" + decodeContent);
        System.out.println();
        System.out.println("--------------------------");
        StringBuilder asciiContent = new StringBuilder();
        int a = 'c';
        for(i = 0;i < len;i ++){
            int ascii = content.charAt(i);
            asciiContent.append(Integer.toString(ascii,2));
        }
        System.out.println("ASCII平均编码长度为 ：" + asciiContent.length() * 1.0 / content.length());
        System.out.println("ASCII编码内容 ：" + asciiContent);

    }

}

class Node {
    double weight;//权重
    int lChild;//左儿子
    int rChild;//右儿子
    int parent;//父亲
}

