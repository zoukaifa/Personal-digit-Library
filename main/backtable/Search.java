package backtable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 集合了搜索方法的类
 *
 * @author 开发
 */
public class Search {

    private final String lyz;
    private ArrayList<String> result = new ArrayList<>();

    public Search() {
        if (System.getProperty("os.name").startsWith("W")) {
            lyz = "\\\\";
        } else {
            lyz = "/";
        }

    }

    /**
     * 调用该方法会清空txtFolder和otherFolder
     */
    public void Init() {
        File f1 = new File("main/backtable/txtFolder/");
        if (f1.exists()) {
            DeleteDirectory(f1);
        }
        f1.mkdir();
        File f2 = new File("main/backtable/otherFolder/");
        if (f2.exists()) {
            DeleteDirectory(f2);
        }
        f2.mkdir();
    }

    /**
     * 删除文件夹下所有文件（仅适用于无子目录的文件夹）
     *
     * @param path 文件夹名
     */
    public void DeleteDirectory(File path) {
        for (File f : path.listFiles()) {
            f.delete();
        }
    }

    /**
     * 搜索某个磁盘下所有文件
     *
     * @param path 路径
     * @throws Exception
     */
    public void SearchDish(String path) throws Exception {
        try {
            File f = new File(path);
            if (f.isDirectory()) {
                File flist[] = f.listFiles((File pathname) -> !pathname.isHidden()); // 过滤掉隐藏文件夹
                if (flist != null) { // C盘部分文件夹形成数组为null
                    boolean notFirst = false; // 写文件的标志量
                    for (File flist1 : flist) {
                        if (flist1.isDirectory() && flist1.listFiles() != null) { // 非空且非隐藏则递归读取子文件夹
                            SearchDish(flist1.getPath());
                        }
                        if (flist1.isFile() && !flist1.isHidden()) { // 判断文件
                            if (flist1.getName().endsWith("txt")) {
                                MakeFile(flist1, notFirst, "txtFolder/");
                                notFirst = true; // 将标志量置为true，下次写之后的循环中写文件将不再清空
                            } else if (flist1.getName().endsWith("pdf")
                                    || flist1.getName().endsWith("doc")) {
                                MakeFile(flist1, notFirst, "otherFolder/");
                                notFirst = true;
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
        }
    }

    /**
     * 写文件，如果是非txt格式则在行末追加豆瓣信息
     *
     * @param f 文件
     * @param nf 是否在循环中第一次写入文件
     * @param type 文件夹("txtFolder/或者otherFolder/")
     * @throws IOException
     */
    public void MakeFile(File f, boolean nf, String type) throws Exception {
        // 创建文件
        File file = new File("main/backtable/"
                + type
                + f.getParent().replaceAll(lyz, "&&").replaceAll(":&&", "&&&")
                + ".pdl");  // pdl格式防止程序检索
        //System.out.println(file.getAbsolutePath());
        if (!file.exists()) {
            file.createNewFile();
        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, nf)))) {
            bw.write(f.getName());
            //if ("otherFolder/".equals(type)) {
            //	bw.write("??" + FileMatch(f));
            //}
            bw.write("\n");
            bw.close();
        }
    }

    /**
     * 根据传入的正则表达式进行目录搜索
     *
     * @param path 搜索路径
     * @param p 正则表达式
     * @return 内容为文件路径的动态数组
     * @throws Exception
     */
    public ArrayList<String> SearchDish(String path, Pattern p)
            throws Exception {
        ArrayList<String> al = new ArrayList<>();
        try {
            File f = new File(path);
            if (f.isDirectory() && f.listFiles() != null) {
                File flist[] = f.listFiles((File pathname) -> !pathname.isHidden());
                for (File flist1 : flist) {
                    if (flist1.isDirectory() && flist1.listFiles() != null) {
                        SearchDish(flist1.getPath());
                    }
                    if (flist1.isFile() && !flist1.isHidden()) {
                        try (FileReader fr = new FileReader(flist1); BufferedReader br = new BufferedReader(fr)) {
                            while (br.ready()) {
                                String s = br.readLine();
                                Matcher m = p.matcher(s);
                                if (m.find()) {
                                    if (System.getProperty("os.name").startsWith("W")) {
                                        al.add(nameChange(flist1.getName()) + "\\" + s);
                                    } else {
                                        al.add(nameChange(flist1.getName()) + "/" + s);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return al;
    }

    /**
     * 搜索题目
     *
     * @param name 要搜索的名字
     * @param type 文件类型("txtFolder/或者otherFolder/")
     * @throws Exception
     */
    public void NameSearch(String name, String type) throws Exception {
        Pattern p = Pattern.compile(name);
        for (String sd : SearchDish("main/backtable/" + type, p)) {
            result.add(sd);
        }
    }

    public ArrayList<String> getresult() {
        return this.result;
    }

    /**
     * 对txtFolder和otherFolder文件夹下的文件名字进行格式转换
     *
     * @param name 要转换的文件名
     * @return 转换后的名字
     */
    public String nameChange(String name) {
        String temp = name.replaceAll("&&&", ":&&").replaceAll("&&", lyz);
        return temp.substring(0, temp.length() - 4);
    }

    /**
     * 对两个字符串进行模糊匹配
     *
     * @param user 已有的字符串
     * @param douban 被匹配的字符串
     * @return 前者匹配后者词的个数
     * @throws Exception
     */
    public static double NameMatch(String user, String douban) throws Exception {
        ArrayList<String> userList = new ArrayList<>();
        Analyze.testCJK(user, userList);  //将文件名分词；
        int actLength = 0;
        actLength = userList.stream().map((word) -> Pattern.compile(word)).map((p) -> p.matcher(douban)).filter((m) -> (m.find())).map((_item) -> 1).reduce(actLength, Integer::sum);
        return actLength * 1.0 / userList.size();
    }

    /**
     * 对某一文件匹配豆瓣的信息
     *
     * @param user 匹配文件
     * @return 被匹配到的文件的名字（含pdl后缀），如果匹配不到，返回""
     * @throws java.lang.Exception
     */
    public static String FileMatch(File user) throws Exception {
        File file = new File("main/backtable/bookInfo");
        File[] files = file.listFiles();
        File matchFile = null;  //初始化被匹配到的文件
        double num = 0, i; //num代表匹配率，i是循环中每个文件的匹配率
        for (File f : files) {
            i = NameMatch(user.getName(), f.getName());
            if (i >= 0.4 && i > num) {  //匹配率40%以上
                num = i;
                matchFile = f;
            }
        }
        if (matchFile == null) { //未匹配到的情况
            return "null";
        } else {
            return matchFile.getName();
        }
    }

    /**
     * 按作者搜索
     *
     * @param name 作者名
     * @return 相关书籍组成的动态数组
     * @throws Exception
     */
    public ArrayList<String> SearchForAuthor(String name) throws Exception {
        Pattern p = Pattern.compile(name);  //正则匹配作者名
        File f = new File("main/backtable/otherFolder/");
        ArrayList<String> al = new ArrayList<>();  //存放用的动态数组
        File[] files = f.listFiles((File pathname) -> !pathname.isHidden());
        for (File file : files) {
            try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
                while (br.ready()) {
                    String line = br.readLine();
                    if (!line.endsWith("null")) {  //空结尾说明文件不能从bookInfo中匹配
                        File book = new File("main/backtable/bookInfo/" + line.split("&&")[1]);
                        try (BufferedReader br1 = new BufferedReader(new FileReader(book))) {
                            br1.readLine();
                            String s = br1.readLine();  //第二行是作者
                            if (s != null) {
                                Matcher m = p.matcher(s);
                                if (m.find()) {
                                    if (System.getProperty("os.name").startsWith("W")) {
                                        al.add(nameChange(file.getName()) + "\\" + line.split("&&")[0]);
                                    } else {
                                        al.add(nameChange(file.getName()) + "/" + line.split("&&")[0]);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return al;
    }

    /**
     * 调用SearchForAuthor来完成作者名搜索，此方法方便gui调用和测试输出；
     *
     * @param name 作者名
     * @return
     * @throws java.lang.Exception
     */
    public ArrayList<String> AuthorSearch(String name) throws Exception {
        ArrayList<String> re = SearchForAuthor(name);
        return re;
    }
}