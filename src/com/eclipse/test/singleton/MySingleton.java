package com.eclipse.test.singleton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eclipse.test.libsvm.svm_scale;
import com.eclipse.test.libsvm.svm_train;
import com.eclipse.test.model.MyModel;
import com.eclipse.test.util.CSVFileUtil;
import com.eclipse.test.util.Constant;
import com.eclipse.test.util.DateUtil;
import com.eclipse.test.util.Util;

/**
 * 单例模式实现
 * @author Ziyan
 *
 */
public class MySingleton {
	private MySingleton() {}
	private static final MySingleton single = new MySingleton();
	public static MySingleton getInstance() {
		return single;
	}
	
	/**
	 * 读取文件
	 * @param dir 文件夹路径
	 * @param fileName 数据文件名
	 * @return
	 * @throws Exception
	 */
	public List<MyModel> myReadFile(String dir, String fileName) throws Exception {
		DateUtil.printNameDate(new Date(), "开始读取 " + fileName + " 文件");
		
		List<MyModel> list = new ArrayList<MyModel>();
		CSVFileUtil csv = new CSVFileUtil(dir + "/" + fileName);
		String str = null;
		ArrayList<String> tmpList = null;
		
		csv.readLine(); // 读取第一行
		
		// 第二行开始读数据
		while((str = csv.readLine()) != null) {
			tmpList = csv.fromCSVLinetoArray(str);
			MyModel tempModel = new MyModel(tmpList);
			/*MyModel tempModel = new MyModel(tmpList.get(1), tmpList.get(2),
					Double.valueOf(tmpList.get(3)),	Double.valueOf(tmpList.get(4)), 
					Double.valueOf(tmpList.get(5)), Double.valueOf(tmpList.get(6)),
					Double.valueOf(tmpList.get(7)), Double.valueOf(tmpList.get(8)), 
					Double.valueOf(tmpList.get(9)),	Double.valueOf(tmpList.get(10)), 
					Double.valueOf(tmpList.get(11)));*/
			list.add(tempModel);
		}
		
		DateUtil.printNameDate(new Date(), "读取 " + fileName + " 文件 完成");
		return list;
	}
	
	/**
	 * 创建训练文件
	 * @param list 模型链表
	 * @param trainFile 训练文件名
	 * @param dir 训练文件夹路径
	 */
	public void createTrainFile(List<MyModel> list, String trainFile, String dir) {
		DateUtil.printNameDate(new Date(), "创建 "+trainFile+" 文件");
		
		StringBuffer tmpStr = new StringBuffer();
		MyModel tempModel = null;
		for (int i = 0; i < list.size(); i++) {
			tempModel = list.get(i);
			// 定义该行训练节点
			tmpStr.append(Constant.actMapToCode.get(tempModel.getAct()));
			// 最小值
			tmpStr.append(" " + Constant.FUN_101_MINIMUM_CODE + ":" + tempModel.getT_min());
			// 最大值
			tmpStr.append(" "+Constant.FUN_102_MAXIMUM_CODE+":"+tempModel.getT_max());
			// 方差 
			tmpStr.append(" "+Constant.FUN_103_VARIANCE_CODE+":"+tempModel.getT_variance());
			// 过均值率
			tmpStr.append(" "+Constant.FUN_104_MEANCROSSINGSRATE_CODE+":"+tempModel.getT_mcr());
			// 标准差
			tmpStr.append(" "+Constant.FUN_105_STANDARDDEVIATION_CODE+":"+tempModel.getT_sttdev());
			// 平均值
			tmpStr.append(" "+Constant.FUN_106_MEAN_CODE+":"+tempModel.getT_mean());
			// 均方根平均值
			tmpStr.append(" "+Constant.FUN_112_RMS_CODE+":"+tempModel.getT_rms());
			// 四分卫距
			tmpStr.append(" "+Constant.FUN_114_IQR_CODE+":"+tempModel.getT_iqr());
			// 绝对平均差
			tmpStr.append(" "+Constant.FUN_115_MAD_CODE+":"+tempModel.getT_mad());
			tmpStr.append(Util.getChangeRow()); // 根据系统不同获取换行符
		}
		
		Util.stringToFile(tmpStr.toString(), dir+"/"+trainFile,false);
		
		DateUtil.printNameDate(new Date(), trainFile+" 文件创建完成");
	}
	
	/**
	 * 训练数据train 进行归一化处理并生生scale文件
	 * @param args String[] args = new String[]{"-l","0","-u","1",path+"/train"}
	 * @param scalePath 结果输出文件名
	 */
	public void createScaleFile(String[] args, String scalePath) {
		DateUtil.printNameDate(new Date(), "开始归一化");
		FileOutputStream fileOutputStream =null;
		PrintStream printStream = null;
		
		try {
			File file = new File(scalePath);
			file.createNewFile();
			fileOutputStream = new FileOutputStream(file);
			printStream = new PrintStream(fileOutputStream);
			// old stream
			PrintStream oldStream = System.out;
			System.setOut(printStream); // 重新定义system.out
			svm_scale.main(args); // 开始归一化
			System.setOut(oldStream); // 回复到system.out
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fileOutputStream!=null){
					fileOutputStream.close();
				}
				if(printStream != null){
					printStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		DateUtil.printNameDate(new Date(), "归一化结束");
	}
	
	/**
	 * 训练c和g的值
	 * @param str python指令
	 * @param gridPath 生产文件名
	 * @return
	 */
	public String[] cmdGridPy(String str,String gridPath) {
		DateUtil.printNameDate(new Date(), "开始计算c g的值");
		String gridString = Util.exeCmd(str);
		System.out.println(str);
		Util.stringToFile(gridString, gridPath, false);
		String gridEndLine = Util.readLastLine(new File(gridPath),null);
		gridEndLine = gridEndLine.substring(0, gridEndLine.indexOf("\n"));
		String[] cgr= gridEndLine.split(" ");
		DateUtil.printNameDate(new Date(),"c g的值计算结束：c="+cgr[0]+" γ="+cgr[1]
				+" CV Rate="+cgr[2]+"%");
		return cgr;
	}
	
	/**
	 * 创建model
	 * @param agrs
	 */
	public void createModeFile(String[] agrs) {
		DateUtil.printNameDate(new Date(),"开始计算model");
		try {
			svm_train.main(agrs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		DateUtil.printNameDate(new Date(),"计算model结束");
	}
	
	/**
	 * 读取归一化标标准文件 以行为单位放到字符串数组里
	 * @param rulePath
	 * @return
	 */
	public String[] readRule(String rulePath) {
		String ruleStr = Util.readFileToString(rulePath);
		String[] ruleArr = ruleStr.split(Util.getChangeRow());
		return ruleArr;
	}
}
