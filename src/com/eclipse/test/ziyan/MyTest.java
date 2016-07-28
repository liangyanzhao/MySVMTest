package com.eclipse.test.ziyan;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

import com.eclipse.test.model.MyModel;
import com.eclipse.test.singleton.MySingleton;
import com.eclipse.test.util.Features;
import com.eclipse.test.util.Util;

public class MyTest {
	public static String fileName = "myaccdata.csv"; // 训练数据文件名
	public static String dir = MyTest.class.getResource(
			"../resource").getPath().substring(1); // resource路径名
	
	// 中间生产文件名
	public static String trainFileNameString = "train";
	public static String scaleFileNameString = "scale";
	public static String rangeFileNameString = "range";
	public static String gridFileNameString = "grid";
	public static String modelFileNameString = "model";
	public static String testTrainFileNameString = "trainTest";
	public static String testResultFileNameString = "trainTestResult";
	
	public static DecimalFormat df = new DecimalFormat("0.00");
	public static String grid_py = "/libsvm/tools/grid.py";
	public static MySingleton mySingleton = MySingleton.getInstance();
	
	public static void main(String[] args) throws Exception {
//		myTrainModel();
		myPredict();
	}

	/**
	 * 识别
	 * @throws IOException 
	 */
	private static void myPredict() throws IOException {
		String[] ruleArrStrings = mySingleton.readRule(dir + "/" + rangeFileNameString);
		int sum = ruleArrStrings.length-2; // 获取标准文件中特征数量
		String[] tempArr = null; // 临时存放每个特征信息label
		// tempArr[0] 最小值:tempArr[1] 最大值:tempArr[2]
		
		// 分析归一化内部文件
		tempArr = ruleArrStrings[1].split(" ");
		double lower = Double.parseDouble(tempArr[0]); // 获取最小值
		double upper = Double.parseDouble(tempArr[1]); // 获取最大值
		
		// 定义数据点
		String trainTest = Util.readFileToString(dir + "/" + testTrainFileNameString);
//		System.out.println(trainTest);
		String[] trainTestLineArry = trainTest.split(Util.getChangeRow());
		String[] trainTestItemArry = null;
		svm_node[] px =null;
		svm_node p = null;
		String[] tempNode = null;
		StringBuffer sb = new StringBuffer();
//		System.out.println("预测结果--- 真实结果-----istrue");
		int cw =0;
		int zq = 0;
		
		for(int j = 0; j < trainTestLineArry.length; j++){
			trainTestItemArry = trainTestLineArry[j].split(" ");
			px = new svm_node[sum];
			for(int i=0;i<9;i++){
				p = new svm_node();
				tempArr = ruleArrStrings[i+2].split(" ");
				tempNode = trainTestItemArry[i+1].split(":");
				p.index = Integer.parseInt(tempNode[0]);
				p.value = Features.zeroOneLibSvm(lower, upper, Double.parseDouble(
						tempNode[1]), Double.parseDouble(tempArr[1]), 
						Double.parseDouble(tempArr[2]));
				px[i] = p;
			}
			svm_model model = svm.svm_load_model(dir + "/" + modelFileNameString);
			double code = svm.svm_predict(model, px);
			if (trainTestItemArry[0].equals(code+"")){
//				System.out.println(code+"  "+trainTestItemArry[0]+" true");
				sb.append(code+"  "+trainTestItemArry[0]+" true");
				zq++;
			} else {
//				System.err.println(code+"  "+trainTestItemArry[0]+" false");
				sb.append(code+"  "+trainTestItemArry[0]+" false");
				cw++;
			}
			sb.append(Util.getChangeRow());
		}
		System.out.println("预测结束：正确：" + zq + " 错误:" + cw + " 正确率：" + df.format(
				zq / (zq + cw + 0.0) * 100) + "%");
		Util.stringToFile(sb.toString(), dir + "/" + testResultFileNameString, false);
	}

	/**
	 * 训练模型
	 * @throws Exception 
	 */
	private static void myTrainModel() throws Exception {
		// 读取文件
		List<MyModel> list = mySingleton.myReadFile(dir, fileName);
		// System.out.println(list.size());
		mySingleton.createTrainFile(list, trainFileNameString, dir);
		// 创建归一化文件
		mySingleton.createScaleFile( 
				new String[]{"-l","0","-u","1","-s",dir+"/"+rangeFileNameString,
						dir+"/"+trainFileNameString}, dir+"/"+scaleFileNameString);
		// 用grid.py计算c g的值
		String[] cgr = mySingleton.cmdGridPy(
				"python "+Util.getSvmPath()+grid_py+" "+dir+"/"+scaleFileNameString,
				dir+"/"+gridFileNameString);
		//生成model模型文件 "-v","5",
		mySingleton.createModeFile(new String[]{"-s","0","-c",cgr[0],
				"-t","2","-g",cgr[1],"-e","0.1",dir+"/"+scaleFileNameString,
				dir+"/"+modelFileNameString});
	}

}

