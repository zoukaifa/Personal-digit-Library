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

	/**
	 * 搜索某个磁盘下所有文件
	 *
	 * @param path 路径
	 * @throws Exception
	 */
	public void SearchDish(String path) throws Exception {
		try {
			File f = new File(path);
			if (f.isDirectory() && f.listFiles().length != 0) {  //非空文件夹
				File flist[] = f.listFiles();
				boolean notFirst = false;  //写文件的标志量
				for (File flist1 : flist) {
					if (!flist1.isHidden() && flist1.isDirectory() && flist1.listFiles().length != 0) {  //非空且非隐藏则递归读取子文件夹
						SearchDish(flist1.getPath());
					}
					if (flist1.isFile() && !flist1.isHidden()) {  //判断文件
						if (flist1.getName().endsWith("txt")) {
							MakeFile(flist1, notFirst, "txtFolder\\");
							notFirst = true;  //将标志量置为true，下次写之后的循环中写文件将不再清空
						} else if (flist1.getName().endsWith("pdf") || flist1.getName().endsWith("doc")) {
							MakeFile(flist1, notFirst, "otherFolder\\");
							notFirst = true;
						}
					}
				}
			}
		} catch (Exception e) {
			//C盘无法访问
		}
	}

	/**
	 * 写文件
	 *
	 * @param f 文件
	 * @param nf 是否在循环中第一次写入文件
	 * @param type 文件夹
	 * @throws IOException
	 */
	public void MakeFile(File f, boolean nf, String type) throws IOException {
		//创建文件
		File file = new File(System.getProperty("java.class.path") + "\\backtable\\" + type + f.getParent().replaceAll("\\\\", "@@").replaceAll(":@@", "@@@"));
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, nf)));
		bw.write(f.getName() + '\n');
		bw.close();
	}

	/**
	 * 根据传入的正则表达式进行目录搜索
	 *
	 * @param path 搜索路径
	 * @param p 正则表达式
	 * @return 内容为文件路径的动态数组
	 * @throws Exception
	 */
	public ArrayList<String> SearchDish(String path, Pattern p) throws Exception {
		ArrayList<String> al = new ArrayList<>();
		try {
			File f = new File(path);
			if (f.isDirectory() && f.listFiles().length != 0) {
				File flist[] = f.listFiles();
				for (File flist1 : flist) {
					if (!flist1.isHidden() && flist1.isDirectory() && flist1.listFiles().length != 0) {
						SearchDish(flist1.getPath());
					}
					if (flist1.isFile() && !flist1.isHidden()) {
						FileReader fr = new FileReader(flist1);
						BufferedReader br = new BufferedReader(fr);
						while (br.ready()) {
							String s = br.readLine();
							Matcher m = p.matcher(s);
							if (m.find()) {
								al.add(flist1.getName().replaceAll("@@@", ":@@").replaceAll("@@", "\\\\") + s);
							}
						}
						br.close();
						fr.close();
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
	 * @param type 文件类型
	 * @throws Exception
	 */
	public void NameSearch(String name, String type) throws Exception {
		Pattern p = Pattern.compile(name);
		//暂时以打印为输出
		for (String sd : SearchDish(System.getProperty("java.class.path") + "\\backtable\\" + type, p)) {
			System.out.println(sd);
		}
	}
}
